package sy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class HttpClientUrl {
	
	
	public static String urlConnectionPost(String urls) {
	    StringBuilder responseBuilder = null;
	    BufferedReader reader = null;
	    OutputStreamWriter wr = null;

	    URL url;
	    try {
	    	System.out.println(urls);
	        url = new URL(urls);
	        URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        conn.setConnectTimeout(1000 * 5);
	        wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write("");
	        wr.flush();

	        // Get the response
	        reader = new BufferedReader(new InputStreamReader(conn
	                .getInputStream()));
	        responseBuilder = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            responseBuilder.append(line);
	        }
	        wr.close();
	        reader.close();
	        return responseBuilder.toString();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }return "";

	}


}
