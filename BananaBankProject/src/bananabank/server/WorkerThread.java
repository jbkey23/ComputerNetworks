package bananabank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.StringTokenizer;

public class WorkerThread extends Thread 
{
	Socket clientSocket;
	ServerSocket serverSocket;
	BananaBank bank;
	
	public WorkerThread(Socket cs, BananaBank b, ServerSocket ss)
	{
		this.clientSocket = cs;
		this.bank = b;
		this.serverSocket = ss;
	}
	

	public String handleRequest(String request)
	{
		StringTokenizer st = new StringTokenizer(request);
		if(st.countTokens() != 3)
		{
			return "FAILURE: Invalid request received!";
		}
		
		int amount = Integer.parseInt(st.nextToken());
		int accountSrc = Integer.parseInt(st.nextToken());
		int accountDst = Integer.parseInt(st.nextToken());
		
		
		Account src = bank.getAccount(accountSrc);
		Account dst = bank.getAccount(accountDst);
		
		if(src == null)
		{
			return "FAILURE: Invalid source account!";
		}
		if(dst == null)
		{
			return "FAILURE: Invalid destination account!";
		}
			
		Account firstLock;
		Account secondLock;
		if(src.hashCode() < dst.hashCode())
		{
			firstLock = src;
			secondLock = dst;
		}
		else
		{
			firstLock = dst;
			secondLock = src;
		}
		
		synchronized(firstLock)
		{
			synchronized(secondLock)
			{
				src.transferTo(amount, dst);
			}
		}
		
		return "SUCCESS: " + amount + " transferred from account " + accountSrc + " to account " + accountDst;
		
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
				if(clientInput.equalsIgnoreCase("SHUTDOWN"))
				{
					int total = 0;
					Collection<Account> accounts = bank.getAllAccounts();
					for(Account a : accounts)
					{
						total += a.getBalance();
					}
					
					output.println(total);
					
					serverSocket.close();
				}
				else
				{
					clientOutput = handleRequest(clientInput);
					//System.out.println("Sending: " + clientOutput);
					output.println(clientOutput);
				}
			}
			//System.out.println("WORKER" + Thread.currentThread().getId() + ": Client disconnected");
			output.close();
			input.close();
		}catch (IOException e) 
		{
			//e.printStackTrace();
		}
		finally
		{
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
}

