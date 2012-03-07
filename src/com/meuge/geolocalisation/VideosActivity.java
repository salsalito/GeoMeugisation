/**
 * 
 */
package com.meuge.geolocalisation;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

/**
 * @author 
 *
 */
public class VideosActivity extends MapActivity {

	   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videos_layout);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
