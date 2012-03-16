package com.meuge.geolocalisation;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class OngletsActivity extends TabActivity {
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost tabHost =  getTabHost(); 
        //initilatise avant l'affectation des activités
        tabHost.setup(); 
        
        // Tab for Photos
        TabSpec meugespec = tabHost.newTabSpec("Meuge");
        meugespec.setIndicator("Meuge", getResources().getDrawable(R.drawable.icon_meuge_tab));
        Intent photosIntent = new Intent(this, MeugeActivity.class);
//        photosIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        meugespec.setContent(photosIntent);
        
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Datas");
        // setting Title and Icon for the Tab
        songspec.setIndicator("Songs", getResources().getDrawable(R.drawable.icon_songs_tab));
        Intent songsIntent = new Intent(this, SongsActivity.class);
        songsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        songspec.setContent(songsIntent);
        
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Carte");
        videospec.setIndicator("Videos", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent videosIntent = new Intent(this, VideosActivity.class);
//        videosIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        videospec.setContent(videosIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
        tabHost.addTab(meugespec); // Adding meuge tab
        tabHost.setCurrentTab(2);
        


    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
