package com.meuge.geolocalisation;

public class KmsCalcules implements Comparable {
	private double nbKms;
	private String informations;
	private String categorie;
	
	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public final double getNbKms() {
		return nbKms;
	}
	
	public KmsCalcules(double nbKms, String categorie, String informations) {
		super();
		this.nbKms = nbKms;
		this.categorie = categorie;
		this.informations = informations;
	}
	
	public final void setNbKms(double nbKms) {
		this.nbKms = nbKms;
	}
	
	public final String getInformations() {
		return informations;
	}
	
	public final void setInformations(String informations) {
		this.informations = informations;
	}
	@Override
	public int compareTo(Object another) {
		 if(!(another instanceof KmsCalcules))
		      throw new ClassCastException();
		    KmsCalcules v = (KmsCalcules) another;
		    return Double.compare(getNbKms(),v.getNbKms());

	}
	
}
