package com.meuge.geolocalisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public final class BundleTools {
	     
	    private static String LATITUDEINFO = "LATITUDEINFO";
	    private static String LONGITUDEINFO = "LONGITUDEINFO";
	    private static String ADRESSEINFO = "ADRESSEINFO";
	    private static String GPS_ON_OFF ="GPSINFO";
	    
		private static final class BundleToolsHolder {
			static final Bundle singleton =  new Bundle();
		}

		public static Bundle getInstance() {
		return BundleToolsHolder.singleton;
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
		
}
