package org.imbox.client.network.ultility;

import java.net.InetAddress;
import java.net.NetworkInterface;


public class Internetrecord
{
	private static String MAC;
	private static String token;
	static
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
				MAC = sb.toString();
				token = "";
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public static void settoken(String _token)
	{
		token = _token;
	}
	
	public static String gettoken()
	{
		return token;
	}
	
	public static String getMAC()
	{
		return MAC;
	}
	
}