package org.imbox.client.showapi;

import java.util.List;

import org.imbox.client.network.Connectionchecker;
import org.imbox.client.network.block.Blockgetter;
import org.imbox.client.network.block.Blockposter;
import org.imbox.client.network.block.Blockrecordgetter;
import org.imbox.client.network.file.Deletefilerequester;
import org.imbox.client.network.file.Modifyfilerequester;
import org.imbox.client.network.file.Newfilerequester;
import org.imbox.client.network.file.Syncrequester;
import org.imbox.client.network.login.Accountcreator;
import org.imbox.client.network.login.Loginmaster;
import org.imbox.client.network.sharelink.URLgenerator;
import org.imbox.client.network.ultility.Internetrecord;
import org.imbox.infrastructure.Casting;
import org.imbox.infrastructure.Workspace;
import org.imbox.infrastructure.file.Block;
import org.imbox.infrastructure.file.BlockRec;
import org.imbox.infrastructure.file.FileRecH;



public class Supertester
{
	public static void main (String[]args)
	{
		try{
			System.out.println("this is a super tester, will run through all the function needed");
			System.out.println("================================================================");
			System.out.println("testing target: create account,  showing all function");
			Accountcreator ac = new Accountcreator("testaccount", "password");
			ac.register();
			System.out.println("[create account]token = " + Internetrecord.gettoken());
			System.out.println("[create account]succ = " + ac.getstatus());
			System.out.println("[create account]errorcode = " + Integer.toString(ac.geterrorcode()));
			System.out.println("=================================================================");
			System.out.println("testing target: login,  showing all function");
			Loginmaster lg = new Loginmaster("qq", "qq");
			lg.authenticate();
			System.out.println("[login]token = " + Internetrecord.gettoken());
			System.out.println("[login]succ = " + lg.getstatus());
			System.out.println("[login]errorcode = " + Integer.toString(lg.geterrorcode()));
			System.out.println("=================================================================");
			System.out.println("testing target: connectionchecker,  showing all function");
			Connectionchecker cc = new Connectionchecker();
			System.out.println("[connectionchecker]network available: " + cc.checknetworkstatus());
			System.out.println("=================================================================");
			System.out.println("testing target: New file,  showing all function");
			Newfilerequester nfq = new Newfilerequester("test00.pdf", "1234567889");
			nfq.sendrequest();
			System.out.println("[new file] succ = " + nfq.getstatus());
			System.out.println("[new file] errorcode = " + nfq.geterrorcode());
			System.out.println("[new file] needtopostblock = " + nfq.needtopostblock());
			System.out.println("================================================================");
			System.out.println("testing target: Delete file,  showing all function");
			Deletefilerequester dfq = new Deletefilerequester("findnemo.txt");
			dfq.sendrequest();
			System.out.println("[new file] succ = " + dfq.getstatus());
			System.out.println("[new file] errorcode = " + dfq.geterrorcode());
			System.out.println("================================================================");
			System.out.println("testing target: Modify file,  showing all function");
			Modifyfilerequester mfq = new Modifyfilerequester("test.txt","1234567889");
			mfq.sendrequest();
			System.out.println("[Modify file] succ = " + mfq.getstatus());
			System.out.println("[Modify file] errorcode = " + mfq.geterrorcode());
			System.out.println("================================================================");
			System.out.println("testing target: syncrequest,  showing all function");
			Syncrequester srq = new Syncrequester();
			srq.sendrequest();
			System.out.println("[syncrequest] succ = " + srq.getstatus());
			System.out.println("[syncrequest] errorcode = " + srq.geterrorcode());
			List<FileRecH> filelist = srq.getfilelist();
			for(int i = 0;i<filelist.size();i++)
			{
				System.out.println("[syncrequest] data = " + filelist.get(i).getName() + "\t" + filelist.get(i).getCurrentMD5() + "\t" + filelist.get(i).getPastMD55());
			}
			System.out.println("================================================================");
			System.out.println("testing target: block post,  showing all function");
			Blockposter bp = new Blockposter("test00.txt",Block.readBlockFromHD(Workspace.SYSDIRs, "4e4b036c810b7d2a4d03eb1ab124d0e8"),0);
			bp.sendrequest();
			System.out.println("[block post] succ = " + bp.getstatus());
			System.out.println("[block post] errorcode = " + bp.geterrorcode());
			System.out.println("================================================================");
			System.out.println("testing target: block get,  showing all function");
			Blockgetter bg = new Blockgetter("4e4b036c810b7d2a4d03eb1ab124d0e8", 0);
			bg.sendrequest();
			System.out.println("[block get] succ = " + bg.getstatus());
			System.out.println("[block get] errorcode = " + bg.geterrorcode());
			Block.writeBlock(Workspace.SYSDIRs, Casting.stringToBytes(bg.getdata()));
			System.out.println("================================================================");
			System.out.println("testing target:get block record,  showing all function");
			Blockrecordgetter brg = new Blockrecordgetter("test00.txt");
			brg.sendrequest();
			System.out.println("[get block record] succ = " + brg.getstatus());
			System.out.println("[get block record] errorcode = " + brg.geterrorcode());
			List<BlockRec> blocklist = brg.getblocklist();
			for(int i=0;i<blocklist.size();i++)
			{
				System.out.println("[get block record] block name = " + blocklist.get(i).getName() + " seq = " + blocklist.get(i).getPos());
			}
			System.out.println("================================================================");
			System.out.println("testing target:get URL,  showing all function");
			URLgenerator ug = new URLgenerator("test.txt");
			ug.sendrequest();
			System.out.println("[get URL] succ = " + ug.getstatus());
			System.out.println("[get URL] errorcode = " + ug.geterrorcode());
			System.out.println("[get URL] URL = " + ug.geturl());
			System.out.println("END OF TESTING............");
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("FOR TEST, CATCH ALL EXCEPTION");
		}
		
	}
}