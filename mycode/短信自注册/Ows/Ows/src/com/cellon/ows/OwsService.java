package com.cellon.ows;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.MSimTelephonyManager;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.PowerManager;

import com.android.qualcomm.qcnvitems.QcNvItems;
import com.cellon.ows.OwsDialog;

public class OwsService extends Service {
	
	private static final String TAG = "Ows";
	private static final boolean DEBUG = false;

	private Context mContext;
	private long i;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private QcNvItems mQcNvItems;
	private long prei;
	private String mStandbyTime;
	private long standby;
	private int mSub;
	private boolean hasService = false;
	private int CYCLE = 1;
	
//	private PowerManager.WakeLock sCpuWakeLock;
	
	private PhoneStateListener[] mPhoneStateListener;
	private TelephonyManager mPhone;
	
	private AlarmManager mAlarmManager;
	private PendingIntent mPendingIntent;
	
	private boolean hasSendSms = false;
	
//	private Handler handler = new Handler();
//
//	private Runnable mTask = new Runnable() {
//
//		@Override
//		public void run() {
//			if (hasService) {
//				if (prei > 0) {
//					i += prei;
//					prei = 0;
//				}
//				i += (30 * 60);
//				if (DEBUG) Log.d(TAG, "OwsService-->i=" + i);
//				if (i >= standby && !hasSendSms) {
//					unregisterPhoneListener();
//					new OwsDialog(mContext);
//					hasSendSms = true;
//					editor.putBoolean("standby_time_limite_fulfilled", true);
//				}
//				editor.putLong("standby_time_limite_value", i);
//				editor.commit();
//				
//				try {
//					mQcNvItems.setLifeTime(i);//total standby time
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			handler.postDelayed(this, 30 * 60 * 1000);
//		}
//
//	};

	private PhoneStateListener getPhoneStateListener(final int sub) {
		PhoneStateListener sPhoneStateListener = new PhoneStateListener(sub) {
			@Override
	        public void onServiceStateChanged(ServiceState serviceState) {
	            // TODO Auto-generated method stub
	            if (serviceState != null) {
	            	if (DEBUG) Log.d(TAG, "OwsService-->" + sub + ",,state==" + serviceState.getState());
	                if (serviceState.getState() == ServiceState.STATE_IN_SERVICE) {
	                	hasService = true;
	                	if (DEBUG) Log.d(TAG, "OwsService-->no service or SIM is valid..." + sub);
	                } else {
	                	hasService = false;
	                	Log.d(TAG, "OwsService-->no service or SIM is invalid..." + sub);
	                }
	            } else {
	            	if (DEBUG) Log.d(TAG, "no service state...may be the balance is not enough" + sub);
	            }
	            super.onServiceStateChanged(serviceState);
	        }
		};
		return sPhoneStateListener;
	}
	
	private void registerPhoneListener() {
		mSub = MSimTelephonyManager.getDefault().getPhoneCount();
		mPhoneStateListener = new PhoneStateListener[mSub];
		for (int i = 0; i < mSub; i++) {
            mPhoneStateListener[i] = getPhoneStateListener(i);
            mPhone.listen(mPhoneStateListener[i], PhoneStateListener.LISTEN_SERVICE_STATE);
        }
	}
	
	private void unregisterPhoneListener() {
		mSub = MSimTelephonyManager.getDefault().getPhoneCount();
//		mPhoneStateListener = new PhoneStateListener[mSub];
		for (int i = 0; i < mSub; i++) {
//            mPhoneStateListener[i] = getPhoneStateListener(i);
			if(mPhoneStateListener[i] != null){
				mPhone.listen(mPhoneStateListener[i], PhoneStateListener.LISTEN_NONE);
			}
        }
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
//		acquireCpuWakeLock(this);
		IntentFilter filter = new IntentFilter();
        filter.addAction(OwsTool.SHUTDOWN_ACTION);
        registerReceiver(shutdown, filter);
        
        registerReceiver(mAlarmReceiver, new IntentFilter("com.cellon.alarm_time_out"));
        
		sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		editor = sp.edit();
		mStandbyTime = sp.getString("standby_time_limit", "7200");
		if (mStandbyTime != null) {
			try {
				standby = Long.parseLong(mStandbyTime) * 60;
				if (standby < 30 * 60) {
					CYCLE = 1;
				} else {
					CYCLE = 30;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (DEBUG) Log.d("Ows", "CYCLE=" + CYCLE);
		mQcNvItems = new QcNvItems();
		try {
			prei = mQcNvItems.getLifeTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (TelephonyManager.getDefault().isMultiSimEnabled()) {
			mPhone = (MSimTelephonyManager) mContext
					.getSystemService(Context.MSIM_TELEPHONY_SERVICE);
		} else {
			mPhone = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		
		registerPhoneListener();
		if (DEBUG) Log.d(TAG, "OwsService-->OnCreate()...");
		
		mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("com.cellon.alarm_time_out"), 0);
        mAlarmManager.setRepeating(AlarmManager.RTC, 0, CYCLE * 60 * 1000, mPendingIntent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(shutdown);
		unregisterReceiver(mAlarmReceiver);
		mAlarmManager.cancel(mPendingIntent);
		//if the service was killed, we should write the lifetime to NV
		try {
			mQcNvItems.setLifeTime(i);//total standby time
		} catch (Exception e) {
			e.printStackTrace();
		}
//		handler.removeCallbacks(mTask);
//        releaseCpuLock();
        if (DEBUG) Log.d(TAG, "OwsService has destroyed...");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		hasSendSms = false;
//		handler.postDelayed(mTask, 0);
		if (DEBUG) Log.d("baron.hu", "OwsService-->onStart()...");
	}
	
	private BroadcastReceiver shutdown = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(OwsTool.SHUTDOWN_ACTION)) {
				try {
					mQcNvItems.setLifeTime(i);//total standby time
					if (DEBUG) Log.d(TAG, "OwsService-->phone shutdown......");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	
//	private void acquireCpuWakeLock(Context context) {
//        if (sCpuWakeLock != null) {
//            return;
//        }
//        PowerManager pm =
//            (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        sCpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        sCpuWakeLock.acquire();
//    }
//
//    private void releaseCpuLock() {
//        if (sCpuWakeLock != null) {
//            sCpuWakeLock.release();
//            sCpuWakeLock = null;
//        }
//    }
    
	
	private BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (hasService) {
				if (prei > 0) {
					i += prei;
					prei = 0;
				}
				i += (CYCLE * 60);
				if (DEBUG)
					Log.d(TAG, "OwsService-->i=" + i);
				if (i >= standby && !hasSendSms) {
					unregisterPhoneListener();
					new OwsDialog(mContext);
					hasSendSms = true;
				}

				try {
					mQcNvItems.setLifeTime(i);// total standby time
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	};

}
