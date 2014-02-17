package serverProject.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class WorkerThread extends Thread 
{
	Socket clientSocket;
	
	public WorkerThread(Socket cs)
	{
		this.clientSocket = cs;
	}
	
	@Override
	public void run() 
	{
		//System.out.println("WORKER" + Thread.currentThread().getId() + ": Worker thread starting");
		try 
		{
		BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintStream output = new PrintStream(clientSocket.getOutputStream());
		
		String clientInput;
		String clientOutput;
		
			while((clientInput = input.readLine()) != null)
			{
				//System.out.println("Received: " + clientInput);
				clientOutput = clientInput.toUpperCase();
				//System.out.println("Sending: " + clientOutput);
				output.println(clientOutput);
			}
			//System.out.println("WORKER" + Thread.currentThread().getId() + ": Client disconnected");
			output.close();
			input.close();
		}catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

