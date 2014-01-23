package com.example.myfirstapp;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class MainActivity extends Activity {
	
	private final String TAG = this.getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG, "onCreate has been called");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy has been called");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause has been called");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "onRestart has been called");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume has been called");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart has been called");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop has been called");
	}



}
