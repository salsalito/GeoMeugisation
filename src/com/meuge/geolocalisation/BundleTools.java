package com.meuge.geolocalisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public final class BundleTools {
	     
	    private static String LATITUDEINFO = "LATITUDEINFO";
	    private static String LONGITUDEINFO = "LONGITUDEINFO";
	    private static String ADRESSEINFO = "ADRESSEINFO";
	    private static String GPS_ON_OFF ="GPSINFO";
	    private static String DATABASE_CHARGE ="DATABASE_LOADED";

	    
		private static final class BundleToolsHolder {
			static final Bundle singleton =  new Bundle();
			static final Hashtable<String, ArrayList<CoordonneesPOI>> coordsFind =  new Hashtable<String, ArrayList<CoordonneesPOI>>();
		}

		public static Bundle getInstance() {
			return BundleToolsHolder.singleton;
		}
		
		private static Hashtable<String, ArrayList<CoordonneesPOI>> getCoords() {
			return BundleToolsHolder.coordsFind;
		}
		
		public static  boolean isDBaseLoaded()
		{
			Bundle bundle = getInstance();
			return bundle.getBoolean(DATABASE_CHARGE);
		}
		
		public static void loadedDB()
		{
			Bundle bundle = getInstance();
			bundle.putBoolean(DATABASE_CHARGE, true);
		}
		public static void storeGPSStatus(boolean valeur)
		{
			Bundle bundle = getInstance();
			bundle.putBoolean(GPS_ON_OFF, valeur);
		}
		
		public static boolean GPSStatus()
		{
			Bundle bundle = getInstance();
			return bundle.getBoolean(GPS_ON_OFF);
		}
		
		public static void storeLatitude(double variableSave)
		{
			Bundle bundle = getInstance();
			bundle.putDouble(LATITUDEINFO, variableSave);				
		}
		
		public static double getLatitude()
		{
			Bundle bundle = getInstance();
			return bundle.getDouble(LATITUDEINFO,bundle.getDouble(LATITUDEINFO,(double) 0.0));
		}

		public static void storeLongitude(double variableSave)
		{
			Bundle bundle = getInstance();
			bundle.putDouble(LONGITUDEINFO, variableSave);
		}	
		
		public static double getLongitude()
		{
			Bundle bundle = getInstance();
			return bundle.getDouble(LONGITUDEINFO,bundle.getDouble(LONGITUDEINFO,(double) 0.0));
		}

		public static void storeAdresse(String variableSave)
		{
			Bundle bundle = getInstance();
			bundle.putString(ADRESSEINFO, variableSave);
		}	
		
		public static String getAdresse()
		{
			Bundle bundle = getInstance();
			return bundle.getString(ADRESSEINFO);
		}
		
		public static void commitExtras(Intent intent) {
			intent.putExtras(BundleTools.getInstance());
		}
		
		public static void reset() {
			resetCoords();
			Bundle bundle = getInstance();
			bundle.clear();
		}
		
		public static void storeCoords(ArrayList<CoordonneesPOI> coords)
		{
			getCoords().clear();
			for (int i=0; i < coords.size(); i++)
			{
				CoordonneesPOI tmp = coords.get(i);
				ArrayList<CoordonneesPOI> tableau = BundleToolsHolder.coordsFind.get(tmp.getCategorie());
				tableau = tableau == null ? new ArrayList<CoordonneesPOI>() : tableau; 
				tableau.add(tmp);
				getCoords().put(tmp.getCategorie(), tableau);
			}
		}
		
		public static void resetCoords() {
			getCoords().clear();
		}
}
