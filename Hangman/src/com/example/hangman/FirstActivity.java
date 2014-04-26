package com.example.hangman;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;

import android.view.View;

public class FirstActivity extends Activity {
	
	public final String LOCAL_ADDRESS = "10.66.140.21";
	public final String SERVER_ADDRESS = "54.186.235.124";
	public final int SERVER_PORT = 30000;
	
	public static DatagramSocket socket = null;
	public static InetSocketAddress serverSocketAddress;
	public static WorkerThread wt = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		if(socket == null)
		{
			try {
				socket = new DatagramSocket();
				serverSocketAddress = new InetSocketAddress(LOCAL_ADDRESS, SERVER_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	
	}


	public void playGame(View v)
	{
		startActivity(new Intent(this,MainActivity.class));
	}
	
	public void showLeaderboard(View v)
	{
		startActivity(new Intent(this,LeaderboardActivity.class));
	}

}
