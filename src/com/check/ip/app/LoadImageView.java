package com.check.ip.app;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class LoadImageView extends AsyncTask<String, Void, Bitmap> {
	protected ImageView map = null;
	public Bitmap staticmap = null;
	protected boolean bw; 
	protected boolean lock = true; 

	public LoadImageView( ImageView bmImage ) {		
		this.map = bmImage;
		bw = false;
	}
	
	public LoadImageView( ImageView bmImage, int result ) {			
		this.map = bmImage;
		this.map.setImageResource(result);
		bw = false;
	}	
	
	public LoadImageView(  ) {		
		bw = false;
	}
	
	public boolean lock() { return lock; }
	
	LoadImageView BW() { bw = true; return this; }
	
	protected Bitmap doInBackground(String... urls) {
		
		lock = true;
		String urldisplay = urls[0];        
		Bitmap temp = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			temp = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		
		if( bw ) 
			staticmap = toBlackWhite(temp);
		else 
			staticmap = temp;
		
		return staticmap;
	}

	protected void onPostExecute(Bitmap result) {
		if( map!= null ) this.map.setImageBitmap(result);
		lock = false;
	}

	public static Bitmap toBlackWhite(Bitmap src) {
		int width = src.getWidth();
		int height = src.getHeight() - 29;
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int R, G, B;
		int pixel;
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {

				pixel = src.getPixel(x, y);
				//A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				if (B == 80 && G == 80 && R == 255) {
					bmOut.setPixel(x, y, pixel);
					continue;
				}

				int gray = ((int) 0xff - (R + G + B) / 3);
				bmOut.setPixel(x, y, Color.argb(0xFF, gray, gray, gray));
			}
		}
		return bmOut;
	}	
}
