package websem;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
/**
 * @author Crunchify.com
 */
 
public class CrunchifyJSONReadFromFile {
 
        @SuppressWarnings("unchecked")
    public static void main(String[] args) {
      //  JSONParser parser = new JSONParser();
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
   ArrayList<String> list=new ArrayList<>();
  list.add("lyon");
  list.add("toulouse");
  list.add("mulhouse");
  list.add("marseille");
  list.add("nantes");
  list.add("rouen");
  for(int i = 0 ; i < list.size(); i++) {
	  
  
        try {
 
           // Object obj = parser.parse(new FileReader(
                //    "D:\\Etude\\dsc\\cours\\web sematic\\station.json"));
        	
        	
			JSONObject jsonObject = readJsonFromUrl("https://transport.data.gouv.fr/gbfs/"+ list.get(i)+"/station_status.json");
 
         System.out.println(list.get(i));
 
            int ttl =  (Integer) jsonObject.get("ttl");
            int last_updat = (Integer) jsonObject.get("last_updated");
            System.out.println("ttl: " + ttl);
            System.out.println("last_updated: " + last_updat);
            System.out.println("\nstations");
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray stations = (JSONArray) data.get("stations");
            
           // JSONArray jsonObjects =  (JSONArray) obj;

	        for (Object o : stations) {
	            JSONObject jsonObjec = (JSONObject) o;
	            String id = (String) jsonObjec.get("station_id");
	            System.out.println("station_id: " + id);

            int num_dock_available = (Integer)jsonObjec.get("num_docks_available");
	            System.out.println("num_docks_available: " + num_dock_available);
           
 
        }
	        } catch (Exception e) {
            e.printStackTrace();
        }
    }}



	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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
		    
		

	    
		
		
	


