package com.panzyma.nm.auxiliar; 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus; 
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log; 

public class NMComunicacion {
 
	static String TAG=NMComunicacion.class.getClass().getSimpleName();
 	public NMComunicacion(){} 
 	
	@SuppressWarnings("unused")
	public static synchronized boolean DeviceHasConnection(Context context)
	{		
		
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) 
		{
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
	    
		return true;
		
	}
	
	public static synchronized Object InvokeMethod(ArrayList<Parameters> params,String URL,String NAME_SPACE,String METHOD_NAME)throws Exception
    { 
        SoapObject request =new SoapObject(NAME_SPACE,METHOD_NAME); 
        for(PropertyInfo pinfo:params) 
        	request.addProperty(pinfo);   
        SoapSerializationEnvelope envelope = GetEnvelope(request);
        return  MakeCall(URL,envelope,NAME_SPACE,METHOD_NAME);
    } 
	
    public static synchronized SoapSerializationEnvelope GetEnvelope(SoapObject Soap)throws Exception
    {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Soap); 
        return envelope;
    } 
    
	public static synchronized Object MakeCall(String URL, SoapSerializationEnvelope Envelope, String NAMESPACE, String METHOD_NAME)throws Exception
    {   
	        HttpTransportSE ht = new HttpTransportSE(URL);
	        ht.debug = true;
	        ht.call(NAMESPACE+METHOD_NAME, Envelope);
        return  Envelope.getResponse(); 
    }
	
	public static synchronized JSONObject InvokeService(String URL) throws Exception
	{
		return new JSONObject(MakeCall_To_Service(URL).toString());
	}
	
	public static synchronized JSONArray InvokeService2(String URL) throws Exception
	{
		return MakeCall_To_Service2(URL);
	}
	
	public static synchronized StringBuilder MakeCall_To_Service(String url) throws Exception
	{  
        HttpClient httpclient = new DefaultHttpClient();  
        
        HttpGet request = new HttpGet(url);                
    	request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        
    	HttpResponse response = httpclient.execute(request);
    	
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) { 
            Log.w(TAG, "Error " + statusCode + " for URL " + url); 
            return null;
        }
         
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
    	StringBuilder builder = new StringBuilder();
    	for (String line = null; (line = reader.readLine()) != null ; ) {
    		builder.append(line).append("\n");
    	}   
        return builder;     
    }
	
	public static synchronized JSONArray  MakeCall_To_Service2(String url) throws Exception
	{  
		  HttpClient httpclient = new DefaultHttpClient();  
	        
	        HttpGet request = new HttpGet(url);                
	    	request.setHeader("Accept", "application/json");
	        request.setHeader("Content-type", "application/json");
	        
	    	HttpResponse response = httpclient.execute(request);
	    	
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            Log.w(TAG, "Error " + statusCode + " for URL " + url); 
	            return null;
	        }
	         
	        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	    	StringBuilder builder = new StringBuilder();
	    	for (String line = null; (line = reader.readLine()) != null ; ) {
	    		builder.append(line).append("\n");
	    	}  
	      JSONArray arrayjson = new JSONArray(new String(builder));
        return arrayjson;
    }
    
	
	/*
	  Comunicación Mediante Bluetooth
	 * */

}
