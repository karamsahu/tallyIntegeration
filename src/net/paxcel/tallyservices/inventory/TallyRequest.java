package net.paxcel.tallyservices.inventory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TallyRequest {
	   	 
	/*suppy xml to this method and it will output resopnse to console*/
	    public static String SendToTally(String requestFor) throws Exception{
	        String Url = "http://172.16.1.126:9000/";      
	        String SOAPAction = "";
	// request comes in xml format containing data inside
	        String request = requestFor;
	        System.out.println("Request :" +request);
	// Create the connection where we're going to send the file.
	        URL url = new URL(Url);
	        URLConnection connection = url.openConnection();
	        HttpURLConnection httpConn = (HttpURLConnection) connection;
	        ByteArrayInputStream bin = new ByteArrayInputStream(request.getBytes());
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        System.out.println(bout);
	// Copy the SOAP file to the open connection.
	        copy(bin, bout);     
	        byte[] b = bout.toByteArray();
	// Set the appropriate HTTP parameters.
	        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
	        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	        httpConn.setRequestProperty("SOAPAction", SOAPAction);
	        httpConn.setRequestMethod("POST");
	        httpConn.setDoOutput(true);
	        httpConn.setDoInput(true);

	// Everything's set up; send the XML that was read in to b.
	        OutputStream out = httpConn.getOutputStream();
	        out.write(b);
	        out.close();

	// Read the response and write it to standard out.

	        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
	        BufferedReader in = new BufferedReader(isr);
	        String inputLine;
	        String tallyXmlResponse ="";

	        while ((inputLine = in.readLine()) != null) {
	            //System.err.println(inputLine);
	            tallyXmlResponse+=inputLine+"\n";
	        }
	        
	        System.err.println(tallyXmlResponse);

	        in.close();
	        return tallyXmlResponse;
	    }

	    public static void copy(InputStream in, OutputStream out)
	            throws IOException {

	// do not allow other threads to read from the
	// input or write to the output while copying is
	// taking place

	        synchronized (in) {
	            synchronized (out) {

	                byte[] buffer = new byte[256];
	                while (true) {
	                    int bytesRead = in.read(buffer);
	                    if (bytesRead == -1) {
	                        break;
	                    }
	                    out.write(buffer, 0, bytesRead);
	                }
	            }
	        }
	    }
  
}// class closed
