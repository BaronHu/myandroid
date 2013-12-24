package com.example.androidtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PowerOnBroadcastReceiver extends BroadcastReceiver {
	
	public static final String TAG = "PowerOnBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d("baron", TAG + ":action=" + action);
		if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			Intent serviceIntent = new Intent(context, ReceiveService.class);
			context.startService(serviceIntent);
			Log.d("baron", "ReceiveService started from " + TAG);
		}
	}

}
