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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.db4o.foundation.Collection4;


import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class SongsActivity extends ExpandableListActivity {

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
	
	 private ArrayList<HashMap<String,String>> createParents(Hashtable<String, ArrayList<String>> categories) {
         ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();
         for( Enumeration<String> i = categories.keys() ; i.hasMoreElements() ;  ) { // n groups........
           HashMap<String, String> m = new HashMap<String, String>();
           m.put( "Group Item",i.nextElement()); // cle + valeur
           result.add( m );
         }
         return result;
   }
	 
	 
	 
	 private ArrayList<ArrayList<HashMap<String, String>>> createEnfants(Hashtable<String, ArrayList<String>> categories) {
		    NumberFormat formatter = new DecimalFormat("#,###");
	        ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
	        Enumeration<String> mesKeys = categories.keys();
	        for( int i = 0 ; i < categories.size() ; i++ ) { 
	          ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
	          ArrayList<String> tmpR =mesKeys.hasMoreElements() ? categories.get(categories.keys().nextElement()) : new ArrayList<String>();
	          for (int elements = 0; elements < tmpR.size() ; elements++)
	          {
	        	  
	        	  HashMap<String, String> child = new HashMap<String, String>();
		           child.put( "Sub Item", tmpR.get(elements));
		           secList.add( child );
	          }
	         result.add( secList );
	        }
	        return result;
	    }
	
	
	private void ecritureTexte()
    {
//		arrayInfos[0] = (double) 48.858219;
//		arrayInfos[1] = (double) 2.294498;
		//Coordonnees tour eiffel
		// Recuperation des informations passées par les onglets
		
		String resultat = "Aucune position définie !!!";
   //     Bundle extras = getParent().getIntent().getExtras();
        if (BundleTools.getLatitude() !=(double)0 && BundleTools.getLongitude() != (double) 0)
        {

	        CoordonneesPOI coordonnesPassees = new CoordonneesPOI();
	        coordonnesPassees.setLatitude(BundleTools.getLatitude());
	        coordonnesPassees.setLongitude(BundleTools.getLongitude());
	        coordonnesPassees.setPositions(CalculLatLong.calculate(BundleTools.getLatitude(), BundleTools.getLongitude()));
	    	CoordonneesPOIProvider cp = new CoordonneesPOIProvider(CoordonneesPOI.class, this);
			List<CoordonneesPOI> tmp = cp.findAllLastMax(-1);
			TreeSet<KmsCalcules> monTri = new TreeSet<KmsCalcules>(new CollectionComparator());
			for (CoordonneesPOI i : tmp)
			{
				double tempFormula = Math.acos(formula(coordonnesPassees.getPositions(), i.getPositions())) * (double)6371;
				KmsCalcules tmpKms = new KmsCalcules(tempFormula, i.getCategorie(), i.getAdresse());
				monTri.add(tmpKms);
			}
			cp.close();
			cp.db().close();
			

			Hashtable<String, ArrayList<String>> categories = new Hashtable<String, ArrayList<String>>();
			for (Iterator<KmsCalcules> i= monTri.iterator(); i.hasNext();)
			{
				if (categories.size() < 6)
				{
					KmsCalcules meskms = (KmsCalcules) i.next();
					ArrayList<String> tmpCats = categories.get(meskms.getCategorie()) == null ? new ArrayList<String>() : categories.get(meskms.getCategorie()) ;
					
					if (!categories.containsKey(meskms.getCategorie()))
					{
						categories.put(meskms.getCategorie(), new ArrayList<String>());
					}
					tmpCats.add(meskms.getInformations());
					categories.put(meskms.getCategorie(), tmpCats);
				}
				else break;
			}
			
			SimpleExpandableListAdapter expListAdapter =
		            new SimpleExpandableListAdapter(
		                    this,
		                    createParents(categories),              // Creating group List.
		                    R.layout.parents,             // Group item layout XML.
		                    new String[] { "Group Item" },  // the key of group item.
		                    new int[] { R.id.grp_parent },    // ID of each group item.-Data under the key goes into this TextView.
		                    createEnfants(categories),              // childData describes second-level entries.
		                    R.layout.enfants,             // Layout for sub-level entries(second level).
		                    new String[] {"Sub Item"},      // Keys in childData maps to display.
		                    new int[] { R.id.grp_enfant}     // Data under the keys above go into these TextViews.
		                );
		            setListAdapter( expListAdapter );       // setting the adapter in the list.


			
			
//			NumberFormat formatter = new DecimalFormat("#,###");
//			Iterator<KmsCalcules> meskmsTries = monTri.iterator();
//			resultat = "";
//			for (int i =0 ; i < 7; i++)
//			{
//				if (meskmsTries.hasNext())
//				{
//					KmsCalcules meskms = (KmsCalcules) meskmsTries.next();
//					resultat +=  formatter.format(meskms.getNbKms()) + " Km => "+ meskms.getCategorie()+ " : "+meskms.getInformations()+"\n";
//				}
//			}
	    }
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