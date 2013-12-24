package com.unistrong.barcodetest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class ReadService extends IntentService {
	
	public static final String LOG_TAG = "ReadService";
	
	private Context mContext;
	private boolean bool = true;
	
	Handler handler = new Handler();

	public ReadService() {
		super("ReadService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while (bool) {
			Message msg = handler.obtainMessage(1);
			if (msg.what == 100) return;
			synchronized (this) {
				try {
					//Thread.sleep(200);
					String str = BarcodeNative.get(3);
					Log.d(LOG_TAG, "str======" + str);					
					if (str != null) {
						FileHelper.write(str, mContext);
					} 
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}//end while(bool)
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		Log.d(LOG_TAG, "ReadService--->>>onCreate()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		bool = false;
		Message msg = new Message();
		msg.what = 100;
		handler.sendMessage(msg);
		Log.d(LOG_TAG, "ReadService--->>>onDestroy()");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(LOG_TAG, "ReadService--->>>onStart()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "ReadService--->>>onStartCommand()");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		super.setIntentRedelivery(enabled);
		Log.d(LOG_TAG, "ReadService--->>>setIntentRedelivery() enabled=" + enabled);
	}

}
