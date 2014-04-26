package com.example.hangman;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.app.Activity;

public class LeaderboardActivity extends Activity {

	public static final int DONE = 3;
	public static final int LEADER = 4;
	
	public static MyLeaderboardHandler handler = null;
	
	private TreeMap<Long,String> leaders;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
		
		if(FirstActivity.wt == null)
		{
			FirstActivity.wt = new WorkerThread();
			FirstActivity.wt.start();
		}
		
		if(handler == null)
		{
			handler = new MyLeaderboardHandler(this);
		}
		
		leaders = new TreeMap<Long,String>();
		
		String command = "LEADERBOARD";
		
		sendToServer(command);
	}

	public void sendToServer(String message)
	{
		new AsyncTask<String,Void, Void>()
		{

			@Override
			protected Void doInBackground(String... args) {
				
				String msg = args[0];
				
				try {
					DatagramPacket txPacket = new DatagramPacket(msg.getBytes(), msg.length(), FirstActivity.serverSocketAddress);
					FirstActivity.socket.send(txPacket);
				}
				catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return null;
			}
		
		}.execute(message);
	}
	
	static class MyLeaderboardHandler extends Handler
 	{
 		private final WeakReference<LeaderboardActivity> mActivity;
 		
 		public MyLeaderboardHandler(LeaderboardActivity activity)
 		{
 			mActivity = new WeakReference<LeaderboardActivity>(activity);
 		}
 		@Override
 		public void handleMessage(Message msg)
 		{	
 			LeaderboardActivity activity = mActivity.get();
 			
 			if(activity != null)
 			{
 				switch(msg.what)
 				{
 				case LEADER:
 					activity.populateLeaders(false,(String)msg.obj);
 					break;
 				case DONE:
 					activity.populateLeaders(true,(String)msg.obj);
 					break;
 				}
 			}
 		}
 	}
	
	private void populateLeaders(boolean done,String msg)
	{
		if(!done)
		{
			String [] tokens = msg.split(": ");
			
			String name = tokens[1];
			String time = tokens[2];
			
			long time_ = Long.parseLong(time);
			
			time = String.format("%d min, %d sec", 
				    TimeUnit.MILLISECONDS.toMinutes(time_),
				    TimeUnit.MILLISECONDS.toSeconds(time_) - 
				    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_))
				);
			
			leaders.put(time_,name);
			Log.d("Leaders", "Name: " + name + " Time: " + time);
		    
			
			String command = "LEADERBOARD";
			sendToServer(command);
		}
		else
		{
			showLeaders();
		}
	}
	
	private void showLeaders()
	{
		int count = 1;
		for (Map.Entry<Long, String> entry : leaders.entrySet())
		{
			String name = entry.getValue();
			String time;
			long time_ = entry.getKey();
			
			time = String.format("%d min, %d sec", 
				    TimeUnit.MILLISECONDS.toMinutes(time_),
				    TimeUnit.MILLISECONDS.toSeconds(time_) - 
				    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_))
				);
		    if(count == 1)
		    {
		    	((TextView)findViewById(R.id.textView1_1)).setText(name);
		    	((TextView)findViewById(R.id.textView1_2)).setText(time);
		    	count++;
		    }
		    else if(count == 2)
		    {
		    	((TextView)findViewById(R.id.textView2_1)).setText(name);
		    	((TextView)findViewById(R.id.textView2_2)).setText(time);
		    	count++;
		    }
		    else if(count == 3)
		    {
		    	((TextView)findViewById(R.id.textView3_1)).setText(name);
		    	((TextView)findViewById(R.id.textView3_2)).setText(time);
		    	count++;
		    }
		    else if(count == 4)
		    {
		    	((TextView)findViewById(R.id.textView4_1)).setText(name);
		    	((TextView)findViewById(R.id.textView4_2)).setText(time);
		    	count++;
		    }
		    else if(count == 5)
		    {
		    	((TextView)findViewById(R.id.textView5_1)).setText(name);
		    	((TextView)findViewById(R.id.textView5_2)).setText(time);
		    	count++;
		    }
		    else if(count == 6)
		    {
		    	((TextView)findViewById(R.id.textView6_1)).setText(name);
		    	((TextView)findViewById(R.id.textView6_2)).setText(time);
		    	count++;
		    }
		    else if(count == 7)
		    {
		    	((TextView)findViewById(R.id.textView7_1)).setText(name);
		    	((TextView)findViewById(R.id.textView7_2)).setText(time);
		    	count++;
		    }
		    else if(count == 8)
		    {
		    	((TextView)findViewById(R.id.textView8_1)).setText(name);
		    	((TextView)findViewById(R.id.textView8_2)).setText(time);
		    	count++;
		    }
		    else if(count == 9)
		    {
		    	((TextView)findViewById(R.id.textView9_1)).setText(name);
		    	((TextView)findViewById(R.id.textView9_2)).setText(time);
		    	count++;
		    }
		    else if(count == 10)
		    {
		    	((TextView)findViewById(R.id.textView10_1)).setText(name);
		    	((TextView)findViewById(R.id.textView10_2)).setText(time);
		    	count++;
		    }
		    else
		    {
		    	break;
		    }
		}
	}
	
	

}
