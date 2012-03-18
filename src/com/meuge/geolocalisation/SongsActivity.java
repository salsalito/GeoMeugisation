package com.meuge.geolocalisation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.db4o.foundation.Collection4;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        if (isNetworkAvailable()) {
        	Bitmap imageLoaded = chargeImage("http://www.google.fr/intl/en_com/images/srpr/logo1w.png");
        	if (imageLoaded != null)
        		monImage.setImageBitmap(imageLoaded);
        }
    }

    
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	private double formula(CalculLatLong coordsCalculate, CalculLatLong toCoord)
	{
		return (coordsCalculate.getCOSLAT() * toCoord.getCOSLAT()
				*(toCoord.getCOSLNG() * coordsCalculate.getCOSLNG()
			    +toCoord.getSINLNG() * coordsCalculate.getSINLNG()
			    )+coordsCalculate.getSINLAT() * toCoord.getSINLAT());
	}
	private void ecritureTexte()
    {
//		arrayInfos[0] = (double) 48.858219;
//		arrayInfos[1] = (double) 2.294498;
		//Coordonnees tour eiffel
		// Recuperation des informations passées par les onglets
        Bundle extras = getParent().getIntent().getExtras();
        if (extras !=null)
        {
        	double[] arrayInfos=new double[2];
        	arrayInfos = (double []) extras.getDoubleArray("GPSINFO");
	        Coordonnees coordonnesPassees = new Coordonnees();
	        coordonnesPassees.setLatitude(arrayInfos[0]);
	        coordonnesPassees.setLongitude(arrayInfos[1]);
	        coordonnesPassees.setPositions(CalculLatLong.calculate(coordonnesPassees.getLatitude(), coordonnesPassees.getLongitude()));
	    	String resultat = "";
	    	CoordonneesPOIProvider cp = new CoordonneesPOIProvider(CoordonneesPOI.class, this);
			List<CoordonneesPOI> tmp = cp.findAllLastMax(-1);
			TreeSet<KmsCalcules> monTri = new TreeSet<KmsCalcules>(new CollectionComparator());
			for (CoordonneesPOI i : tmp)
			{
				double tempFormula = Math.acos(formula(coordonnesPassees.getPositions(), i.getPositions())) * (double)6371;
				KmsCalcules tmpKms = new KmsCalcules(tempFormula, i.getCategorie());
				monTri.add(tmpKms);
			}
			cp.close();
			cp.db().close();
			NumberFormat formatter = new DecimalFormat("#,###");
			Iterator<KmsCalcules> meskmsTries = monTri.iterator();
			for (int i =0 ; i < 5; i++)
			{
				if (meskmsTries.hasNext())
				{
					KmsCalcules meskms = (KmsCalcules) meskmsTries.next();
					resultat +=  formatter.format(meskms.getNbKms()) + " Km => "+ meskms.getInformations()+"\n";
				}
			}
	    	((TextView)findViewById(R.id.monChamp)).setText(resultat);
	        }
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