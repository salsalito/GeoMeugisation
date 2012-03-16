package com.meuge.geolocalisation;


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
		Query query = getQuery();
		Constraint lonT = query.descend("longitude").constrain(coord.getLongitude());
		query.descend("latitude").constrain(coord.getLatitude()).and(lonT);
		query.descend("id").orderAscending();
		ObjectSet<Coordonnees> resultat = query.execute();
		return findMax(resultat,1);
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
	public List<Coordonnees> findAllLastMax (int limit)
	{
		Query query = getQuery();
		query.descend("id").orderDescending();
		ObjectSet<Coordonnees> resultat = query.execute();
		return findMax(resultat,limit);
	}
}
