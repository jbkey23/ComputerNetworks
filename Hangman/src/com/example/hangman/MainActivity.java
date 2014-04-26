package com.example.hangman;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.util.concurrent.TimeUnit;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	
	LinearLayout letters;
	TextView[] letterChars;
	ImageView[] bodyParts;
	
	int curPart;
	int numParts = 6;
	int numLetters;
	int numLettersCorrect;
	String curWord;
	
	int wordsSolved;
	
	long startTime;
	long endTime;
	long totalTime;
	
	int clientID = -1;
	
	//private static final String groupName = "hangman";
	
	public static final int REGISTERED = 1;
	public static final int WORD = 2;
	public static final int JOINED = 3;

	
	public static MyMainHandler handler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bodyParts = new ImageView[numParts];
		bodyParts[0] = (ImageView)findViewById(R.id.head);
		bodyParts[1] = (ImageView)findViewById(R.id.body);
		bodyParts[2] = (ImageView)findViewById(R.id.arm1);
		bodyParts[3] = (ImageView)findViewById(R.id.arm2);
		bodyParts[4] = (ImageView)findViewById(R.id.leg1);
		bodyParts[5] = (ImageView)findViewById(R.id.leg2);
		
		letters = (LinearLayout)findViewById(R.id.Letters);
		
		if(FirstActivity.wt == null)
		{
			FirstActivity.wt = new WorkerThread();
			FirstActivity.wt.start();
		}
		
		if(handler == null)
		{
			handler = new MyMainHandler(this);
		}
	 
		if(clientID == -1)
		{
			String command = "REGISTER";
			sendToServer(command);
		}
		
		wordsSolved = 0;
		
		for(int i = 0; i < numParts; i++)
		{
			bodyParts[i].setVisibility(View.INVISIBLE);
		}
	 
		startTime = System.currentTimeMillis();
	}

	static class MyMainHandler extends Handler
 	{
 		private final WeakReference<MainActivity> mActivity;
 		
 		public MyMainHandler(MainActivity activity)
 		{
 			mActivity = new WeakReference<MainActivity>(activity);
 		}
 		@Override
 		public void handleMessage(Message msg)
 		{	
 			MainActivity activity = mActivity.get();
 			
 			if(activity != null)
 			{
 				switch(msg.what)
 				{
 				case REGISTERED:
 					activity.setID((String)msg.obj);
 					break;
 				case WORD:
 					activity.setWord((String)msg.obj);
 					break;
 				}
 			}
 		}
 	}
	
 
	public void setWord(String message)
	{
		String [] tokens = message.split(":");
		
		curWord = tokens[1].trim().toUpperCase();
		
		startGame();
	}
	
	
	public void setID(String message)
	{
		String [] tokens = message.split(":");
		
		clientID = Integer.parseInt(tokens[1].trim());
		
		Toast.makeText(this, "Client ID is: " + clientID, Toast.LENGTH_SHORT).show();
		
		getWord();
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
	
	private void getWord()
	{
		String message = "START";
		sendToServer(message);
	}
	
	private void startGame()
	{
		
		Log.d("Started", "The current word is: " + curWord);
		
		letterChars = new TextView[curWord.length()];
		
		letters.removeAllViews();
		
		for(int i = 0; i<curWord.length(); i++)
		{
			letterChars[i] = new TextView(this);
			letterChars[i].setText("" + curWord.charAt(i));
			
			letterChars[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			letterChars[i].setGravity(Gravity.CENTER);
			letterChars[i].setTextColor(Color.WHITE);
			letterChars[i].setBackgroundResource(R.drawable.letter_bg);
			
			letters.addView(letterChars[i]);
		}
		
		curPart = 0;
		numLetters = curWord.length();
		numLettersCorrect = 0;
		
		for(int i = 0; i < numParts; i++)
		{
			bodyParts[i].setVisibility(View.INVISIBLE);
		}
		
		
	}
	
	public void letterPressed(View v)
	{
		String l = ((Button)v).getText().toString();
		char letter = l.charAt(0);
		
		v.setEnabled(false);
		
		boolean correct = false;
		for(int i = 0; i < curWord.length(); i++)
		{
			if(curWord.charAt(i)==letter)
			{
				correct = true;
				numLettersCorrect++;
				letterChars[i].setTextColor(Color.BLACK);
			}
		}
		
		if(correct)
		{
			if(numLettersCorrect == numLetters)
			{
				setLetterStatus(true);
				wordsSolved++;
				displayAlert(true);
			}
		}
		else if(curPart < numParts)
		{
			bodyParts[curPart].setVisibility(View.VISIBLE);
			curPart++;
		}
		else
		{
			setLetterStatus(false);
			displayAlert(false);
		}
		
	}
	
	
	public void displayAlert(boolean won)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String title = "";
		String message = "";
	   
		if(won && wordsSolved == 3)
		{
			title = "You won the game!!";
			
			builder.setPositiveButton("Go to Home Screen",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.this.namePrompt();
						}
					});
			
			endTime = System.currentTimeMillis();
			totalTime = (endTime - startTime);
			
			String time = String.format("%d min, %d sec", 
				    TimeUnit.MILLISECONDS.toMinutes(totalTime),
				    TimeUnit.MILLISECONDS.toSeconds(totalTime) - 
				    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
				);
			
			//Toast.makeText(this, String.valueOf(totalTime), Toast.LENGTH_SHORT).show();
			message = "You have solved all 10 words in " + time + "!\nCheck the leaderboard to see if you made it!";
		}
		else if (won)
		{
			title = "Congrats!";
			message = "You solved the word successfully!\nClick below to go to next word.";
			builder.setPositiveButton("Next Word",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MainActivity.this.getWord();
						}
					});
		}
		else
		{
			title = "Sorry";
			message = "You did not solve the word.\nThe word was " + curWord;
			builder.setPositiveButton("Go to Home Screen",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String command = "FAIL";
							MainActivity.this.sendToServer(command);
							MainActivity.this.finish();
						}
					});
		}
		
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}
	
	public void namePrompt()
	{
		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(this);

		View promptView = layoutInflater.inflate(R.layout.prompt, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder.setView(promptView);

		final EditText input = (EditText) promptView.findViewById(R.id.userInput);

		// setup a dialog window
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result
								
								String name = input.getText().toString();
								
								String message = "SUCCESS " + totalTime + " " + name +  "\n";
								
								sendToServer(message);
								
								MainActivity.this.finish();
							}
						});
				/*.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) {
								buttonCancelled = true;
								dialog.cancel();
							}
						});*/

		// create an alert dialog
		AlertDialog alertD = alertDialogBuilder.create();

		alertD.show();
	}
	
	public void setLetterStatus(boolean status)
	{
		((Button)findViewById(R.id.a)).setEnabled(status);
		((Button)findViewById(R.id.b)).setEnabled(status);
		((Button)findViewById(R.id.c)).setEnabled(status);
		((Button)findViewById(R.id.d)).setEnabled(status);
		((Button)findViewById(R.id.e)).setEnabled(status);
		((Button)findViewById(R.id.f)).setEnabled(status);
		((Button)findViewById(R.id.g)).setEnabled(status);
		((Button)findViewById(R.id.h)).setEnabled(status);
		((Button)findViewById(R.id.i)).setEnabled(status);
		((Button)findViewById(R.id.j)).setEnabled(status);
		((Button)findViewById(R.id.k)).setEnabled(status);
		((Button)findViewById(R.id.l)).setEnabled(status);
		((Button)findViewById(R.id.m)).setEnabled(status);
		((Button)findViewById(R.id.n)).setEnabled(status);
		((Button)findViewById(R.id.o)).setEnabled(status);
		((Button)findViewById(R.id.p)).setEnabled(status);
		((Button)findViewById(R.id.q)).setEnabled(status);
		((Button)findViewById(R.id.r)).setEnabled(status);
		((Button)findViewById(R.id.s)).setEnabled(status);
		((Button)findViewById(R.id.t)).setEnabled(status);
		((Button)findViewById(R.id.u)).setEnabled(status);
		((Button)findViewById(R.id.v)).setEnabled(status);
		((Button)findViewById(R.id.w)).setEnabled(status);
		((Button)findViewById(R.id.x)).setEnabled(status);
		((Button)findViewById(R.id.y)).setEnabled(status);
		((Button)findViewById(R.id.z)).setEnabled(status);
	}
}
