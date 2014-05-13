package org.imbox.client.network.login;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.client.network.ultility.Responsereader;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.Casting;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;
import org.imbox.infrastructure.exceptions.IMBOXNW_jsonException;
import org.json.JSONException;
import org.json.JSONObject;



public class Loginmaster
{
	private String accountname;
	private String password;
	private boolean status;
	private int errorcode;
	public Loginmaster(String accountname,String password) 
	{ 
		this.accountname = accountname; 
		this.password = password ;
		this.status = false; 
		this.errorcode = -1; 
	} 
	public Loginmaster() 
	{ 
		this("",""); 
	}
	
	public void setInfo(String name,String pwd) 
	{ 
		this.accountname = name; 
		this.password = pwd;
	}
	
	public void authenticate() throws IMBOXNW_jsonException, IMBOXNW_httpstatusException, IOException
	{
		try {
			JSONObject obj=new JSONObject();
			obj.put("account", accountname);
			obj.put("password", getencrypt());
			obj.put("MAC", Internetrecord.getMAC());
			Simpleconnection conn = new Simpleconnection();
			HttpResponse res = conn.httppost("login", obj);
			readresponse(res);
		} catch (JSONException e){
			e.printStackTrace();
			Internetrecord.settoken("");
			status = false;
			errorcode = 0;
			throw new IMBOXNW_jsonException("Loginmaster-authenticate");
		}	 
	}
	
	private void readresponse(HttpResponse res) throws IMBOXNW_jsonException, IOException
	{
		try{
			if (res.getStatusLine().getStatusCode() == 200)
			{
				Responsereader responsereader = new Responsereader(res);
				JSONObject result = new JSONObject(responsereader.getresponse());
				Internetrecord.settoken(result.getString("token"));
				status = result.getBoolean("succ");
				errorcode = result.getInt("errorcode");
			}else
			{
				System.out.println("http error: " + Integer.toString(res.getStatusLine().getStatusCode()));
				Internetrecord.settoken("");
				status = false;
				errorcode = -2;
			}
		}catch(JSONException e)
		{
			e.printStackTrace();
			Internetrecord.settoken("");
			status = false;
			errorcode = 20;
			throw new IMBOXNW_jsonException("Loginmaster-readresponse");
		}
	}
	
//	public String gettoken()
//	{
//		return token;
//	}
	
	public boolean getstatus()
	{
		return status;
	}
	
	public int geterrorcode()
	{
		return errorcode;
	}
	
	public String getMAC() 
	{ 
		return getmacaddress(); 
	}
	
	
	private String getencrypt()
	{
		try {
			MessageDigest encrypter = MessageDigest.getInstance("md5");
			encrypter.update(password.getBytes());
			byte[] digest = encrypter.digest();
			return Casting.bytesToString(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	private String getmacaddress()
	{
		InetAddress ip;
		try
		{
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));		
			}
			return sb.toString();
		}catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
	}
	
}