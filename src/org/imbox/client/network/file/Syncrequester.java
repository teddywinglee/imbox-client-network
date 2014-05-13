package org.imbox.client.network.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.imbox.infrastructure.file.FileRecH;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Syncrequester
{
	private boolean succ;
	private int errorcode;
	private JSONArray jsonfilelistarray;
	public Syncrequester()
	{
		
	}
	
	public void sendrequest()throws IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try
		{
			JSONObject obj=new JSONObject();
			obj.put("token", Internetrecord.gettoken());
			obj.put("MAC", Internetrecord.getMAC());
			Simpleconnection conn = new Simpleconnection();
			HttpResponse res = conn.httppost("syncrequest", obj);
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader responsereader = new Responsereader(res);
				JSONObject result = new JSONObject(responsereader.getresponse());
				jsonfilelistarray = result.getJSONArray("filelist");
				succ = result.getBoolean("succ");
				errorcode = result.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				jsonfilelistarray = new JSONArray();
				succ = false;
				errorcode = -2;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("sendrequest-sendrequest");
		}
		
	}
	
	public List<FileRecH> getfilelist()
	{
		List<FileRecH> returnlist = new ArrayList<FileRecH>();
		try
		{
			if (jsonfilelistarray.length() > 0)
			{
				for (int i = 0;i<jsonfilelistarray.length();i+=3)
				{
					returnlist.add(new FileRecH(jsonfilelistarray.get(i).toString(),jsonfilelistarray.get(i+1).toString(),jsonfilelistarray.get(i+2).toString()));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnlist;
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