package bananabank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {
	
	private static final int PORT = 7777;
	
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Socket s = new Socket("localhost", PORT);
		System.out.println("Client is connected to the server");
		
		
		PrintStream ps = new PrintStream(s.getOutputStream());
		BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String result;
		
		ps.println("10 33333 44444");
		System.out.println("10 33333 44444");
				
		result = input.readLine();
		System.out.println("Received: " + result + " from server");
	
		ps.println("1 99999 44444");
		System.out.println("1 99999 44444");
		
		result = input.readLine();
		System.out.println("Received: " + result + " from server");
		
		ps.println("1 55555 44444");
		System.out.println("1 55555 44444");
		
		result = input.readLine();
		System.out.println("Received: " + result + " from server");
		
		ps.println("SHUTDOWN");
		System.out.println("SHUTDOWN");
		
		result = input.readLine();
		System.out.println("Received: " + result + " from server");
		
		input.close();
		s.close();
	}

}
