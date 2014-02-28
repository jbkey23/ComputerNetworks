package bananabank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import bananabank.benchmark.BananaBankBenchmark;

public class MTServer {

	//private static final int PORT = 7777;
	private static final String FILENAME = "accounts.txt"; 
	
	
	public static void main(String[] args) 
	{
		ServerSocket ss = null;
		BananaBank bank = null;
		ArrayList<WorkerThread> threads = new ArrayList<WorkerThread>();
		
		try 
		{
			bank = new BananaBank(FILENAME);
			ss = new ServerSocket(BananaBankBenchmark.PORT);
			System.out.println("ServerSocker created");
			while(true)
			{
				//System.out.println("MAIN: Waiting for client connection on port " + PORT);
				Socket cs = ss.accept();
				//System.out.println("MAIN: Client connected");
				WorkerThread wt = new WorkerThread(cs,bank,ss);
				wt.start();
				threads.add(wt);
			}
		}catch (IOException e) 
		{
			//e.printStackTrace();
		}
		finally
		{
			try {
				ss.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			for(WorkerThread t : threads)
			{
				try {
					t.join();
				} catch (InterruptedException e) {
					
				}
			}
			
			try {
				bank.save(FILENAME);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
		
		
	}

	

}
