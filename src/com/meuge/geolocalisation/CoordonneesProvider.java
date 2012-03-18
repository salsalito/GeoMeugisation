package com.meuge.geolocalisation;


import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

import android.content.Context;

public class CoordonneesProvider extends DbProvider<Coordonnees> {

	public CoordonneesProvider(Class<Coordonnees> persistentClass, Context ctx) {
		super(persistentClass, ctx);
		db();
		// TODO Auto-generated constructor stub
	}
    //Trouve la dernière coordonnée selon la latitude et la longitude
	
	public List<Coordonnees> findByLatLong (Coordonnees coord)
	{
		List<Coordonnees> retour = null;
		try {
			ObjectSet<Coordonnees> resultat ;
			Query query = getQuery();
			Constraint lonT = query.descend("longitude").constrain(coord.getLongitude());
			query.descend("latitude").constrain(coord.getLatitude()).and(lonT);
			query.descend("id").orderDescending();
			resultat = query.execute();
			retour = findMax(resultat,1);
		} catch (NullPointerException e) {
			//Base vide
			 retour = new ArrayList<Coordonnees>();
		}
		return retour;
	}

	/**
	 * @param coord
	 * @return
	 */
	private Query getQuery() {
		Query query = db().query();
		query.constrain(Coordonnees.class);
		return query;
	}
	
	// Retourne les n derniers coordonnées
	public List<Coordonnees> findAllLastMax(int limit)
	{
		List<Coordonnees> retour = null;
		try {
			Query query = getQuery();
			query.descend("id").orderDescending();
			ObjectSet<Coordonnees> resultat = query.execute();
			retour = findMax(resultat,limit);
		} catch (NullPointerException e) {
			//Base vide
			retour = findAllMax(limit);
		}
		return retour;
	}
}
