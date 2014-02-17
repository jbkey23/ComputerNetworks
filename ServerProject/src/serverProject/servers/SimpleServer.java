package serverProject.servers;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

	private static final int PORT = 5555;
	
	public static void main(String[] args) 
	{
		ServerSocket ss = null;
		try 
		{
			ss = new ServerSocket(PORT);
			System.out.println("ServerSocket created");
			while(true)
			{
				//System.out.println("Waiting for client connection on port " + PORT);
				Socket cs = ss.accept();
				//System.out.println("Client connected");
				
				BufferedReader input = new BufferedReader(new InputStreamReader(cs.getInputStream()));
				PrintStream output = new PrintStream(cs.getOutputStream());
				
				String clientInput;
				String clientOutput;
				while((clientInput = input.readLine()) != null)
				{
					//System.out.println("Received: " + clientInput);
					clientOutput = clientInput.toUpperCase();
					//System.out.println("Sending: " + clientOutput);
					output.println(clientOutput);
				}
				//System.out.println("Client disconnected");
				output.close();
				input.close();
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
