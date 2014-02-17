package serverProject.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class WorkerClient extends Thread {

	Socket clientSocket;
	private static final int MESSAGES = 100000;
	public WorkerClient(Socket cs)
	{
		clientSocket = cs;
	}
	@Override
	public void run() {
		PrintStream ps = null;
		BufferedReader input = null;

		try {
			

			//System.out.println("Benchmark Client #"
			//		+ Thread.currentThread().getId()
			//		+ " is connected to the server on port " + clientSocket.getPort());

			
			ps = new PrintStream(clientSocket.getOutputStream());
			input = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String text;

			for (int j = 1; j <= MESSAGES; j++) {
				ps.println("Hello Threaded World #" + j + "!");
				text = input.readLine();
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			try {
				clientSocket.close();
				input.close();
				ps.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
