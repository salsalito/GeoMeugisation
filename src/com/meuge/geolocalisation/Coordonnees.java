package com.meuge.geolocalisation;

import java.io.Serializable;

//Classe pour recuperer nos donnees pour rentrer dans la base
public class Coordonnees implements  Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String adresse;
	private double latitude;
	private double longitude;
	private String UUID;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uuid) {
		this.UUID = uuid;
	}
	
}
