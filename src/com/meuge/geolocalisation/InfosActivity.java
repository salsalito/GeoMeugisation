package com.meuge.geolocalisation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.google.code.microlog4android.Logger;
import com.meuge.adapter.InteractiveArrayAdapter;
import com.tools.meuge.Model;

import android.app.ListActivity;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class InfosActivity extends ListActivity {
	private static Logger logger = LogPersos.getLoggerPerso();
	/** Called when the activity is first created. */

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Create an array of Strings, that will be put to our ListActivity
		ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this,
				getModel(ecritureTexte()));
		setListAdapter(adapter);
	}

	
	private TreeSet<KmsCalcules> ecritureTexte()
    {
		TreeSet<KmsCalcules> monTri = new TreeSet<KmsCalcules>(new CollectionComparator());
		// Recuperation des informations passées par les onglets
		
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
//			Hashtable<String, ArrayList<String>> categories = new Hashtable<String, ArrayList<String>>();
//			ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, getParents(categories),
//					new ArrayList<ArrayList<KmsCalcules>>());
//			
//			for (Iterator<KmsCalcules> i= monTri.iterator(); i.hasNext();)
//			{
//				if (categories.size() < 6)
//				{
//					KmsCalcules meskms = (KmsCalcules) i.next();
//					ArrayList<String> tmpCats = categories.get(meskms.getCategorie()) == null ? new ArrayList<String>() : categories.get(meskms.getCategorie()) ;
//					
//					if (!categories.containsKey(meskms.getCategorie()))
//					{
//						categories.put(meskms.getCategorie(), new ArrayList<String>());
//					}
//					expListAdapter.addItem(meskms);
//					tmpCats.add(meskms.getInformations() + "["+new DecimalFormat("#,###.#").format(meskms.getNbKms())+"Kms]");
//					categories.put(meskms.getCategorie(), tmpCats);
//				}
//				else break;
//			}
//			
//		       // Retrive the ExpandableListView from the layout
//					ExpandableListView listView = getExpandableListView();
//					
//					listView.setOnChildClickListener(retourneInfosEnfantsFromClick());
//			        
//			        listView.setOnGroupClickListener(retourneInfosParentsFromClick());
//					
//					
//		            setListAdapter( expListAdapter );       // setting the adapter in the list.
		            logger.info("Fin de Calcul de Mise a Dispos des Kms");
	    }
        return monTri;
    }
	
	
	private List<Model> getModel(TreeSet<KmsCalcules> monTri) {
  		List<Model> list = new ArrayList<Model>();
  		List<String> liste = new ArrayList<String>();
  		int categories=0;
  		Iterator<KmsCalcules> i= monTri.iterator();
		while (categories < 6)
		{
			if (monTri.isEmpty() || !i.hasNext()) 
				categories = 7;
			else {
				KmsCalcules meskms = (KmsCalcules) i.next();
				if (!liste.contains(meskms.getCategorie()))
				{
					categories++;
					liste.add(meskms.getCategorie());
					list.add(get(meskms.getCategorie()));
				}
			}
		}

//		list.add(get("Linux"));
//		list.add(get("Windows7"));
//		list.add(get("Suse"));
//		list.add(get("Eclipse"));
//		list.add(get("Ubuntu"));
//		list.add(get("Solaris"));
//		list.add(get("Android"));
//		list.add(get("iPhone"));
		// Initially select one of the items
//		list.get(1).setSelected(true);
		return list;
	}

	private Model get(String s) {
		return new Model(s);
	}
}
