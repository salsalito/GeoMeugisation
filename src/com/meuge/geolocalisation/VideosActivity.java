/**
 * 
 */
package com.meuge.geolocalisation;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * @author 
 *
 */
public class VideosActivity extends MapActivity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	double []arrayInfos = new double[2];
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_layout);
        // Recuperation des informations passées par les onglets
        Bundle extras = getParent().getIntent().getExtras();
        arrayInfos = (double []) extras.getDoubleArray("GPSINFO");
        String adresse = (String) extras.getString("ADRESSEINFO");
        
        MapView mapView = (MapView) findViewById(R.id.mapView);
        MapController mc = mapView.getController();
        GeoPoint p = new GeoPoint(
        		(int) (arrayInfos[0] * 1E6), 
        		(int) (arrayInfos[1] * 1E6));
        //Positionnement de nos coordonnées
        mc.animateTo(p);
        mc.setZoom(17);
        VideosActivityData mapOverlay = new VideosActivityData(p,getResources());
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
        mapView.invalidate();
        // Vue des rues
        mapView.setStreetView(true);
        ZoomControls zommControls = (ZoomControls)  findViewById(R.id.zoomcontrols);
        zommControls.setClickable(true);
    }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
