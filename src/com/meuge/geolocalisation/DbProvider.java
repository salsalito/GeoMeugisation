package com.meuge.geolocalisation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;


public class DbProvider<T extends Serializable> extends Db4oHelper {
	public Class<T> persistentClass;
	public static int ALLRECORDS = -1;

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

    public List<T> findAllMax(int limit) {
    	List<T> listToPage = db().query( persistentClass );
    	listToPage =  (limit ==ALLRECORDS) ?  listToPage : Paging(listToPage, 0, limit);
    	return listToPage;
    }

    public List<T> findMax(List<T> objs, int limit)
    {
    	return Paging(objs, 0, limit);
    }
    
    private  List<T> Paging(List<T> listToPage, int start, int limit)
    {
    	List<T> list = new ArrayList<T>();
    	limit = limit < 0 ? listToPage.size() : limit;
        if (start < listToPage.size())
        {
	        int end = calculFin(listToPage, start, limit);
	        for (int i = start; i < end; i++)
	        {
	            list.add(listToPage.get(i));
	        }
        }
        return list;
    }
    
    
    private int calculFin(List<T> resultList, int start, int limit)
    {
        int end = start + limit;
        if (end >= resultList.size())
        {
            return resultList.size();
        }
        return end;
    }
}
