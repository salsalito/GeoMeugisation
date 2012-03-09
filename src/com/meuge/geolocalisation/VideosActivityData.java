package com.meuge.geolocalisation;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class VideosActivityData extends Overlay{
	private GeoPoint p;
    private Resources res;
    
	public VideosActivityData(GeoPoint p0, Resources resources0) {
		p = p0;
		res = resources0;
	}
	 @Override
      public boolean draw(Canvas canvas, MapView mapView, 
      boolean shadow, long when) 
      {
          super.draw(canvas, mapView, shadow);                   

          //---translate the GeoPoint to screen pixels---
          Point screenPts = new Point();
          mapView.getProjection().toPixels(p, screenPts);

          //---add the marker---
          Bitmap bmp = BitmapFactory.decodeResource(
              res, R.drawable.fleche);            
          canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
          return true;
      }
}