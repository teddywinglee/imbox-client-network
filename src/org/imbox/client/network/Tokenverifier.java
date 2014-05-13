package org.imbox.client.network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;

public class Tokenverifier
{
	private String token;
	private String MAC;
	private boolean internetstatus;
	private boolean tokenverifyresult;
	private int errorcode;
	public Tokenverifier()
	{
		token = Internetrecord.gettoken();
		MAC = Internetrecord.getMAC();
	}
	
	public void verifytoken() throws IMBOXNW_httpstatusException, IOException, IMBOXNW_jsonException
	{
		Connectionchecker cc = new Connectionchecker();
		internetstatus = cc.checknetworkstatus();
		if (internetstatus)
		{
			
			try {
				JSONObject obj = new JSONObject();
				obj.put("token", token);
				obj.put("MAC", MAC);
				Simpleconnection conn = new Simpleconnection();
				readresponse(conn.httppost("verifytoken", obj));
				internetstatus = true;
			}catch(JSONException e)
			{
				e.printStackTrace();
				throw new IMBOXNW_jsonException("Tokenverifier-verifytoken");
			}
				
		}else
		{
			tokenverifyresult = false;
		}
	}
	
	private void readresponse(HttpResponse res) throws IOException, IMBOXNW_jsonException
	{
		try {
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader reader = new Responsereader(res);
				JSONObject obj = new JSONObject(reader.getresponse());
				tokenverifyresult = obj.getBoolean("succ");
				errorcode = obj.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				tokenverifyresult = false;
				errorcode = -2;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			tokenverifyresult = false;
			errorcode =20;
			throw new IMBOXNW_jsonException("Tokenverifier-readresponse");
		}
	}
	
	public boolean getinsternetstatus()
	{
		return internetstatus;
	}
	
	public boolean gettokenverifyresult()
	{
		return tokenverifyresult;
	}
	
	public int geterrorcode()
	{
		return errorcode;
	}
}