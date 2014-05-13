package	org.imbox.client.network.ultility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

public class Responsereader
{
	private String responsebody;
	public Responsereader(HttpResponse res ) throws IOException
	{
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
		String readLine;
		responsebody = "";
		while (((readLine = br.readLine()) != null)) 
		{
		   responsebody = responsebody + readLine;
		}
		}catch(IOException e)
		{
			e.printStackTrace();
			throw new IOException();
		}
	}
	
	public String getresponse()
	{
		return responsebody;
	}
}