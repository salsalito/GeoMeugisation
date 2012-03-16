package com.meuge.geolocalisation;


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
    //Trouve la derni�re coordonn�e selon la latitude et la longitude
	public Coordonnees findByLatLong (Coordonnees coord)
	{
		Query query = db().query();
		query.constrain(coord.getClass());
		Constraint lonT = query.descend("longitude").constrain(coord.getLongitude());
		query.descend("latitude").constrain(coord.getLatitude()).and(lonT);
		ObjectSet<Coordonnees> resultat = query.execute();
		Coordonnees coordonnee = null;
		while(resultat.hasNext()) {
			coordonnee = resultat.next();
			}
		return coordonnee;
	}
}
