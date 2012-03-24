package com.meuge.geolocalisation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.appender.FileAppender;
import com.google.code.microlog4android.appender.LogCatAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.google.code.microlog4android.format.PatternFormatter;

public class LogPersos extends LoggerFactory {
	private static Context ctx ;
	private static LogCatAppender getLogCatAppender ;
	private static FileAppender getFileAppender ;
	private static Logger logger ;

	public static Logger getLoggerPerso()
	{
        Logger retour = getLogger();
        if (Boolean.getBoolean(ctx.getString(R.string.log_enable)))
        	retour.addAppender(getLogCatAppender);	            
        retour.addAppender(getFileAppender);	            
        return retour;
	}

	private static LogCatAppender initLogCatAppender()
	{
		LogCatAppender logCat = new LogCatAppender();
		logCat.setFormatter(getPatternPerso());
		return logCat;
	}
	
	private static FileAppender initFileAppender()
	{
		FileAppender appender = new FileAppender();
		appender.setFileName(ctx.getString(R.string.log_file_externe));
		appender.setAppend(true);
		appender.setFormatter(getPatternPerso()); 
		return appender;
	}

	private static PatternFormatter getPatternPerso() {
		PatternFormatter patternFormatter=new PatternFormatter(); 
        patternFormatter.setPattern("%d{DATE} [%P] %m");
		return patternFormatter;
	}
	
	public static void initLog(Context ct, SharedPreferences shaP)
	{
		if (ctx == null)
		{
			ctx = ct;
			PropertyConfigurator.getConfigurator(ctx).configure();
			getLogCatAppender = initLogCatAppender();
			getFileAppender = initFileAppender();
			logger = getLoggerPerso();
			chargeBase(shaP);
		}
		
	}
    private static void chargeBase(SharedPreferences sharedPreferences)
    {
    	if (!BundleTools.isDBaseLoaded(sharedPreferences))
    	{
    		copyDataBase(sharedPreferences);
    		if (!BundleTools.isDBaseLoaded(sharedPreferences))
    		{
	    		//...on affiche un Toast pour le signaler à l'utilisateur
	    		Toast.makeText(ctx,"Chargement de base Magasins",Toast.LENGTH_SHORT).show();
	    		logger.debug("Chargement de base Magasins debutee");
	    		LectureFichierPOI.LectureFichier(ctx.getResources().openRawResource(R.raw.magasins),"Magasins.asc", ctx,sharedPreferences);
	    		logger.debug("Fin de chargement de base Magasins");
	    		Toast.makeText(ctx,"Chargement de base Magasins finie",Toast.LENGTH_SHORT).show();
	    		//Magasins_But.Magasins_But(contextCreated);
    		}
    	}

    }

    
	public static void copyDataBase(SharedPreferences sharedPreferences)
	{   
		
		try {
   				logger.debug("Copie de base Magasins debutee");
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
                logger.debug("Fin de copie de base Magasins debutee");
		} catch (FileNotFoundException e) {
			logger.error("Fichier base non trouve");
		} catch (IOException e) {
			logger.error(String.format("Fichier bizarre : %s non trouve",ctx.getString(R.string.database_file_interne)));
		}               
     }

}
