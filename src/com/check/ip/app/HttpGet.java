package com.check.ip.app;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;

public abstract class HttpGet extends AsyncTask<String, Integer, String> {
	
	abstract void handlerResult ( String recive );
	
	@Override
	protected String doInBackground(String... params) {		
		try {
			HttpURLConnection httpConn = (HttpURLConnection) new URL(params[0]).openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoOutput(true); // indicates POST method
			httpConn.setDoInput(true);

			int status = httpConn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				throw new Exception("Server returned non-OK status: " + status	+ " " + params[0]);
			}

			String data = readFully(httpConn.getInputStream()).toString("UTF-8");
			httpConn.disconnect();
			return data;
		} catch (Exception e) {
			return e.toString();
		}		
	}
	
	protected void onPostExecute( String result ) { handlerResult( result ); }
		
	public static ByteArrayOutputStream readFully(InputStream inputStream)	throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos;
	}	

}
