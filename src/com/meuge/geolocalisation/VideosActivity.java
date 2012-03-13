/**
 * 
 */
package com.meuge.geolocalisation;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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

	  public boolean onKeyDown(int keyCode, KeyEvent event) 
	    {
		  MapView mapView = (MapView) findViewById(R.id.mapView);  
		  MapController mc = mapView.getController(); 
	        switch (keyCode) 
	        {
	            case KeyEvent.KEYCODE_3:
	                mc.zoomIn();
	                break;
	            case KeyEvent.KEYCODE_1:
	                mc.zoomOut();
	                break;
	        }
	        return super.onKeyDown(keyCode, event);
	    }    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	double []arrayInfos = new double[2];
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_layout);
        if (isOnline())
        	afficheCarte();
       
    }

    private boolean isOnline() {
    	ConnectivityManager connectivityManager =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }
	private void afficheCarte() {
		double[] arrayInfos;
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
        VideosActivityData mapOverlay = new VideosActivityData(p,getResources(), getBaseContext());
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
        mapView.invalidate();
        // Vue des rues
        mapView.setStreetView(true);
        ZoomControls mControls = (ZoomControls)  findViewById(R.id.zoomcontrols);
        mControls.setOnZoomInClickListener(ListenerZoomIn(mc));
        mControls.setOnZoomOutClickListener(ListenerZoomOut(mc));
	}
	/**
	 * Diminue le zoom
	 */
	private View.OnClickListener ListenerZoomOut(final MapController mc) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mc.zoomOut();
			}
		};
	}

	/**
	 * Agrandit le zoom
	 */
	private View.OnClickListener ListenerZoomIn(final MapController mc) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mc.zoomIn();
			}
		};
	}
		
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
