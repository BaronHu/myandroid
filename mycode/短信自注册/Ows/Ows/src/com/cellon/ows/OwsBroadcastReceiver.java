package com.cellon.ows;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.cellonflex.CellonFlex;
import com.android.qualcomm.qcnvitems.QcNvItems;

public class OwsBroadcastReceiver extends BroadcastReceiver {

	private OwsTool tool;
	private boolean mSwitch;// flex control
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private int reg_state_calltime;// state of condition 1
	private int reg_state_standbytime;// state of condition 2
	private QcNvItems mQcNvItems;
	private String mCallDuration;// total call time , default is 15 min
	private int call_time_limit;// min time of call
	private int send_times_call;//repeat sending times when failed
	private int send_times_standby;
	private int i,j;

	@Override
	public void onReceive(Context context, Intent intent) {
		mSwitch = CellonFlex.getBoolean("mms",
				"FLEX_ID_SMS_AUTO_REGISTER_SWITCH", true);
		int sendSmsResult = getResultCode();
		Log.d("Ows", "sendSmsResult=" + sendSmsResult + "; rightCode=" + Activity.RESULT_OK
				+ "; mSwitch=" + mSwitch);
		if (!mSwitch) {
			mQcNvItems = new QcNvItems();

			String action = intent.getAction();
			Intent owsService = new Intent(context, OwsService.class);
			tool = new OwsTool(context);

			sp = PreferenceManager.getDefaultSharedPreferences(context);
			mCallDuration = sp.getString("call_time_limit", "15");
			if (mCallDuration != null) {
				try {
					call_time_limit = Integer.parseInt(mCallDuration) * 60;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			editor = sp.edit();
			send_times_call = sp.getInt("send_times_call", 0);
			send_times_standby = sp.getInt("send_times_standby", 0);

			if (action.equals(OwsTool.SMS_SENT_OK_ACTION)) {
				if (sendSmsResult == Activity.RESULT_OK) {
					try {
						mQcNvItems.setRegStatus(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					//send again
					i++;
					editor.putInt("send_times_call", i);
					editor.apply();
					if ((send_times_call % 3) == 0) {
						try {
							Thread.sleep(60 * 60 * 1000);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					String s = tool.composeMsg("2");
					tool.sendSms(s, 0);
				}
				
			}

			if (action.equals(OwsTool.SMS_SENT_SUCCESSFULLY_ACTION)) {
				if (sendSmsResult == Activity.RESULT_OK) {
					context.stopService(owsService);
					try {
						mQcNvItems.setStateOfStandby(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					j++;
					editor.putInt("send_times_standby", j);
					editor.apply();
					if ((send_times_standby % 3) == 0) {
						try {
							Thread.sleep(60 * 60 * 1000);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					String s = tool.composeMsg("1");
					tool.sendSms(s, 1);
				}
				
			}

			try {
				reg_state_calltime = mQcNvItems.getRegStatus();
				reg_state_standbytime = mQcNvItems.getStateOfStandby();
				Log.d("Ows", "reg_state_calltime=" + reg_state_calltime);
				Log.d("Ows", "reg_state_standbytime="
						+ reg_state_standbytime);
				if (reg_state_calltime == 1 && reg_state_standbytime == 1) {
					editor.putBoolean("sms_status", false);
					editor.apply();
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}

			if (action.equals(OwsTool.CALL_TIME_LIMITE_ACTION)
					&& reg_state_calltime != 1) {
				try {
					int preCallTime = mQcNvItems.getOutcallsTotalTime();// total call time
					long t = tool.getCallDuration();
					Log.d("Ows", "t=" + t);
					mQcNvItems.setOutcallsTotalTime((int) t + preCallTime);
					Thread.sleep(300);
					int callTime = mQcNvItems.getOutcallsTotalTime();
					if (callTime >= call_time_limit) {
						String s = tool.composeMsg("2");
						tool.sendSms(s, 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (action.equals(Intent.ACTION_BOOT_COMPLETED)
					&& reg_state_standbytime != 1) {
				context.startService(owsService);
			} 
//			else if (action.equals(OwsTool.STANDBY_TIME_LIMITE_ACTION)
//					&& reg_state_standbytime != 1) {
//				new OwsDialog(context);
//			} 
			/*else if (action.equals(OwsTool.CALL_TIME_CHANGED_ACTION)
					&& reg_state_calltime != 1) {
				try {
					int callTime = mQcNvItems.getOutcallsTotalTime();
					String callTimeLimit = sp
							.getString("call_time_limit", "15");
					Log.d("Ows", "callTimeChanged callTime=" + callTime
							+ ";callTimeLimit=" + callTimeLimit);
					int call_time_limit = 0;
					if (callTimeLimit != null) {
						call_time_limit = Integer.parseInt(callTimeLimit) * 60;
					}
					if (callTime >= call_time_limit) {
						String s = tool.composeMsg("2");
						tool.sendSms(s, 0);
						Log.d("Ows", "callTimeChanged, sms send out...");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
		}
	}

}
