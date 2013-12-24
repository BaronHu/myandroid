package com.cellon.ows;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CallLog.Calls;
import android.telephony.MSimTelephonyManager;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class OwsTool {
	
	public static final String CALL_TIME_LIMITE_ACTION      = "com.cellon.ows.CALL_TIME_LIMITE";
	public static final String STANDBY_TIME_LIMITE_ACTION   = "com.cellon.ows.STANDBY_TIME_LIMITE";
	public static final String SMS_SENT_SUCCESSFULLY_ACTION = "com.cellon.ows.SMS_SENT_SUCCESSFULLY";
	public static final String SMS_SENT_OK_ACTION           = "com.cellon.ows.SMS_SENT_OK";
	public static final String SMS_SENT_TEST_ACTION         = "com.cellon.ows.SMS_SENT_TEST";
	public static final String SHUTDOWN_ACTION              = "android.intent.action.ACTION_SHUTDOWN";
	public static final String CALL_TIME_CHANGED_ACTION     = "com.cellon.ows.CALL_TIME_CHANGED";
	public static final String STANDBY_TIME_CHANGED_ACTION  = "com.cellon.ows.STANDBY_TIME_CHANGED";
	
	private Context mContext;
	private SharedPreferences mGateway;
	private String gateway;

	public OwsTool(Context context) {
		this.mContext = context;
		mGateway = PreferenceManager.getDefaultSharedPreferences(mContext);
		gateway = mGateway.getString("gateway", "8009");
	}

	public String getIEMI() {
		String iemi = null;
		//TelephonyManager tm = TelephonyManager.getDefault();
		if (TelephonyManager.isMultiSimEnabled()) {
			MSimTelephonyManager mtm = MSimTelephonyManager.getDefault();
			iemi = mtm.getDeviceId(0);
//			int phonecount = mtm.getPhoneCount();
//			int[] type = new int[phonecount];
//			for (int i=0; i<phonecount; i++) {
//                    // C+G mode
//                Log.d("Ows",i+",,,imei==" + mtm.getDeviceId(i));
//            }
		}else{
			TelephonyManager tem = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
			iemi = tem.getDeviceId();
		}
		
		return iemi;
	}

	public String getModel() {
		return android.os.Build.MODEL; 
	}
	
	public int getLifeTime() {
		//get from NV
		return 0;
	}
	
	public int getCallLimite() {
		//get from NV
		return 0;
	}

	public long getCallDuration() {
		long duration = 0;
		Cursor c = mContext.getContentResolver().query(Calls.CONTENT_URI,
				new String[] { Calls.DURATION }, null, null, Calls._ID + " desc");
		if (c != null) {
			if (c.moveToFirst()) {
				duration = c.getLong(c.getColumnIndex(Calls.DURATION));
				Log.d("Ows", "duration=" + duration);
			}
		}
		if (c != null) {
			c.close();
		}
		
		return duration;
	}
	
	public String composeMsg(String s) {
		return "Q-mobile dkbh " + getIEMI() + " " + getModel() + " " + s;
	}
	
	public void sendServiceSms(String content) {
		try {
			SmsManager manager = SmsManager.getDefault();
			PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, 
					new Intent(SMS_SENT_TEST_ACTION), 0);
			manager.sendTextMessage("8009", null, content + getIEMI(), sentIntent, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendSms(final String content, final int a) {
		SmsManager manager = SmsManager.getDefault();
    	PendingIntent sentIntent = null;
    	if (a == 0) {
    		sentIntent = PendingIntent.getBroadcast(mContext, 0,
					new Intent(SMS_SENT_OK_ACTION), 0);
    	} else {
    		sentIntent = PendingIntent.getBroadcast(mContext, 0,
					new Intent(SMS_SENT_SUCCESSFULLY_ACTION), 0);
    	}
		manager.sendTextMessage(gateway, null, content, sentIntent, null);
	}
	
	public static boolean isNum(String str) {
    	String regex = "[^0-9]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			return true;
		}
		return false;
    }

}
