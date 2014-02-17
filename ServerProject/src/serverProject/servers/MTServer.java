package serverProject.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MTServer {

	private static final int PORT = 7777;
	
	public static void main(String[] args) 
	{
		ServerSocket ss = null;
		try 
		{
			ss = new ServerSocket(PORT);
			System.out.println("MAIN: ServerSocker created");
			while(true)
			{
				//System.out.println("MAIN: Waiting for client connection on port " + PORT);
				Socket cs = ss.accept();
				//System.out.println("MAIN: Client connected");
				new WorkerThread(cs).start();
			}
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	

}
