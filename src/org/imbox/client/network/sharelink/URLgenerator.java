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


public class URLgenerator
{
	private String filename;
	private String URL;
	private boolean status;
	private int errorcode;
	public URLgenerator(String filename)
	{
		this.filename = filename;
		URL=new String();
		status = false;
		errorcode = -1;
	}
	
	public void sendrequest()throws IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("MAC",Internetrecord.getMAC());
			obj.put("token",Internetrecord.gettoken());
			obj.put("filename", this.filename);
			Simpleconnection conn = new Simpleconnection();
			readresponse(conn.httppost("generateURL", obj));
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("URLgenerator-sendrequest");
		}
		
	}
	
	private void readresponse(HttpResponse res) throws IMBOXNW_jsonException, IOException
	{
		try
		{
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader reader = new Responsereader(res);
				JSONObject obj = new JSONObject(reader.getresponse());
				URL = obj.getString("URL");
				status = obj.getBoolean("succ");
				errorcode = obj.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				URL=new String();
				status = false;
				errorcode = -2;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			URL = new String();
			status = false;
			errorcode = 20;
			throw new IMBOXNW_jsonException("URLgenerator-readresponse");
		}
	}
	public String geturl()
	{
		return URL;
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