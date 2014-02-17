package serverProject.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {

	private static final int MT_PORT = 7777;
	private static final int S_PORT = 5555;
	
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Socket simple = new Socket("localhost", S_PORT);
		System.out.println("Client is connected to the simple server");
		Socket mt = new Socket("localhost", MT_PORT);
		System.out.println("Client is connected to the threaded server");
		
		PrintStream ps_simple = new PrintStream(simple.getOutputStream());
		ps_simple.println("Hello Simple World!");
		System.out.println("Client sent message to simple server");
		
		PrintStream ps_threaded = new PrintStream(mt.getOutputStream());
		ps_threaded.println("Hello Threaded World!");
		System.out.println("Client sent message to threaded server");
		
		BufferedReader simple_input = new BufferedReader(new InputStreamReader(simple.getInputStream()));
		BufferedReader mt_input = new BufferedReader(new InputStreamReader(mt.getInputStream()));
		
		String simpleText;
		String mtText;
		
		simpleText = simple_input.readLine();
		mtText = mt_input.readLine();
		
		System.out.println("Received: " + simpleText + " from simple server");
		System.out.println("Received: " + mtText + " from threaded server");
	
		
		simple_input.close();
		mt_input.close();
		simple.close();
		mt.close();
	}

}
