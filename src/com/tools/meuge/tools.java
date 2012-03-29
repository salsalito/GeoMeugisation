package com.tools.meuge;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.h2.jdbcx.JdbcDataSource;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class tools {

	private static Connection conn = createConnection();
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		runStatement("drop table if exists CoordonneesPOI");
		runStatement("create table CoordonneesPOI ("
                + "ID INTEGER auto_increment PRIMARY KEY, "
                + "CATEGORIE VARCHAR(25) NOT NULL, "
                + "LATITUDE DOUBLE NOT NULL, "
                + "LONGITUDE DOUBLE NOT NULL, "
                + "COSLAT DOUBLE, "
                + "SINLAT DOUBLE, "
                + "COSLNG DOUBLE, "
                + "SINLNG DOUBLE, "
                + "TYPE VARCHAR(20) NOT NULL, "
                + "ADRESSE VARCHAR(80))");
		Insertfile("D:\\h2\\magasins.asc", "Grands Magasins");
		conn.close();
		//callWebService();
	}
	/**
	 * 
	 */
	private static void callWebService() {
		String NAMESPACE = "http://webservices.meuge.com/" ;
    	String METHOD_NAME = "getPrice";
    	String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    	String URL = "http://localhost:8080/WebServiceProject/services/StockQuoteService";

    		
    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope.dotNet =  false;
	    envelope.setOutputSoapObject(request);
	    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

	    try
	    {

	    androidHttpTransport.call(SOAP_ACTION, envelope);

	    SoapPrimitive Result = (SoapPrimitive)envelope.getResponse();

	   String hresult = Result.toString();
	   hresult.toCharArray();
	    }
	    catch(Exception e) 
	    {
	    	e.getClass();
	    }
	}
 
	private static boolean testNumber (String s){
		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()){
			return true; 
		} 
		return false; 
	}
 //Obtient une connection valide
	public static Connection createConnection() {
		Connection cnx = null;
		JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:tcp://localhost/test");
        ds.setUser("sa");
        ds.setPassword("");
        try {
        	cnx =  ds.getConnection();
        } catch (Exception e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
        return cnx;
    }
	//Execute une commande
    public static void prepareStatement(ArrayList<String> montableau) {
        try {
        	String sqlStr = " insert into CoordonneesPOI ("
                    + "CATEGORIE, "
                    + "LATITUDE, "
                    + "LONGITUDE, "
                    + "COSLAT, "
                    + "SINLAT, "
                    + "COSLNG, "
                    + "SINLNG, "
                    + "ADRESSE, "
                    + "TYPE) " +
                    "VALUES (?,?,?,?,?,?,?,?,?);";
			PreparedStatement stmt =  conn.prepareStatement(sqlStr);
			 stmt.setString(1, montableau.get(0));
			 stmt.setDouble(2, Double.valueOf(montableau.get(1)));
			 stmt.setDouble(3, Double.valueOf(montableau.get(2)));
			 stmt.setDouble(4, Double.valueOf(montableau.get(3)));
			 stmt.setDouble(5, Double.valueOf(montableau.get(4)));
			 stmt.setDouble(6, Double.valueOf(montableau.get(5)));
			 stmt.setDouble(7, Double.valueOf(montableau.get(6)));
			 stmt.setString(8, montableau.get(7));
			 stmt.setString(9, montableau.get(8));
			 stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
        
    }	
    //Execute une commande
    public static void runStatement(String sqlstmt) {
    	System.out.println(sqlstmt);
    	try {
    		Statement stmt = conn.createStatement();
    		stmt.executeUpdate(sqlstmt);
    		stmt.close();
    		
    	} catch (SQLException ex) {
    		System.err.println("SQLException: " + ex.getMessage());
    	}
    }	
    // inserre fichier
	public static void Insertfile(String nomFichier, String Categorie) {
		int compteur = 0;
        try {
        	 String separator = ",";
 			 BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(nomFichier)),"ISO-8859-1"));
        	 String strLine = br.readLine();
	    	  //File Ligne Par Ligne
	    	  while (strLine != null)   {
	    		  compteur++;
	    		  strLine = strLine.replaceAll("\"\"", "\"");
	    		  if (strLine.trim().length() >0)
	    		  {
	    			  StringTokenizer temp = new StringTokenizer(strLine,separator);
		              if (temp.countTokens()==3)
		              {
		            	  String longitude = ((String) temp.nextElement()).trim();
		            	  String latitude = ((String) temp.nextElement()).trim();
		            	  String infos = ((String) temp.nextElement()).trim();
		            	  String  magasin = "";
		            	  if (infos.startsWith("\"") && infos.endsWith("\""))
		            		  infos = infos.substring(1, infos.length()-1);
		            	  if (infos.startsWith("[") && (infos.indexOf("]") < infos.length()))
		            	  {
		            		  magasin = infos.substring(1, infos.indexOf("]"));
		            		  infos = infos.substring(infos.indexOf("]")+1).trim();
		            	  }
		            	  if (testNumber(latitude)  
		            			  && testNumber(longitude))
		            	  {
		            		  ArrayList<String> tmpLigne = new ArrayList<String>();
		            		  tmpLigne.add(0,magasin);
		            		  tmpLigne.add(1,latitude);
		            		  tmpLigne.add(2,longitude);
		            		  tmpLigne.add(3,String.valueOf(Math.cos(Double.valueOf(latitude) * Math.PI / 180.0)));
		            		  tmpLigne.add(4,String.valueOf(Math.sin(Double.valueOf(latitude) * Math.PI / 180.0)));
		            		  tmpLigne.add(5,String.valueOf(Math.cos(Double.valueOf(longitude) * Math.PI / 180.0)));
		            		  tmpLigne.add(6,String.valueOf(Math.sin(Double.valueOf(longitude) * Math.PI / 180.0)));
		            		  tmpLigne.add(7,infos);
		            		  tmpLigne.add(8,Categorie);
		            		  prepareStatement(tmpLigne);
		            	  }

		              }
	    		  }
	    		  strLine = br.readLine();
	    	  }
	    	  br.close();
        } catch (Exception e) {
            System.err.println(compteur + ": Caught IOException: " + e.getMessage());
        }
    }
}
