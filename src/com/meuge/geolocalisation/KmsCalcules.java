package com.meuge.geolocalisation;

public class KmsCalcules implements Comparable {
	private double nbKms;
	private String informations;
	
	public final double getNbKms() {
		return nbKms;
	}
	
	public KmsCalcules(double nbKms, String informations) {
		super();
		this.nbKms = nbKms;
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
