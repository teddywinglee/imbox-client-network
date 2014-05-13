package org.imbox.client.network.file;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

public class Deletefilerequester
{
	private String filename;
	private int errorcode;
	private boolean succ;
	public Deletefilerequester(String filename)
	{
		this.filename = filename;
		errorcode = -1;
		succ = false;
	}
	
	public void sendrequest() throws IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try
		{
			JSONObject obj=new JSONObject();
			obj.put("token", Internetrecord.gettoken());
			obj.put("MAC", Internetrecord.getMAC());
			obj.put("filename", filename);
			Simpleconnection conn = new Simpleconnection();
			HttpResponse res = conn.httppost("deletefile", obj);
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader responsereader = new Responsereader(res);
				JSONObject result = new JSONObject(responsereader.getresponse());
				succ = result.getBoolean("succ");
				errorcode = result.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				succ = false;
				errorcode = -2;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Deletefilerequester-sendrequest");
		}
	}
	
	public boolean getstatus()
	{
		return succ;
	}
	
	public int geterrorcode()
	{
		return errorcode;
	}
}