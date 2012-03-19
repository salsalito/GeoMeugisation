package com.meuge.geolocalisation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class LectureFichierPOI {
	
	private static boolean testNumber (String s){
		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()){
			return true; 
		} 
		return false; 
	}
	public static void LectureFichier (String fichier, Context ctx)
	{
	    try{
	    	 String separator = ",";
	    	  // Ouverture fichier 
	    	  FileInputStream fstream = new FileInputStream(fichier);
	    	  // Retourne l'objet du DataInputStream
	    	  DataInputStream in = new DataInputStream(fstream);
	    	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	  String strLine = br.readLine();
	    	  int compteur = 0;
	    	  //File Ligne Par Ligne
	    	  while (strLine != null)   {
	    		  compteur++;
	    		  strLine = strLine.replaceAll("\"\"", "\"");
	    		  if (strLine.trim().length() >0)
	    		  {
	    			  StringTokenizer temp = new StringTokenizer(strLine,separator);
		              if (temp.countTokens()==3)
		              {
		            	  String latitude = ((String) temp.nextElement()).trim();
		            	  String longitude = ((String) temp.nextElement()).trim();
		            	  String infos = ((String) temp.nextElement()).trim();
		            	  String  magasin = "";
		            	  if (infos.startsWith("\"") && infos.endsWith("\""))
		            		  infos = infos.substring(1, infos.length()-1);
		            	  if (infos.startsWith("[") && (infos.indexOf("]") < infos.length()))
		            	  {
		            		  magasin = infos.substring(1, infos.indexOf("]"));
		            		  infos = infos.substring(infos.indexOf("]")+1).trim();
		            	  }
		            	  if (testNumber(latitude)  && testNumber(longitude))
		            		  System.out.println (compteur+" : Magasin : "+magasin+" | Lat : " +latitude + "| Lon: " +longitude + "| Adresse : "+infos);
		            	  else 
		            		  System.out.println (compteur+ " : Erreur ligne :" + strLine);
		              }
	    		  }
	              strLine = br.readLine();
	    	  }
	    	  //Fermeture
	    	  in.close();
	    	}catch (Exception e){//Catch exception if any
	    	   	System.err.println("Error: " + e.getMessage());
	    	}
	}
}
