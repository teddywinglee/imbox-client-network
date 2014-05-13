package org.imbox.client.network.sharelink;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;


public class Filegenerator
{
	private String account;
	private String filename;
	private String data;
	private boolean status;
	private int errorcode;
	public Filegenerator(String account,String filename)
	{
		this.account = account;
		this.filename = filename;
		errorcode = -1;
		data = new String();
		status = false;
	}
	
	public void sendrequest() throws IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("MAC",Internetrecord.getMAC());
			obj.put("token",Internetrecord.gettoken());
			obj.put("account", this.account);
			obj.put("filename", this.filename);
			Simpleconnection conn = new Simpleconnection();
			readresponse(conn.httppost("generatefile", obj));
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Filegenerator-sendrequest");
		}
		
	}
	
	private void readresponse(HttpResponse res) throws IMBOXNW_jsonException
	{
		try
		{
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader reader = new Responsereader(res);
				JSONObject obj = new JSONObject(reader.getresponse());
				data = obj.getString("data");
				status = obj.getBoolean("succ");
				errorcode = obj.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				data = new String();
				status = false;
				errorcode = -2;
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			data = new String();
			status = false;
			errorcode = 20;
			throw new IMBOXNW_jsonException("Filegenerator-readresponse");
		}
	}
	public String getdata()
	{
		return data;
	}
	public boolean getstatus()
	{
		return status;
	}
	public int geterrorcode()
	{
		return errorcode;
	}
}