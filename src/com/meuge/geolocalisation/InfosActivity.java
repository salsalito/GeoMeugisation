package com.meuge.geolocalisation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.code.microlog4android.Logger;
import com.meuge.adapter.ExpandableListAdapter;


public class InfosActivity extends ExpandableListActivity {

	private static Logger logger = LogPersos.getLoggerPerso();
	
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
	 
	 private ArrayList<String> getParents(Hashtable<String, ArrayList<String>> categories) {
		 ArrayList<String> result = new ArrayList<String>();
		 for( Enumeration<String> i = categories.keys() ; i.hasMoreElements() ;  ) { // n groups........
			 result.add(i.nextElement());
		 }
		 return result;
	 }
	 
	
	
	private void ecritureTexte()
    {
		// Recuperation des informations passées par les onglets
		
		String resultat = "Aucune position définie !!!";
   //     Bundle extras = getParent().getIntent().getExtras();
        if (BundleTools.getLatitude() !=(double)0 && BundleTools.getLongitude() != (double) 0)
        {
	        CoordonneesPOI coordonnesPassees = new CoordonneesPOI();
	        coordonnesPassees.setLatitude(BundleTools.getLatitude());
	        coordonnesPassees.setLongitude(BundleTools.getLongitude());
	        
	    	CoordonneesPOIProvider cp = new CoordonneesPOIProvider(CoordonneesPOI.class, this);
	        coordonnesPassees.setPositions(CalculLatLong.calculate(BundleTools.getLatitude(), BundleTools.getLongitude()));
	    	logger.info("Debut Requete FindAllMax");
	    	List<CoordonneesPOI> tmp = cp.findAllMax(CoordonneesPOIProvider.ALLRECORDS);
	    	logger.info("Debut Tri de FindAllMax");
			TreeSet<KmsCalcules> monTri = new TreeSet<KmsCalcules>(new CollectionComparator());
			logger.info("Début de Calcul de Kms");
			for (CoordonneesPOI i : tmp)
			{
				float results[]=new float[1];
		    	Location.distanceBetween(BundleTools.getLatitude(), BundleTools.getLongitude(), i.getLatitude(), i.getLongitude(), results);
		    	double tempFormula = Double.valueOf(Float.toString(results[0])) / (double)1000.0;
				KmsCalcules tmpKms = new KmsCalcules(tempFormula, i.getCategorie(), i.getAdresse());
				monTri.add(tmpKms);
			}
			cp.close();
			cp.db().close();

			logger.info("Début de Calcul de Mise a Dispos des Kms");
			Hashtable<String, ArrayList<String>> categories = new Hashtable<String, ArrayList<String>>();
			ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, getParents(categories),
					new ArrayList<ArrayList<KmsCalcules>>());
			
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
					expListAdapter.addItem(meskms);
					tmpCats.add(meskms.getInformations() + "["+new DecimalFormat("#,###.#").format(meskms.getNbKms())+"Kms]");
					categories.put(meskms.getCategorie(), tmpCats);
				}
				else break;
			}
			
			
			
//			SimpleExpandableListAdapter expListAdapter =
//		            new SimpleExpandableListAdapter(
//		                    this,
//		                    createParents(categories),              // Creating group List.
//		                    R.layout.parents,             // Group item layout XML.
//		                    new String[] { "Group Item" },  // the key of group item.
//		                    new int[] { R.id.grp_parent },    // ID of each group item.-Data under the key goes into this TextView.
//		                    createEnfants(categories),              // childData describes second-level entries.
//		                    R.layout.enfants,             // Layout for sub-level entries(second level).
//		                    new String[] {"Sub Item"},      // Keys in childData maps to display.
//		                    new int[] { R.id.grp_enfant}     // Data under the keys above go into these TextViews.
//		                );
		            setListAdapter( expListAdapter );       // setting the adapter in the list.
		            logger.info("Fin de Calcul de Mise a Dispos des Kms");


			
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
        		logger.error("Erreur sur URL l 'image : "+ myURL);
        	} catch (IOException e) {
        		logger.error("Erreur sur URL l 'image : "+ myURL);
        	}
        	return bitmap;
    }
}