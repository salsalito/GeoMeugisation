package com.meuge.geolocalisation;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class CarteActivityData extends Overlay{
	private GeoPoint p;
    private Resources res;
    private Context con;
    
	public CarteActivityData(GeoPoint p0, Resources resources0, Context context0) {
		p = p0;
		res = resources0;
		con = context0;
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
  @Override
  public boolean onTouchEvent(MotionEvent event, MapView mapView) 
  {   
      //--- On touche un autre point de la carte ---
      if (event.getAction() == 1) {                
          GeoPoint p = mapView.getProjection().fromPixels(
              (int) event.getX(),
              (int) event.getY());
          Geocoder geoCoder = new Geocoder(
                  con, Locale.getDefault());    
          try {
              List<Address> addresses = geoCoder.getFromLocation(
                  p.getLatitudeE6()  / 1E6, 
                  p.getLongitudeE6() / 1E6, 1);

              String add = "";
              if (addresses.size() > 0) 
              {
                  for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                       i++)
                     add += addresses.get(0).getAddressLine(i) + "\n";
              }

              Toast.makeText(con, add, Toast.LENGTH_SHORT).show();
          }
          catch (IOException e) {                
              e.printStackTrace();
          }   
          return true;
      }
      return false;
  }
}