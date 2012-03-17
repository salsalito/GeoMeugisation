package com.meuge.geolocalisation;

public class CalculLatLong {
	private  double COSLAT;
	private double SINLAT;
	private double COSLNG;
	private double SINLNG;
	
	public static  CalculLatLong calculate(double lat, double lon)
	{
		CalculLatLong retour = new CalculLatLong();
		retour.SINLAT = Math.sin(deg2rad(lat));
		retour.COSLAT = Math.cos(deg2rad(lat));
		retour.COSLNG = Math.cos(deg2rad(lon));
		retour.SINLNG = Math.sin(deg2rad(lon));
		return retour;
	}
	
	private static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}

	public  double getCOSLAT() {
		return COSLAT;
	}

	public  double getSINLAT() {
		return SINLAT;
	}

	public  double getCOSLNG() {
		return COSLNG;
	}

	public  double getSINLNG() {
		return SINLNG;
	}
	
}
