/**
 * 
 */
package com.meuge.geolocalisation;

import android.os.Bundle;
import android.widget.ZoomControls;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

/**
 * @author 
 *
 */
public class VideosActivity extends MapActivity {

	   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	double[] coords = new double[2];
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_layout);
        MapView mapView = (MapView) findViewById(R.id.mapView);
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
