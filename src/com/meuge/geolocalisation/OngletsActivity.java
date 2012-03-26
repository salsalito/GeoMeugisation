package com.meuge.geolocalisation;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.google.code.microlog4android.Logger;

public class OngletsActivity extends TabActivity {
	private static Logger logger = LogPersos.getLoggerPerso();
	/** Called when the activity is first created. */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost tabHost =  getTabHost(); 
        //initilatise avant l'affectation des activités
        tabHost.setup(); 
        tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

        
		setupTab(new TextView(this), "Meuge", new Intent(this, MeugeActivity.class));
		setupTab(new TextView(this), "Carte", new Intent(this, CarteActivity.class));
		setupTab(new TextView(this), "Infos", new Intent(this, InfosActivity.class)); 
		tabHost.setCurrentTab(0);
		}
	
	private void setupTab(final View view, final String tag, Intent intent) {
    	View tabview = createTabView(getTabHost().getContext(), tag);


        TabSpec setContent = getTabHost().newTabSpec(tag).setIndicator(tabview).setContent(
         getTabContentFactory(view)).setContent(intent);

    	getTabHost().addTab(setContent);
	}
	/**
	 * @param view
	 * @return
	 */
	private TabContentFactory getTabContentFactory(final View view) {
		return new TabContentFactory() 
          {
              public View createTabContent(String tag) 
              {return view;}
          };
	}
    		 
	private static View createTabView(final Context context, final String text) {
    	View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
    	TextView tv = (TextView) view.findViewById(R.id.tabsText);
    	tv.setText(text);
    	return view;
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
