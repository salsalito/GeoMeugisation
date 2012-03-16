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

	public List<Coordonnees> findByLatLong (Coordonnees coord)
	{
		List<Coordonnees> retour = new ArrayList<Coordonnees>();
		Query query = db().query();
		query.constrain(coord.getClass());
		Constraint lonT = query.descend("longitude").constrain(coord.getLongitude());
		query.descend("latitude").constrain(coord.getLatitude()).and(lonT);
		ObjectSet<Coordonnees> resultat = query.execute();
		while(resultat.hasNext()) {
			retour.add(resultat.next());
			}
		return retour;
	}
}
