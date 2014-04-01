package com.example.tictactoe;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final String LOCAL_ADDRESS = "10.67.84.219";
	public final String SERVER_ADDRESS = "54.186.235.124";
	public final int SERVER_PORT = 20000;
	
	private Button newGameButton;
	private Button pos0_0;
	private Button pos0_1;
	private Button pos0_2;
	private Button pos1_0;
	private Button pos1_1;
	private Button pos1_2;
	private Button pos2_0;
	private Button pos2_1;
	private Button pos2_2;
	private TextView statusText;
	private boolean player1_turn;
	private boolean gameOver;
	private boolean draw;
	private int buttonClicks;
	public static final int PUT_LETTER = 1;
	public static final int REGISTERED = 2;
	
	private int clientID = -1;
	private String groupName;
	
	public static MyHandler handler = null;
	
	public static DatagramSocket socket = null;
	public static InetSocketAddress serverSocketAddress;
	public static WorkerThread wt = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		newGameButton = (Button)findViewById(R.id.newGameButton);
		pos0_0 = (Button)findViewById(R.id.button00);
		pos0_1 = (Button)findViewById(R.id.button01);
		pos0_2 = (Button)findViewById(R.id.button02);
		pos1_0 = (Button)findViewById(R.id.button10);
		pos1_1 = (Button)findViewById(R.id.button11);
		pos1_2 = (Button)findViewById(R.id.button12);
		pos2_0 = (Button)findViewById(R.id.button20);
		pos2_1 = (Button)findViewById(R.id.button21);
		pos2_2 = (Button)findViewById(R.id.button22);
		statusText = (TextView)findViewById(R.id.statusText);
		player1_turn = true;
		gameOver = false;
		draw = false;
		buttonClicks = 0;
		
		if(socket == null)
		{
			try {
				socket = new DatagramSocket();
				serverSocketAddress = new InetSocketAddress(LOCAL_ADDRESS, SERVER_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		if(handler == null)
		{
			handler = new MyHandler(this);
		}
		
		if(wt == null)
		{
			wt = new WorkerThread();
			wt.start();
		}
		
		if(clientID == -1)
		{
			String command = "REGISTER";
			sendToServer(command);
		}
	}

	static class MyHandler extends Handler
 	{
 		private final WeakReference<MainActivity> mActivity;
 		
 		public MyHandler(MainActivity activity)
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
 				case PUT_LETTER:
 					activity.putLetter((String)msg.obj);
 					break;
 				case REGISTERED:
 					activity.setID((String)msg.obj);
 					break;
 				}
 			}
 		}
 	}
	
	public void setID(String message)
	{
		String [] tokens = message.split(":");
		
		clientID = Integer.parseInt(tokens[1]);
		
		Toast.makeText(this, "Client ID is: " + clientID, Toast.LENGTH_SHORT).show();
	}
	
	public void sendToServer(String message)
	{
		new AsyncTask<String,Void, Void>()
		{

			@Override
			protected Void doInBackground(String... args) {
				
				String msg = args[0];
				
				try {
					DatagramPacket txPacket = new DatagramPacket(msg.getBytes(), msg.length(), serverSocketAddress);
					socket.send(txPacket);
				}
				catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return null;
			}
			
		}.execute(message);
	}
	
	
	public void putLetter(String msg)
	{
		String [] tokens = msg.split(":");
		
		String [] tokens2 = tokens[1].split(" ");
		
		String pos = tokens2[0];
		String letter = tokens2[1];
		
		if(pos == "00")
		{
			if(pos0_0.getText() == "")
			{
				pos0_0.setText(letter);
				pos0_0.setClickable(false);
			}
		}
		else if(pos == "01")
		{
			if(pos0_1.getText() == "")
			{
				pos0_1.setText(letter);
				pos0_1.setClickable(false);
			}
		}
		else if(pos == "02")
		{
			if(pos0_2.getText() == "")
			{
				pos0_2.setText(letter);
				pos0_2.setClickable(false);
			}
		}
		else if(pos == "10")
		{
			if(pos1_0.getText() == "")
			{
				pos1_0.setText(letter);
				pos1_0.setClickable(false);
			}
		}
		else if(pos == "11")
		{
			if(pos1_1.getText() == "")
			{
				pos1_1.setText(letter);
				pos1_1.setClickable(false);
			}
		}
		else if(pos == "12")
		{
			if(pos1_2.getText() == "")
			{
				pos1_2.setText(letter);
				pos1_2.setClickable(false);
			}
		}
		else if(pos == "20")
		{
			if(pos2_0.getText() == "")
			{
				pos2_0.setText(letter);
				pos2_0.setClickable(false);
			}
		}
		else if(pos == "21")
		{
			if(pos2_1.getText() == "")
			{
				pos2_1.setText(letter);
				pos2_1.setClickable(false);
			}
		}
		else if(pos == "22")
		{
			if(pos2_2.getText() == "")
			{
				pos2_2.setText(letter);
				pos2_2.setClickable(false);
			}
		}
	}
	public void newGame(View v)
	{
		groupNamePrompt();
		
		newGameButton.setEnabled(false);
		pos0_0.setEnabled(true);
		pos0_0.setClickable(true);
		pos0_0.setText("");
		pos0_1.setEnabled(true);
		pos0_1.setClickable(true);
		pos0_1.setText("");
		pos0_2.setEnabled(true);
		pos0_2.setClickable(true);
		pos0_2.setText("");
		pos1_0.setEnabled(true);
		pos1_0.setClickable(true);
		pos1_0.setText("");
		pos1_1.setEnabled(true);
		pos1_1.setClickable(true);
		pos1_1.setText("");
		pos1_2.setEnabled(true);
		pos1_2.setClickable(true);
		pos1_2.setText("");
		pos2_0.setEnabled(true);
		pos2_0.setClickable(true);
		pos2_0.setText("");
		pos2_1.setEnabled(true);
		pos2_1.setClickable(true);
		pos2_1.setText("");
		pos2_2.setEnabled(true);
		pos2_2.setClickable(true);
		pos2_2.setText("");
		
		player1_turn = true;
		setGameStatus();
	}
	
	public void groupNamePrompt()
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
								String groupname = input.getText().toString();
								
								String message = "JOIN " + clientID + " " + groupname + "\n";
								
								sendToServer(message);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alertD = alertDialogBuilder.create();

		alertD.show();
	}
	public void setGameStatus()
	{
		if(gameOver)
		{
			String winner;
			if(player1_turn)
				winner = "Player 1 Wins!";
			else
				winner = "Player 2 Wins!";
			
			if(draw)
				winner = "Draw!";
			
			statusText.setText("GAME OVER!! " + winner);
			newGameButton.setEnabled(true);
			pos0_0.setEnabled(false);
			pos0_1.setEnabled(false);
			pos0_2.setEnabled(false);
			pos1_0.setEnabled(false);
			pos1_1.setEnabled(false);
			pos1_2.setEnabled(false);
			pos2_0.setEnabled(false);
			pos2_1.setEnabled(false);
			pos2_2.setEnabled(false);
			
			buttonClicks = 0;
			draw = false;
			gameOver = !gameOver;
		}
		else if (player1_turn)
		{
			statusText.setText("Player 1's turn");
		}
		else
		{
			statusText.setText("Player 2's turn");
		}
	}
	
	public void gameBoardPressed(View v)
	{
		buttonClicks++;
		
		String letter;
		String message = "SEND " + clientID + " " + groupName + " ";
		
		if(player1_turn)
		{
			letter = "X";
		}
		else
		{
			letter = "O";
		}
		
		if(v.getId() == pos0_0.getId())
		{
			pos0_0.setText(letter);
			message += "00 " + letter + "\n";
			pos0_0.setClickable(false);
		}
		else if(v.getId() == pos0_1.getId())
		{
			pos0_1.setText(letter);
			message += "01 " + letter + "\n";
			pos0_1.setClickable(false);
		}
		else if(v.getId() == pos0_2.getId())
		{
			pos0_2.setText(letter);
			message += "02 " + letter + "\n";
			pos0_2.setClickable(false);
		}
		else if(v.getId() == pos1_0.getId())
		{
			pos1_0.setText(letter);
			message += "10 " + letter + "\n";
			pos1_0.setClickable(false);
		}
		else if(v.getId() == pos1_1.getId())
		{
			pos1_1.setText(letter);
			message += "11 " + letter + "\n";
			pos1_1.setClickable(false);
		}
		else if(v.getId() == pos1_2.getId())
		{
			pos1_2.setText(letter);
			message += "12 " + letter + "\n";
			pos1_2.setClickable(false);
		}
		else if(v.getId() == pos2_0.getId())
		{
			pos2_0.setText(letter);
			message += "20 " + letter + "\n";
			pos2_0.setClickable(false);
		}
		else if(v.getId() == pos2_1.getId())
		{
			pos2_1.setText(letter);
			message += "21 " + letter + "\n";
			pos2_1.setClickable(false);
		}
		else if(v.getId() == pos2_2.getId())
		{
			pos2_2.setText(letter);
			message += "22 " + letter + "\n";
			pos2_2.setClickable(false);
		}
		
		if(gameOver())
		{
			gameOver = true;
			setGameStatus();
			return;
		}
		
		sendToServer(message);
		player1_turn = !player1_turn;
		setGameStatus();
	}
	
	public boolean gameOver()
	{
		if(pos0_0.getText() == pos0_1.getText() && pos0_1.getText() == pos0_2.getText() && pos0_0.getText() != "")
		{
			return true;
		}
		else if(pos1_0.getText() == pos1_1.getText() && pos1_1.getText() == pos1_2.getText() && pos1_0.getText() != "")
		{
			return true;
		}
		else if(pos2_0.getText() == pos2_1.getText() && pos2_1.getText() == pos2_2.getText() && pos2_0.getText() != "")
		{
			return true;
		}
		else if(pos0_0.getText() == pos1_0.getText() && pos1_0.getText() == pos2_0.getText() && pos0_0.getText() != "")
		{
			return true;
		}
		else if(pos0_1.getText() == pos1_1.getText() && pos1_1.getText() == pos2_1.getText() && pos0_1.getText() != "")
		{
			return true;
		}
		else if(pos0_2.getText() == pos1_2.getText() && pos1_2.getText() == pos2_2.getText() && pos0_2.getText() != "")
		{
			return true;
		}
		else if(pos0_0.getText() == pos1_1.getText() && pos1_1.getText() == pos2_2.getText() && pos0_0.getText() != "")
		{
			return true;
		}
		else if(pos0_2.getText() == pos1_1.getText() && pos1_1.getText() == pos2_0.getText() && pos0_2.getText() != "")
		{
			return true;
		}
		else if(buttonClicks == 9)
		{
			draw = true;
			return true;
		}
		
		return false;
	}
}
