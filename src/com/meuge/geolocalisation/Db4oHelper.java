package com.meuge.geolocalisation;

import java.io.IOException;

import android.content.Context;
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
              oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));
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
           configuration.common().objectClass(Coordonnees.class).objectField("id").indexed(true);
           configuration.common().objectClass(Coordonnees.class).cascadeOnUpdate(true);
           configuration.common().objectClass(Coordonnees.class).cascadeOnActivate(true);
           configuration.common().objectClass(CoordonneesPOI.class).objectField("id").indexed(true);
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
                if(objectArgs.object() instanceof Coordonnees){
                	Coordonnees coords = (Coordonnees) objectArgs.object();
                	coords.setId(increment.getNextID(coords.getClass()));
                }
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
                     return ctx.getDir("databases", 0) + "/" + "dbmeuge.db4o";
       }

       /**
       * Ferme la database
       */

       public void close() {
                     if (oc != null)
                                   oc.close();
                     }

}
