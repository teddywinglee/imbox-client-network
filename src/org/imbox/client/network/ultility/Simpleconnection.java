package org.imbox.client.network.ultility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.json.JSONObject;


public class Simpleconnection 
{
	private String URL = "http://54.254.1.241:8080";//base domain  real : 54.254.1.241
	private HttpClient connectionmanager;
	
	public Simpleconnection()
	{
		connectionmanager = HttpClientBuilder.create().build();
	}
	
	public HttpResponse httppost(String targetpage,JSONObject obj) throws IMBOXNW_httpstatusException, IOException
	{
		
		try {
			String targethost = URL+"/"+targetpage;
			HttpPost httppost = new HttpPost(targethost);
			httppost.setEntity(new StringEntity(obj.toString()));
			HttpResponse res = connectionmanager.execute(httppost);
			return res;
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_httpstatusException("Simpleconnection");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new IMBOXNW_httpstatusException("Simpleconnection");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Simpleconnection");
		}
	}
	
	public HttpResponse httpget(String targetpage)
	{
		try
		{
			HttpGet httpget = new HttpGet(URL+"/"+targetpage);
			return connectionmanager.execute(httpget);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}