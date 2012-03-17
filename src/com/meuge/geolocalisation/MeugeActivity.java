package com.meuge.geolocalisation;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MeugeActivity extends Activity  implements OnClickListener, LocationListener {
	private LocationManager lManager;
    private Location location;
    private String choix_source = "";
    final Handler handler = new Handler();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //On spécifie que l'on va avoir besoin de gérer l'affichage du cercle de chargement
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
 
        setContentView(R.layout.meuge_layout);
 
        //On récupère le service de localisation
        lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
 
        //Initialisation de l'écran
        reinitialisationEcran();
 
        //On affecte un écouteur d'évènement aux boutons
        findViewById(R.id.choix_source).setOnClickListener(this);
        findViewById(R.id.obtenir_adresse).setOnClickListener(this);
        findViewById(R.id.afficherAdresse).setOnClickListener(this);
        findViewById(R.id.refresh).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        findViewById(R.id.world).setOnClickListener(this);
    }
    
    //On cree le menu Quitter
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.icontext : finish(); break; 
    	}
    	return true;
    }
    
        //Méthode déclencher au clique sur un bouton
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choix_source:
			choisirSaSource();
			break;
		case R.id.afficherAdresse:
			afficherAdresse();
			break;
		case R.id.obtenir_adresse:
			afficherLatitude();
			break;
		case R.id.refresh:
			reinitialisationEcran();
			break;
		case R.id.save:
			saveDBCoordonnees();
			break;
		case R.id.world:
			tacheDeFond();
			break;
		default:
			break;
		}
	}
 
	//Réinitialisation de l'écran
	private void reinitialisationEcran(){
		((TextView)findViewById(R.id.latitude)).setText("0.0");
		((TextView)findViewById(R.id.longitude)).setText("0.0");
		((TextView)findViewById(R.id.altitude)).setText("0.0");
		((TextView)findViewById(R.id.adresse)).setText("");
		((TextView)findViewById(R.id.adresse_etat)).setText("");
 
	}
    //Va a la page GPS (independant de nous)
    private void showGpsOptions(){  
        Intent gpsOptionsIntent = new Intent(  
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
        startActivity(gpsOptionsIntent);  
    }  
    //Affiche la toolbox fdu GPS et ensuite realise l'action associee
	private void createGpsDisabledAlert(){  
    AlertDialog.Builder builder = new AlertDialog.Builder(this);  
    builder.setMessage("Votre GPS est eteint! Voulez vous l'allumer ?")  
         .setCancelable(false)  
         .setPositiveButton("GPS OK",  
              new DialogInterface.OnClickListener(){  
              public void onClick(DialogInterface dialog, int id){  
                   showGpsOptions();
              }  
         });  
         builder.setNegativeButton("GPS KO",  
              new DialogInterface.OnClickListener(){  
              public void onClick(DialogInterface dialog, int id){  
                   dialog.cancel();
                   choisirSource();
              }  
         });  
    AlertDialog alert = builder.create(); 
    alert.show(); 
    }
	// J'ai perdu le focus
	@Override
	public void onStop()
	{
	    super.onStop();
	    saveCoordonnees();
	}

	/** Called when the activity looses focus **/
	@Override
	public void onPause()
	{
		super.onPause();
		saveCoordonnees();
	}
	
	//Sauvegarde 
	private void saveCoordonnees() {
		final Bundle bundle = new Bundle();
		Intent myIntent = getParent().getIntent();
		double []arrayInfos = new double[2];
		arrayInfos[0] = getLatitude();
		arrayInfos[1] = getLongitude();
		bundle.putDoubleArray("GPSINFO", arrayInfos);
		bundle.putString("ADRESSEINFO", getAdresse());
		myIntent.putExtras(bundle);
		
		setIntent(myIntent);
	}
	//Fais un bean
	private Coordonnees getCoords()
	{
		Coordonnees retour = new Coordonnees();
		retour.setAdresse(getAdresse());
		retour.setLatitude(getLatitude());
		retour.setLongitude(getLongitude());
		retour.setUUID(getUUID());
		retour.setPositions(CalculLatLong.calculate(getLatitude(), getLongitude()));
		
		return retour;
	}
	//Sauvegarde en base
	private void saveDBCoordonnees()
	{
		if (!(getLatitude()==(double) 0L && getLongitude()==(double) 0L))
		{
			CoordonneesProvider cp = new CoordonneesProvider(Coordonnees.class, this);
			//Coordonnee dans la base
			List<Coordonnees> coordonnee = cp.findByLatLong(getCoords());
			if(coordonnee.size() == 0)
			{
		        cp.store(getCoords());
		        cp.db().commit();
			}
	        cp.close();
	        cp.db().close();
		}
	}
	//Affiche les sources possibles
	private void choisirSource() {
		//On demande au service la liste des sources disponibles.
		List <String> providers = lManager.getProviders(true);
		final String[] sources = new String[providers.size()];
		int i =0;
		//on stock le nom de ces source dans un tableau de string
		for(String provider : providers)
			sources[i++] = provider;
 
		//On affiche la liste des sources dans une fenêtre de dialog
		//Pour plus d'infos sur AlertDialog, vous pouvez suivre le guide
		//http://developer.android.com/guide/topics/ui/dialogs.html
		new AlertDialog.Builder(MeugeActivity.this)
		.setItems(sources, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//on stock le choix de la source choisi
				choix_source = sources[which];
				//on ajoute dans la barre de titre de l'application le nom de la source utilisé
				setTitle(String.format("%s - %s", getString(R.string.app_name),
						choix_source));
				obtenirPosition();
			}
		})
		.create().show();
	}

	/**
	 * 
	 */
	private void choisirSaSource() {
		reinitialisationEcran();
	    if (!lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
	          createGpsDisabledAlert();  
	    }
	    else choisirSource();
	}
 
	private void obtenirPosition() {
		//on démarre le cercle de chargement
		setProgressBarIndeterminateVisibility(true);
 
		//On demande au service de localisation de nous notifier tout changement de position
		//sur la source (le provider) choisie, toute les minutes (60000millisecondes).
		//Le paramètre this spécifie que notre classe implémente LocationListener et recevra
		//les notifications.
		lManager.requestLocationUpdates(choix_source, 60000, 0, this);
	}
 

	
	private void afficherLocation() {
		//On affiche les informations de la position a l'écran
		((TextView)findViewById(R.id.latitude)).setText(String.valueOf(location.getLatitude()));
		((TextView)findViewById(R.id.longitude)).setText(String.valueOf(location.getLongitude()));
		((TextView)findViewById(R.id.altitude)).setText(String.valueOf(location.getAltitude()));
	}
 
	private void afficherAdresse() {
		setProgressBarIndeterminateVisibility(true);
 
		geoCoderInformation();
		//on stop le cercle de chargement
		setProgressBarIndeterminateVisibility(false);
	}
	private void afficherLatitude() {
		setProgressBarIndeterminateVisibility(true);
		
		geoCoderAdresseInformation();
		//on stop le cercle de chargement
		setProgressBarIndeterminateVisibility(false);
	}
	/**
	 *  Obtenir les coordonnees avec Adresse
	 */
	private void geoCoderAdresseInformation() {
		//Le geocoder permet de récupérer ou chercher des adresses
		//gràce à un mot clé ou une position
		Geocoder geo = new Geocoder(MeugeActivity.this);
		try {
			//Ici on récupère la premiere adresse trouvé gràce à la position que l'on a récupéré
			String monAdresse = getAdresse();
			List
			<Address> adresses = geo.getFromLocationName(monAdresse, 1);
			if(adresses != null && adresses.size() == 1){
				Address adresse = adresses.get(0);
				
				//Si le geocoder a trouver une adresse, alors on l'affiche
				
				((TextView)findViewById(R.id.adresse_etat)).setText(String.format("Latitude : %s - Longitude :%s",
						adresse.getLatitude(),
						adresse.getLongitude()));
			}
			else {
				//sinon on affiche un message d'erreur
				((TextView)findViewById(R.id.adresse_etat)).setText("L'adresse n'a pu être déterminée");
			}
		} catch (IOException e) {
			e.printStackTrace();
			((TextView)findViewById(R.id.adresse_etat)).setText("L'adresse n'a pu être déterminée");
		}
	}
 	
	/**
	 *  Obtenir les coordonnees avec latitude et longitude
	 */
	private void geoCoderInformation() {
		//Le geocoder permet de récupérer ou chercher des adresses
		//gràce à un mot clé ou une position
		double latitude = 0L;
		double longitude = 0L;
		Geocoder geo = new Geocoder(MeugeActivity.this);
		try {
			//Ici on récupère la premiere adresse trouvé gràce à la position que l'on a récupéré
		
			latitude  = getLatitude() ;
			longitude = getLongitude() ;
			List
<Address> adresses = geo.getFromLocation(latitude,
					longitude,1);
 
			if(adresses != null && adresses.size() == 1){
				Address adresse = adresses.get(0);
				//Si le geocoder a trouver une adresse, alors on l'affiche
				((EditText)findViewById(R.id.adresse)).setText(String.format("%s %s %s ,%s",
						adresse.getAddressLine(0),
						adresse.getPostalCode(),
						adresse.getLocality(),
						adresse.getCountryName()));
			}
			else {
				//sinon on affiche un message d'erreur
				((TextView)findViewById(R.id.adresse_etat)).setText("L'adresse n'a pu être déterminée");
			}
		} catch (IOException e) {
			e.printStackTrace();
			((TextView)findViewById(R.id.adresse_etat)).setText("L'adresse n'a pu être déterminée");
		} catch (NumberFormatException e) {
			((TextView)findViewById(R.id.adresse_etat)).setText("Coordonnées entrées invalides");
		}
	}

	/**
	 * Obtenir la longitude
	 */
	private Double getLongitude() {
		Double retour = null;
		try {
		return Double.valueOf(((TextView)findViewById(R.id.longitude)).getText().toString());
		} catch (NumberFormatException e) {
			retour = (double) 0L;
		}
		return retour;
	}

	/**
	 * Obtenir la latitude
	 */
	private Double getLatitude() {
		Double retour = null;
		try {
			retour = Double.valueOf(((TextView)findViewById(R.id.latitude)).getText().toString());
		} catch (NumberFormatException e) {
			retour = (double) 0L;
		}
		return retour;
	}
    /**
     * Obtenir l'adresse
     */
	private String getAdresse() {
		return ((EditText)findViewById(R.id.adresse)).getText().toString();
	}
	
	/**
     * Obtenir L'UUID
     */
	private String getUUID() {
		TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}
	
	
	public void onLocationChanged(Location location) {
		//Lorsque la position change...
		Log.i("Tuto géolocalisation", "La position a changé.");
		//... on stop le cercle de chargement
		setProgressBarIndeterminateVisibility(false);
		//... on active le bouton pour afficher l'adresse
		findViewById(R.id.afficherAdresse).setEnabled(true);
		//... on sauvegarde la position
		this.location = location;
		//... on l'affiche
		afficherLocation();
		//... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
		lManager.removeUpdates(this);
	}
 
	public void onProviderDisabled(String provider) {
		//Lorsque la source (GSP ou réseau GSM) est désactivé
		Log.i("Tuto géolocalisation", "La source a été désactivé");
		//...on affiche un Toast pour le signaler à l'utilisateur
		Toast.makeText(MeugeActivity.this,
				String.format("La source \"%s\" a été désactivé", provider),
				Toast.LENGTH_SHORT).show();
		//... et on spécifie au service que l'on ne souhaite plus avoir de mise à jour
		lManager.removeUpdates(this);
		//... on stop le cercle de chargement
		setProgressBarIndeterminateVisibility(false);
	}
	//Demarrage en tache de fond
	public void tacheDeFond() {
		ToggleButton tgb = (ToggleButton) findViewById(R.id.world);
		if (tgb.isChecked() && choix_source.length()==0)
		{
			Toast.makeText(this, "Choississez au moins une source !! ", Toast.LENGTH_SHORT).show();
			tgb.setChecked(false);
		}
		else
			startThread();
	    
	}
	/**
	 * 
	 */
	private void startThread() {
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
		@Override
		public void run() {
			ToggleButton tgb = (ToggleButton) findViewById(R.id.world);
			if (!tgb.isChecked())
			{
				timer.cancel();
			}
			else
				handler.post(checkCoords());
		}
		}, 10000, 10000); //delay, //periode
	}

	/**
	 * @return
	 */
	private Runnable checkCoords() {
		return new Runnable() {
			  @Override
			  public void run() {
				  	obtenirPosition();
				  	geoCoderInformation();
			    	handler.removeCallbacks(this);
			  }
			  
			};
	}
	
	public void onProviderEnabled(String provider) {
		Log.i("Géolocalisation", "La source a été activé.");
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("Géolocalisation", "Le statut de la source a changé.");
	}
 }