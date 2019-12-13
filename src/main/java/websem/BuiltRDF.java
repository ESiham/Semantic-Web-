//cette classe est pour creer le Model RDF de notre projet
package websem;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.jena.graph.impl.AdhocDatatype;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.jsonldjava.core.RDFDataset.BlankNode;
 
/**
 * @author Crunchify.com
 */
 
public class BuiltRDF {
	static String FUESKI_LOCAL_ENDPOINT  = "http://localhost:3030/bicycleSharing";
 
        @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
      //  ces premier lignes du code sont pour but d'eviter les problemes du certificat ssl 
    	//Create a trust manager that does not validate certificate chains
    	TrustManager[] trustAllCerts = new TrustManager[] { 
    	 new X509TrustManager() {     
    	     public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
    	         return new X509Certificate[0];
    	     } 
    	     public void checkClientTrusted( 
    	         java.security.cert.X509Certificate[] certs, String authType) {
    	         } 
    	     public void checkServerTrusted( 
    	         java.security.cert.X509Certificate[] certs, String authType) {
    	     }
    	 } };
    	//Install the all-trusting trust manager
    	try {
    	 SSLContext sc = SSLContext.getInstance("SSL"); 
    	 sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
    	 HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    	} catch (GeneralSecurityException e) {
    	}
    	
 // la liste des villes utilisees dans le model
   ArrayList<String> list=new ArrayList<>();
  list.add("lyon");
  list.add("toulouse");
  list.add("mulhouse");
  list.add("marseille");
  list.add("nantes");
  list.add("rouen");
  Model model = ModelFactory.createDefaultModel(); //on va utiliser un seul model pour tout les villes
  for(int i = 0 ; i < list.size(); i++) {
	  
        try {	
        	// créer un objet JSONArray pour accéder aux differents elements du fichier JSON des stations
			JSONArray jsonObjects= readJsonFromUrl("https://api.jcdecaux.com/vls/v1/stations?contract="+list.get(i)+"&apiKey=345d6d791a9f6a8152fcf679657486e851cb0a95");
	    
 for (Object obj: jsonObjects) {
	  JSONObject jsonObjec = (JSONObject) obj;
               String name = (String) jsonObjec.get("name");
               String address = (String) jsonObjec.get("address");
               Integer available_bike_stands = (Integer) jsonObjec.get("available_bike_stands");
               Integer available_bikes = (Integer) jsonObjec.get("available_bikes");
               String status = (String) jsonObjec.get("status");
               JSONObject position =(JSONObject) jsonObjec.get("position");
               Long updat =(Long) jsonObjec.get("last_update");
               Integer number =(Integer) jsonObjec.get("number");
               String Inville =(String) jsonObjec.get("contract_name");
               Double lat = (Double) position.get("lat");
   	           Double lng = (Double) position.get("lng"); 
   	           Integer stands=(Integer) jsonObjec.get("bike_stands");
   // commencer d'écrire les URL des triplets
               String ville ="http://ville.org"; // definir une ville
               String indice ="http://"+list.get(i)+".org/";
               String station="http://station_"+number+"_"+Inville+".org/";
               String  ns="http://www.w3.org/2000/01/rdf-schema/";
               String STATION ="http://station.org/"; // definir une station
               String contains = "contains";
               String gps = "GPS";	     
               String stationName = "Has_Name";
      		   String stand_AV="Has_stand_AV";
      		   String bike_AV ="Has_bike_AV";
      		   String has_lat= "Has_lat";
      		   String has_lng="Has_lng";
      		   String has_status="Has_status";
      		   String las_updat="Has_last_update";
      		   String IN="IN";
      		   String has_adress="Has_adress";
      		   String has_stands="Has_stands";
      		  // acceder au fichier de temperature
   			    JSONObject jsonWeather= readJsonFromUrl1("https://samples.openweathermap.org/data/2.5/weather?q="+list.get(i)+"&appid=b6907d289e10d714a6e88b30761fae22");
   				JSONArray weather =(JSONArray) jsonWeather.get("weather");
   			   for (Object obje: weather) {
   			    JSONObject jsonObject = (JSONObject) obje;
   				String description= (String)jsonObject.get("description");
   				String Weather="http://weather.org";//definir un meteo
   				String indix="http://weather_"+ list.get(i)+"_.org";
   				String has_weather= "Has_weather";
   				Property Has_weather = model.createProperty(ns,has_weather);
   					
   			
     //définir les proprietes du Model
      		   Property Contain=model.createProperty(ns,contains);
      		   Property GPS=model.createProperty(ns,gps);
      		   Property HasName = model.createProperty(ns, stationName);
     		   Property has_stand_avlb = model.createProperty(ns,stand_AV);
     		   Property has_bikes_avlb = model.createProperty(ns,bike_AV);
     		   Property Has_lat =model.createProperty(ns,has_lat);
     		   Property Has_lng = model.createProperty(ns,has_lng);
     		   Property Has_status = model.createProperty(ns,has_status);
     		   Property Has_updated = model.createProperty(ns,las_updat);
     		   Property IN_city= model.createProperty(ns,IN);
     		   Property Has_adress = model.createProperty(ns,has_adress);
     		   Property Has_stands= model.createProperty(ns,has_stands);
   // définir les ressources du model et ajouter pour chaque ressource ces proprietes
              Resource city = model.createResource(indice).addProperty(RDF.type, ville);	
              Resource Station = model.createResource(station).addProperty(RDF.type, STATION)
            	      .addProperty(HasName, name.toString())
            		  .addProperty(has_bikes_avlb, available_bikes.toString())
            		  .addProperty(has_stand_avlb, available_bike_stands.toString())
            		  .addProperty(GPS, model.createResource().addProperty(Has_lat, lat.toString())
            		  .addProperty(Has_lng, lng.toString()))
            		  .addProperty(Has_status, status)
            		  .addProperty(Has_updated, updat.toString())
            		  .addProperty(IN_city,Inville)
            		  .addProperty(Has_adress, address)
            		  .addProperty(Has_stands, stands.toString())
            		  .addProperty(Contain, model.createResource(indix).addProperty(RDF.type, Weather)
         						 .addProperty(Has_weather, description)); 
                      
            		 city.addProperty(Contain, Station);
            		         		 
   			}		
 }

	        } catch (Exception e) {
            e.printStackTrace();
        }
        model.write(System.out, "turtle");
  
		// telecharger le model dans Fuseki  
        DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(FUESKI_LOCAL_ENDPOINT);
		accessor.putModel(model);
		//accessor.add(model);
	
    }

  
  }

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	public static JSONObject readJsonFromUrl1(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	}

		    
		

	    
		
		
	


