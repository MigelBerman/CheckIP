package com.check.ip.app;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import org.json.JSONObject;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * Copyright (C) 2019 @author wood
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

public class CheckActivity extends Activity implements View.OnClickListener {
	
	EditText ip = null;
	TextView json = null;
	TextView location = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		_findView(R.id.lookup);		
		((Button) _findView(R.id.button1)).setText("close");
						
		ip       = (EditText)_findView(R.id.ip);
		json     = (TextView)_findView(R.id.json);
		location = (TextView)_findView(R.id.location);
	
		Uri data = getIntent().getData();
       
		String host = (data == null)? ip.getText().toString() : data.getHost();
				
		new GeoApiIp().execute(String.format(GEOIP_API, host));
		
		ip.setText(host);
	
	}

	public View _findView(int id) {
		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1)
			this.finish();
		if (v.getId() == R.id.lookup)			
			new GeoApiIp().execute(String.format(GEOIP_API, ip.getText().toString()));
	}

	public static final String GEOIP_API = "http://ip-api.com/json/%s?fields=status,message,country,countryCode,region,regionName,city,zip,lat,lon,timezone,isp,org,as,query&lang=ru";
	
	private class GeoApiIp extends HttpGet {
		@Override
		void handlerResult(String recive) {
			JSONObject result = null;
			try {
				result = new JSONObject(recive);
			} catch (Exception e) {
				json.setText(e.toString());
				return;
			}
			
			if(result.optString("status").equals("fail")) {
				json.setText("Request status fail.");
				return;
			}
					
			StringBuilder builder = new StringBuilder("You IP: " + result.optString("query"))
					.append("\n"+result.optString("country")).append(", ")
					.append(result.optString("zip")).append(", ")
					.append(result.optString("regionName")).append(", ")
					.append(result.optString("city")).append(".\n")
					.append("timezone " + result.optString("timezone"))
					.append(".\n").append(result.optString("as")).append(".\n");
		
			json.setText(builder.toString());
		
			builder.setLength(0);
			builder.append("\nLat: " + result.optString("lat")).append("\n")
					.append("Lon: " + result.optString("lon")).append("\n");
			
			location.setText(builder.toString());
			
			String s = "https://static-maps.yandex.ru/1.x/?lang=ru_RU&ll=32.810152,39.889847&size=400,400&z=12&l=map";
			s = s.replace("32.810152,39.889847",result.optString("lon") + "," + result.optString("lat"));
			new LoadImageView((ImageView) findViewById(R.id.map)).BW().execute(s);			
	}		
	}	
}