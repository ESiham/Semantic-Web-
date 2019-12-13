package websem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class main {
	static String FUESKI_LOCAL_ENDPOINT  = "http://localhost:3030/bicycleStore";
	public static void main(String[] args) {
		 ArrayList<String> list=new ArrayList<>();
		  list.add("lyon");
		  list.add("toulouse");
		  list.add("mulhouse");
		  list.add("marseille");
		  list.add("nantes");
		  list.add("rouen");
		  for(int i = 0 ; i < list.size(); i++) {
		     try {	
				JSONArray jsonObjects= readJsonFromUrl("https://api.jcdecaux.com/vls/v1/stations?contract="+list.get(i)+"&apiKey=345d6d791a9f6a8152fcf679657486e851cb0a95");
				
		  for (Object obj: jsonObjects) {
			    JSONObject jsonObjec = (JSONObject) obj;
		        Integer available_bike_stands = (int) jsonObjec.get("available_bike_stands");
		        Integer available_bikes = (int) jsonObjec.get("available_bikes");
		        String status = (String) jsonObjec.get("status");
		        Long updat =(Long) jsonObjec.get("last_update");
		        Integer number =(Integer) jsonObjec.get("number");
		        int stands=(int) jsonObjec.get("bike_stands");
		          String query = "PREFIX j.0:<http://www.w3.org/2000/01/rdf-schema/>\r\n"
		          	+"prefix station:<http://station_"+number +"_lyon.org/>\r\n"
		           	+ "prefix xsd: <http://www.w3.org/2001/XMLSchema#> \r\n"
		           	+ "INSERT DATA { <http://station_"+number +"_lyon.org/>   j.0:Has_bike_AV_2 "+ available_bikes+" ;\r\n"
		           	+ "                                                       j.0:Has_stand_AV_2  "+available_bike_stands+" ;\r\n"
		           	+ "                                                       j.0:Has_stands_2  "+stands+" ;\r\n"
		           	+"                                                        j.0:Has_updated_2   "+ updat +" }";
		            			
				
		            			System.out.println(query);
		            			
		            			UpdateRequest update  = UpdateFactory.create(query);
		            			UpdateProcessor qexec = UpdateExecutionFactory.createRemote(update, FUESKI_LOCAL_ENDPOINT);
		            			qexec.execute();
		   	 	
		   	        }
	
		 		
			        } catch (Exception e) {
		            e.printStackTrace();
		        }
	
				
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

private static String readAll(Reader rd) throws IOException {
	StringBuilder sb = new StringBuilder();
	int cp;
	while ((cp = rd.read()) != -1) {
		sb.append((char) cp);
	}
	return sb.toString();
}

}

	    
	


