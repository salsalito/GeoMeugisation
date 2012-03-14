package com.meuge.geolocalisation;

import java.io.Serializable;
import java.util.List;

import android.content.Context;


public class DbProvider<T extends Serializable> extends Db4oHelper {
	public Class<T> persistentClass;

    public DbProvider( Class<T> persistentClass, Context ctx ) {
        super( ctx );
        this.persistentClass = persistentClass;
    }

    public void store( T obj ) {
         db().store( obj );
    }

    public void delete( T obj ) {
         db().delete( obj );
    }

    public List<T> findAll() {
        return db().query( persistentClass );
    }
}
