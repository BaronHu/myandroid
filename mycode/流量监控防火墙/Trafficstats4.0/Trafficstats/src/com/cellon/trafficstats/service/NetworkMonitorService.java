package com.cellon.trafficstats.service;

import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Baron.Hu
 * */
public class NetworkMonitorService extends Service {

	public static final String PREF_NAME = "setting";
	
	private Context mContext;
	private boolean bool = false;
	private ConnectivityManager connManager;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mSharedPreferences = mContext.getSharedPreferences(PREF_NAME,Activity.MODE_WORLD_READABLE);
		editor = mSharedPreferences.edit();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("baron.hu", "NetworkMonitorService destroyed...");
		handler.removeCallbacks(task);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		handler.postDelayed(task, 0);
	}
	
	public static boolean isNetworkAvailable(Context mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	android.os.Handler handler = new android.os.Handler();
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			if (!isNetworkAvailable(mContext)) {
				if (!bool) {
					Intent trafficIntent = new Intent(mContext, TrafficService.class);
					mContext.stopService(trafficIntent);
					bool = true;
				}
			} else {
				if (bool) {
					Intent trafficIntent2 = new Intent(mContext, TrafficService.class);
					mContext.startService(trafficIntent2);
					bool = false;
				}
			}
			setDataEnabled();
			handler.postDelayed(task, 5000);
		}
	};
	
	public void setDataEnabled() {
		connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager.getMobileDataEnabled()) {
			editor.putBoolean("close_data_network", true);
		} else {
			editor.putBoolean("close_data_network", false);
		}
		editor.commit();
	}

}
