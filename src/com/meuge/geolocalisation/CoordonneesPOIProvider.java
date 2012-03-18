package com.meuge.geolocalisation;


import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import android.content.Context;

public class CoordonneesPOIProvider extends DbProvider<CoordonneesPOI> {
	private Context contextCreated;
	public CoordonneesPOIProvider(Class<CoordonneesPOI> persistentClass, Context ctx) {
		super(persistentClass, ctx);
		this.contextCreated = ctx;
		db();
		// TODO Auto-generated constructor stub
	}
    //Trouve la dernière coordonnée selon la latitude et la longitude
	
	public List<CoordonneesPOI> findByLatLong (CoordonneesPOI coord)
	{
		List<CoordonneesPOI> retour = null;
		try {
			ObjectSet<CoordonneesPOI> resultat ;
			Query query = getQuery();
			Constraint lonT = query.descend("longitude").constrain(coord.getLongitude());
			query.descend("latitude").constrain(coord.getLatitude()).and(lonT);
			query.descend("id").orderDescending();
			resultat = query.execute();
			retour = findMax(resultat,1);
		} catch (NullPointerException e) {
			//Base vide
			 retour = new ArrayList<CoordonneesPOI>();
		}
		return retour;
	}

	/**
	 * @param coord
	 * @return
	 */
	private Query getQuery() {
		Query query = db().query();
		query.constrain(CoordonneesPOI.class);
		return query;
	}
	
	// Retourne les n derniers coordonnées
	public List<CoordonneesPOI> findAllLastMax (int limit)
	{
		List<CoordonneesPOI> retour = null;
		try {
			Query query = getQuery();
			query.descend("id").orderDescending();
			ObjectSet<CoordonneesPOI> resultat = query.execute();
			retour = findMax(resultat,limit);
		} catch (NullPointerException e) {
			//Base vide
			retour = findAllMax(limit);
			if (retour.size()==0)
				Magasins_But.Magasins_But(contextCreated);
		}
		return retour;
	}
	
}
