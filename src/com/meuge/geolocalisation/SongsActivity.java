package com.meuge.geolocalisation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SongsActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_layout);   
        ecritureTexte();
        ImageView monImage = (ImageView)findViewById(R.id.imageview);
        Bitmap imageLoaded = chargeImage("http://www.google.fr/intl/en_com/images/srpr/logo1w.png");
        if (imageLoaded != null)
        	monImage.setImageBitmap(imageLoaded);
    }

    private void ecritureTexte()
    {
    	String resultat = "";
    	CoordonneesProvider cp = new CoordonneesProvider(Coordonnees.class, this);
		List<Coordonnees> tmp = cp.findAllLastMax(5);
		for (Coordonnees i : tmp)
		{
			resultat += i.getLatitude() + " " + i.getLongitude() + " => "+ i.getAdresse()+"\n";
		}
		cp.close();
		cp.db().close();
    	((TextView)findViewById(R.id.monChamp)).setText(resultat);
    	
    }
    private Bitmap chargeImage(String myURL)
    {

        	Bitmap bitmap = null;

        	try {

        	URL urlImage = new URL(myURL);

        	HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();

        	InputStream inputStream = connection.getInputStream();

        	bitmap = BitmapFactory.decodeStream(inputStream);

       // 	image.setImageBitmap(bitmap);

        	} catch (MalformedURLException e) {

        		Log.e ("Image","Erreur sur URL l 'image" );

        	} catch (IOException e) {

        	Log.e ("Image","Erreur sur l 'image" );

        	}
        	return bitmap;
    }
}