package org.imbox.client.network.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.imbox.infrastructure.file.BlockRec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Blockrecordgetter
{
	private String filename;
	private boolean succ;
	private int errorcode;
	private JSONArray jsonblocklistarray;
	public Blockrecordgetter(String filename)
	{
		this.filename = filename;
		succ = false;
		errorcode = -1;
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
			HttpResponse res = conn.httppost("getblockrecord", obj);
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader responsereader = new Responsereader(res);
				JSONObject result = new JSONObject(responsereader.getresponse());
				jsonblocklistarray = result.getJSONArray("blocklist");
				succ = result.getBoolean("succ");
				errorcode = result.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				jsonblocklistarray = new JSONArray();
				succ = false;
				errorcode = -2;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Blockrecordgetter-sendrequest");
		}
	}
	
	public List<BlockRec> getblocklist() throws IMBOXNW_jsonException
	{
		List<BlockRec> returnlist = new ArrayList<BlockRec>();
		try
		{
			for (int i = 0;i<jsonblocklistarray.length();i++)
			{
				returnlist.add(new BlockRec(jsonblocklistarray.get(i).toString(), (int) i));
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("Blockrecordgetter-getblocklist");
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