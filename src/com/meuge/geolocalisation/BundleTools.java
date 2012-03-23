package com.meuge.geolocalisation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.Appender;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.appender.LogCatAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.google.code.microlog4android.format.PatternFormatter;

public final class BundleTools {
		public  static  Logger logger = LoggerFactory.getLogger();
	    private static String LATITUDEINFO = "LATITUDEINFO";
	    private static String LONGITUDEINFO = "LONGITUDEINFO";
	    private static String ADRESSEINFO = "ADRESSEINFO";
	    private static String GPS_ON_OFF ="GPSINFO";
	    public static String DATABASE_CHARGE ="DATABASE_LOADED";
	    public static String NB_ELEMENTS ="NB_ELEMENTS";
	    
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
		
		public static  boolean isDBaseLoaded(SharedPreferences sharedPref)
		{
			Bundle bundle = getInstance();
			bundle.putBoolean(DATABASE_CHARGE,sharedPref.getBoolean(DATABASE_CHARGE, false));
			return bundle.getBoolean(DATABASE_CHARGE);
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
	    /**
	     * Configure microlog
	     */
	    public static void micrologMainConfigurator(Context ctx) {
	    	
	    		
	            PropertyConfigurator.getConfigurator(ctx).configure().;
	            FileAppender appender = new FileAppender();
	            LogCatAppender logCat = new LogCatAppender();
	            PatternFormatter patternFormatter=new PatternFormatter(); 
	            patternFormatter.setPattern("%d{DATE} [%P] %m"); 
	            appender.setFileName( "meuge.txt");
	            logCat.setFormatter(patternFormatter);
	            appender.setFormatter(patternFormatter); 
	            logger.addAppender(appender);	            
	            logger.addAppender(logCat);	            
	            
	            logger.debug("Michou Bidou");
	            logger.debug("Michou Bidou2");
	            logger.debug("Michou Bidou3");
	    }
     
		public static void copyDataBase(Context ctx, SharedPreferences sharedPreferences)
		{   
			
			try {
					InputStream inStream = ctx.getResources().openRawResource(R.raw.poimeuge);
		            byte[] buffer = new byte[inStream.available()];               
		            inStream.read(buffer);               
		            inStream.close();  
		            FileOutputStream fos = new FileOutputStream(ctx.getDir("databases", 0) + "/" + ctx.getString(R.string.database_file_interne),false);     
		            fos.write(buffer);               
		            fos.close(); 
	                SharedPreferences.Editor editor = sharedPreferences.edit();
	        		editor.putBoolean(BundleTools.DATABASE_CHARGE, true);
	                editor.commit();
			} catch (FileNotFoundException e) {
				Log.e("DATABASE", "Fichier non trouve");
			} catch (IOException e) {
				Log.e("DATABASE", "Fichier bizarre "+ctx.getString(R.string.database_file_interne)+" non trouve");
			}               
         }
}
