package com.meuge.geolocalisation;

import java.util.List;

import com.db4o.ObjectContainer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SongsActivity extends Activity {
	 private static Db4oHelper db4oHelper ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_layout);   
        ecritureTexte();
    }

    private void ecritureTexte()
    {
    	String resultat = "";
    	CoordonneesProvider cp = new CoordonneesProvider(Coordonnees.class, this);
		cp.db();
		List<Coordonnees> tmp = cp.findAll();
		for (Coordonnees i : tmp)
		{
			resultat += i.getLatitude() + " " + i.getLongitude() + " => "+ i.getAdresse()+"\n";
		}
    	((TextView)findViewById(R.id.monChamp)).setText(resultat);
    	cp.close();
    	cp.db().close();
    	
    }
}