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

public class Newfilerequester
{
	private String filename;
	private String md5;
	private int errorcode;
	private boolean succ;
	private boolean needtopostblock;
	public Newfilerequester(String filename,String md5)
	{
		this.filename = filename;
		this.md5 = md5;
		errorcode = -1;
		succ = false;
		needtopostblock = true;
	}
	
	public void sendrequest()throws IMBOXNW_jsonException, IOException, IMBOXNW_httpstatusException
	{
		try
		{
			JSONObject obj=new JSONObject();
			obj.put("token", Internetrecord.gettoken());
			obj.put("MAC", Internetrecord.getMAC());
			obj.put("filename", filename);
			obj.put("md5", md5);
			Simpleconnection conn = new Simpleconnection();
			HttpResponse res = conn.httppost("newfile", obj);
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
			if (succ && errorcode == 40)
			{
				needtopostblock = false;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Newfilerequester-sendrequest");
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
	public boolean needtopostblock()
	{
		return needtopostblock;
	}
}