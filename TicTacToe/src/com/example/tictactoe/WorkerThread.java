package com.example.tictactoe;

import java.io.IOException;
import java.net.DatagramPacket;

public class WorkerThread extends Thread {
	
	public static final int MAX_PACKET_SIZE = 512;
	
	@Override
	public void run() {
		
		
		while(true)
		{
			byte[] buf = new byte[MAX_PACKET_SIZE];
			
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			
			try {
				MainActivity.socket.receive(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String payload = new String(p.getData(), 0, p.getLength()).trim();
			
			if(payload.startsWith("REGISTERED"))
			{
				MainActivity.handler.obtainMessage(MainActivity.REGISTERED, payload).sendToTarget();
			}
			else if(payload.startsWith("MESSAGE"))
			{
				MainActivity.handler.obtainMessage(MainActivity.PUT_LETTER, payload).sendToTarget();
			}
		}
	}

}
