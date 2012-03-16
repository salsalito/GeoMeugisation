package com.meuge.geolocalisation;

import java.util.List;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SongsActivity extends Activity {
	//private static Db4oHelper db4oHelper ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_layout);   
        ecritureTexte();
    }

    private void ecritureTexte()
    {
    	String resultat = "";
    	CoordonneesProvider cp = new CoordonneesProvider(Coordonnees.class, this);
		List<Coordonnees> tmp = cp.findAllMax(-1);
		for (Coordonnees i : tmp)
		{
			resultat += i.getLatitude() + " " + i.getLongitude() + " => "+ i.getAdresse()+"\n";
		}
		cp.close();
		cp.db().close();
    	((TextView)findViewById(R.id.monChamp)).setText(resultat);
    	
    }
}