package com.cellon.trafficstats.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.internal.telephony.MSimPhoneFactory;
import com.cellon.trafficstats.R;
import com.cellon.trafficstats.service.TrafficService;
import com.cellon.trafficstats.service.NetworkMonitorService;
import com.cellon.trafficstats.ui.ExceedDialog;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * @author Baron.Hu
 * get the power on broadcast and start the service
 * */
public class PowerOnBroadcastReveiver extends BroadcastReceiver {
	private NotificationManager mNm;
	private Context mContext;
	private SharedPreferences mSharedPreferences;
	private SimpleDateFormat sdf;
	public static final String EXCEED_ACTION = "com.cellon.trafficstats.EXCEED";
	public static final String SIM_CHANGED = "com.cellon.trafficstats.SIM_CHANGED";
	public static final String CLEAR = "com.cellon.trafficstats.CLEAR";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int sub = MSimPhoneFactory.getDataSubscription();
		Log.d("baron.hu", "sub=" + sub);
		init(context);
		Intent mintent = new Intent(context, NetworkMonitorService.class);
		Intent trafficIntent = new Intent(context, TrafficService.class);
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			context.startService(trafficIntent);
			context.startService(mintent);
		} else if (intent.getAction().equals(EXCEED_ACTION)) {
			showNotificationWhenExceed();
		} else if (intent.getAction().equals(CLEAR)) {
			context.stopService(trafficIntent);
		} else if (intent.getAction().equals(SIM_CHANGED)) {
			//start a new service of SIM
			//get the sub number, then start service
			if (sub != 0) {
				//start sub 1 and stop sub 0
//				context.stopService(trafficIntent);
			} else {
				//start sub 0
			}
		}
	}
	
	//show the notification when power on
//	public void showNotification() {
//		mNm = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
//		CharSequence text = mContext.getText(com.cellon.trafficstats.R.string.notification);
//		CharSequence title = mContext.getText(R.string.app_name);
//		Intent i = new Intent(mContext, Main.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//		Notification notification = 
//			new Notification(R.drawable.icon, text, System.currentTimeMillis());
//		PendingIntent intent = PendingIntent.getActivity(mContext, 0, i, 0);
//		notification.setLatestEventInfo(mContext, title, text, intent);
////		notification.defaults = Notification.DEFAULT_SOUND;
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//		mNm.notify(getResultCode(), notification);
//	}
	
	//show the notification when 3G traffic has exceed the max value
	public void showNotificationWhenExceed() {
		mNm = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
		CharSequence text = mContext.getText(com.cellon.trafficstats.R.string.traffc_exceed_notification);
		CharSequence title = mContext.getText(R.string.app_name);
		Intent i = new Intent(mContext, ExceedDialog.class);
		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		Notification notification = 
			new Notification(R.drawable.icon, text, System.currentTimeMillis());
		PendingIntent intent = PendingIntent.getActivity(mContext, 0, i, 0);
		notification.setLatestEventInfo(mContext, title, text, intent);
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		mNm.notify(getResultCode(), notification);
	}
	
	public void init(Context context) {
		mContext = context;		
		mSharedPreferences = context.getSharedPreferences("setting", Activity.MODE_WORLD_READABLE);
		sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String s = sdf.format(date);
		Editor editor = mSharedPreferences.edit();
		editor.putString("power_on_date", s);//save this power on date
		editor.commit();
	}
}
