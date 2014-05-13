package org.imbox.client.network.block;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.Casting;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;


public class Blockposter
{
	private String blockdatastring;
	private String filename;
	private int seq;
	private boolean status;
	private String datastring;
	private int errorcode;
	
	public Blockposter(String filename, String blockdata,int seq)
	{
		blockdatastring = blockdata;
		this.filename = filename;
		this.seq = seq;
		status = false;
		errorcode = -1;
		datastring = new String();
	}
	
	public Blockposter(String filename, byte[] blockdata,int seq)throws Exception
	{
		
		try
		{
			blockdatastring = Casting.bytesToString(blockdata);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception();
		}
		this.filename = filename;
		this.seq = seq;
		status = false;
		errorcode = -1;
		datastring = new String();
	}
	
	public void sendrequest() throws  IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("token", Internetrecord.gettoken());
			obj.put("MAC" , Internetrecord.getMAC());
			obj.put("filename", filename);
			obj.put("blockdata", blockdatastring);
			obj.put("seq", seq);
			Simpleconnection conn = new Simpleconnection();
			readresponse(conn.httppost("postblock", obj));
			
		}catch(JSONException e)
		{
			e.printStackTrace();
			throw new IMBOXNW_jsonException("blockpster-sendrequest");
		}
	}
	
	private void readresponse(HttpResponse res) throws IMBOXNW_jsonException, IOException
	{
		
		try {
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
		} catch (JSONException e) {
			e.printStackTrace();
			status = false;
			errorcode = 20;
			throw new IMBOXNW_jsonException("blockposter-readresponse");
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
	
	public String getdata()
	{
		return datastring;
	}

}