package com.meuge.geolocalisation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.events.CancellableObjectEventArgs;
import com.db4o.events.CommitEventArgs;
import com.db4o.events.Event4;
import com.db4o.events.EventListener4;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;

public class Db4oHelper {
	private static ObjectContainer oc = null;
    private Context context; 

    /**       
     * @param ctx
     */

    public Db4oHelper(Context ctx) {
              context = ctx;
    }

    /**
    * Creer, ouvrir et fermer la database
    */
    public ObjectContainer db() {

        try {
            if (oc == null || oc.ext().isClosed()) {
              oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPathWithStorage(context));
              registerEventOnContainer(oc);
            }
            return oc;

        } catch (Exception ie) {
            Log.e(Db4oHelper.class.getName(), ie.toString());
            return null;
        }
    }


    /**
    * Configure les contraintes
    */

    private EmbeddedConfiguration dbConfig() throws IOException {
           EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
           configuration.common().objectClass(CoordonneesPOI.class).objectField("latitude").indexed(true);
           configuration.common().objectClass(CoordonneesPOI.class).objectField("longitude").indexed(true);
           //configuration.common().objectClass(CoordonneesPOI.class).objectField("id").indexed(true);
           configuration.common().objectClass(CoordonneesPOI.class).cascadeOnUpdate(true);
           configuration.common().objectClass(CoordonneesPOI.class).cascadeOnActivate(true);
           return configuration;
    }
    //Cree un auto ID incremental
    public void registerEventOnContainer(final ObjectContainer container) {
        // #example: use events to assign the ids
        final AutoIncrement increment = new AutoIncrement(container);
        EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(container);
        eventRegistry.creating().addListener(new EventListener4<CancellableObjectEventArgs>() {
            public void onEvent(Event4<CancellableObjectEventArgs> event4,
                                CancellableObjectEventArgs objectArgs) {
                if(objectArgs.object() instanceof CoordonneesPOI){
                	CoordonneesPOI coordsPoi = (CoordonneesPOI) objectArgs.object();
                	coordsPoi.setId(increment.getNextID(coordsPoi.getClass()));
                }
            }
        });
        eventRegistry.committing().addListener(new EventListener4<CommitEventArgs>() {
            public void onEvent(Event4<CommitEventArgs> commitEventArgsEvent4,
                                CommitEventArgs commitEventArgs) {
                increment.storeState();
            }
        });
        // #end example
    }
       /**
       * Chemin de la base de donnees
       */

       private String db4oDBFullPath(Context ctx) {
    	   	String nomFile = ctx.getString(R.string.database_file_interne);
    	   	return ctx.getDir("databases", 0) + "/" + nomFile;
       }
       /**
        * Chemin de la base de donnees
        */
       
       private String db4oDBFullPathWithStorage(Context ctx) {
       		String retourFile = db4oDBFullPath(ctx);
    	   if (isExternalStorageAvailable())
       		{
    		   	String nomFile = ctx.getString(R.string.database_file_externe);
       			File ext = new File (Environment.getExternalStorageDirectory().getAbsolutePath()+nomFile);
       			retourFile = ext.exists() ? ext.getAbsolutePath() : db4oDBFullPath(ctx);
       		}
    	   return retourFile;
       }
		/*
		 *  Base externe 
		 */
       public boolean isExternalStorageAvailable() {
    	    boolean state = false;
    	    String extStorageState = Environment.getExternalStorageState();
    	    if (Environment.MEDIA_MOUNTED.equals(extStorageState) && 
    	    	!Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
    	        state = true;
    	    }
    	    return state;
    	}
       
       /**
       * Ferme la database
       */

       public void close() {
                     if (oc != null)
                                   oc.close();
                     }

}
