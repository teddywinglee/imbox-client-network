package org.imbox.client.network.lock;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;


public class Serverlockreleaser
{
	private boolean status;
	private int errorcode;
	public Serverlockreleaser()
	{
		
	}
	
	public void releaselock()throws  IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("token", Internetrecord.gettoken());
			obj.put("MAC", Internetrecord.getMAC());
			Simpleconnection conn = new Simpleconnection();
			HttpResponse res = conn.httppost("releaseserverlock", obj);
			checkstatus(res);
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Serverlockreleaser-releaselock");
		}
	}
	
	private void checkstatus(HttpResponse res) throws IMBOXNW_jsonException, IOException
	{
		try 
		{
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader reader = new Responsereader(res);
				JSONObject obj = new JSONObject(reader.getresponse());
				status = obj.getBoolean("succ");
				errorcode = obj.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				status = false;
				errorcode = -2;
			}
		}catch (JSONException e) {
			status = false;
			errorcode = 20;
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Serverlockreleaser-checkstatus");
		}
		
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