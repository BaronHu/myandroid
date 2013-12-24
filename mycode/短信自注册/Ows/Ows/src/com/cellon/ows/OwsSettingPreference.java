package com.cellon.ows;

import java.util.Date;

import com.cellon.ows.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.qualcomm.qcnvitems.QcNvItems;

public class OwsSettingPreference extends PreferenceActivity implements
OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{
	
	private CheckBoxPreference smsStatus;
	private EditTextPreference callTimeLimit;
	private EditTextPreference standbyTimeLimit;
	private EditTextPreference gateway;
	private Preference status;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	private int call_time;
	private long standby_time;
	private QcNvItems mQcNvItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mQcNvItems = new QcNvItems();
		addPreferencesFromResource(R.xml.pref_ows);
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sp.edit();
		
		smsStatus = (CheckBoxPreference) findPreference("sms_status");
		callTimeLimit = (EditTextPreference) findPreference("call_time_limit");
		standbyTimeLimit = (EditTextPreference) findPreference("standby_time_limit");
		gateway = (EditTextPreference) findPreference("gateway");
		status = (Preference) findPreference("status");
		
		CharSequence cCallTimeLimit = sp.getString("call_time_limit", "15");
		callTimeLimit.setSummary(cCallTimeLimit + " minutes");
		CharSequence cStandbyTimeLimit = sp.getString("standby_time_limit", "7200");
		standbyTimeLimit.setSummary(cStandbyTimeLimit + " minutes");
		CharSequence cGateway = sp.getString("gateway", "8009");
		gateway.setSummary(cGateway);
		
		smsStatus.setOnPreferenceChangeListener(this);
		callTimeLimit.setOnPreferenceChangeListener(this);
		standbyTimeLimit.setOnPreferenceChangeListener(this);
		gateway.setOnPreferenceChangeListener(this);
		status.setOnPreferenceClickListener(this);
		
		try {
			call_time = mQcNvItems.getOutcallsTotalTime();
			standby_time = mQcNvItems.getLifeTime();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (!preference.getKey().equals("sms_status")
				&& (newValue == null || newValue.toString().length() < 1
						|| OwsTool.isNum(newValue.toString()))) {
			showIllegalNumberToast();
			return false;
		}
		if (preference.getKey().equals("call_time_limit")) {
			editor.putString("call_time_limit", newValue + "");
			callTimeLimit.setSummary(newValue.toString() + " minutes");
		} else if (preference.getKey().equals("standby_time_limit")) {
			editor.putString("standby_time_limit", newValue + "");
			standbyTimeLimit.setSummary(newValue.toString() + " minutes");
		} else if (preference.getKey().equals("gateway")) {
			editor.putString("gateway", newValue + "");
			gateway.setSummary(newValue + "");
		}
		editor.apply();
		return true;
	}
	
	public void showIllegalNumberToast() {
		Toast.makeText(this, R.string.toast, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("status")) {
			new AlertDialog.Builder(this)
			.setTitle(R.string.status)
			.setMessage("call time=" + call_time + "s; SIM_Ready_time=" + standby_time + "s; " +new Date().toString())
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
			
		} 
		return false;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
	} 

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		menu.add(1, 1, 1, R.string.sms_service);
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		if (item.getItemId() == 1) {
//			Intent intent = new Intent(this, OWSSmsServiceActivity.class);
//			startActivity(intent);
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
