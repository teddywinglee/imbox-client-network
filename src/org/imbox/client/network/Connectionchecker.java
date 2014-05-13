package org.imbox.client.network;

import org.apache.http.HttpResponse;
import org.imbox.client.network.ultility.Simpleconnection;
import org.imbox.infrastructure.exceptions.IMBOXNW_httpstatusException;


public class Connectionchecker
{
	public Connectionchecker()
	{
		
	}
	
	public boolean checknetworkstatus() throws IMBOXNW_httpstatusException
	{
		try
		{
			Simpleconnection conn = new Simpleconnection();
			HttpResponse res = conn.httpget("networkcheck");
			if (res.getStatusLine().getStatusCode() == 200)
			{
				return true;
			}else
			{
				return false;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new IMBOXNW_httpstatusException("connectionchecker");
		}
	}
}