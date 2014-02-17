package serverProject.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class BenchmarkClient {

	private static final int MT_PORT = 7777;
	private static final int S_PORT = 5555;

	private static final int DURATION = 30;
	
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
	
		
		long startTime = 0;
		long endTime = 0;
		
		System.out.println("Simple benchmark started");
		
		double simpleCount = 0;
		
		startTime = System.currentTimeMillis();
		endTime = startTime + DURATION;
		while(startTime < endTime)
		{
			Socket s = new Socket("localhost", S_PORT);
			new WorkerClient(s).start();
			startTime = System.currentTimeMillis();
			simpleCount++;
		}
	

		
		System.out.println("Threaded benchmark started");
		
		double threadedCount = 0;
		
		startTime = System.currentTimeMillis();
		endTime = startTime + DURATION;
		
		while(startTime < endTime)
		{
			Socket s = new Socket("localhost", MT_PORT);
			new WorkerClient(s).start();
			startTime = System.currentTimeMillis();
			threadedCount++;
		}
	
	
		
		System.out.println("Simple Server connections: " + simpleCount);
		System.out.println("Threaded Server connections: " + threadedCount);
		
		System.out.println("Simple Server rate: " + (simpleCount/DURATION) + " connections/ms");
		System.out.println("Threaded Server rate: " + (threadedCount/DURATION) + " connections/ms");
		

	}

}
