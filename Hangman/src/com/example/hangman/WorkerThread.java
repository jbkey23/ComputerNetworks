package com.example.hangman;

import java.io.IOException;
import java.net.DatagramPacket;

import android.util.Log;

public class WorkerThread extends Thread {
	
	public static final int MAX_PACKET_SIZE = 512;
	
	@Override
	public void run() {
		
		
		while(true)
		{
			byte[] buf = new byte[MAX_PACKET_SIZE];
			
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			
			try {
				FirstActivity.socket.receive(p);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		  
			String payload = new String(p.getData(), 0, p.getLength()).trim();
			
			if(payload.startsWith("REGISTERED"))
			{
				Log.d("Received", payload);
				MainActivity.handler.obtainMessage(MainActivity.REGISTERED, payload).sendToTarget();
			}
			else if(payload.startsWith("START"))
			{
				Log.d("Received", payload);
				MainActivity.handler.obtainMessage(MainActivity.WORD, payload).sendToTarget();
			}
			else if(payload.startsWith("LEADERBOARD"))
			{
				Log.d("Received", payload);
				LeaderboardActivity.handler.obtainMessage(LeaderboardActivity.LEADER, payload).sendToTarget();
			}
			else if(payload.startsWith("ERROR"))
			{
				Log.d("Received", payload);
				LeaderboardActivity.handler.obtainMessage(LeaderboardActivity.DONE, payload).sendToTarget();
			}
		}
	}

}
