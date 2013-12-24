package com.cellon.trafficstats.ui;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cellon.trafficstats.R;
import com.cellon.trafficstats.dao.GprsTrafficImpl;
import com.cellon.trafficstats.dao.IGprsTraffic;
import com.cellon.trafficstats.db.DBService;
import com.cellon.trafficstats.ui.NumberPickerDialog;
import com.cellon.trafficstats.util.DateInfo;
import com.cellon.trafficstats.util.MyNumberFormat;

/**
 * @author Baron.Hu
 * */
public class SettingsFragment extends Fragment {
	
	private static final String PREF_NAME = "setting";
	
	private Context mContext;
	
	private TextView gprs_max;
	private TextView gprs_max_value;
	
	private TextView wifi_max;
	private TextView wifi_max_value;
	
	private TextView corr;
	private TextView corr_value;
	
	private TextView payoff_date;
	private TextView payoff_date_value;
	
	private TextView clear;
	
	private Switch closeDataSwitch;
	private Switch closeNetworkSwitch;
//	private Switch closeWifiSwitch;
	
	private TableRow gprs_max_tablerow;
	private TableRow wifi_max_tablerow;
	private TableRow corr_tablerow;
	private TableRow payoff_date_tablerow;
	private TableRow clear_tablerow;
	
	private DBService mDBService;
	private IGprsTraffic mGprsTraffic;
	private DateInfo mDateInfo;
	
	private SharedPreferences mSharedPreferences;
	private ConnectivityManager connManager;
	
	private static boolean ischecked = false;
	
	SettingsFragment newInstance() {
		SettingsFragment sf = new SettingsFragment();
		Bundle args = new Bundle();
		args.putString("settings", "settings");
		sf.setArguments(args);
		return sf;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstaceState) {
		mContext = inflater.getContext();
		
		mSharedPreferences = mContext.getSharedPreferences(PREF_NAME,Activity.MODE_WORLD_READABLE);
		mDBService = new DBService(mContext);
		mGprsTraffic = new GprsTrafficImpl(mContext);
		mDateInfo = new DateInfo(mContext);
		connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		View v = inflater.inflate(R.layout.setting_fragment, container, false);
		
		TextViewListener listener = new TextViewListener();
		SwitchListener sListener = new SwitchListener();
		
		CharSequence summaryGprs = mSharedPreferences.getString("gprs_max", "0") + " M";
		CharSequence summaryWifi = mSharedPreferences.getString("wifi_max", "0") + " M";
		CharSequence summaryCorr = mSharedPreferences.getString("corr", "0") + " M";
		CharSequence payoffDate = mSharedPreferences.getString("payoff_date", "1");
		
		/** TableRow of max gprs */
		gprs_max_tablerow = (TableRow) v.findViewById(R.id.gprs_max_tablerow);
		gprs_max_tablerow.setOnClickListener(listener);
		
		/** TableRow of max wifi */
		wifi_max_tablerow = (TableRow) v.findViewById(R.id.wifi_max_tablerow);
		wifi_max_tablerow.setOnClickListener(listener);
		
		/** TableRow of correcting traffic  */
		corr_tablerow = (TableRow) v.findViewById(R.id.corr_tablerow);
		corr_tablerow.setOnClickListener(listener);
		
		/** TableRow of payoff date */
		payoff_date_tablerow = (TableRow) v.findViewById(R.id.payoff_date_tablerow);
		payoff_date_tablerow.setOnClickListener(listener);
		
		/** TableRow of clear all data */
		clear_tablerow = (TableRow) v.findViewById(R.id.clear_tablerow);
		clear_tablerow.setOnClickListener(listener);
		
		/** gprs max view */
		View gv = v.findViewById(R.id.gprs_max);
		gprs_max = (TextView)gv;
//		gprs_max.setOnClickListener(listener);
		
		View gvv = v.findViewById(R.id.gprs_max_value);
		gprs_max_value = (TextView)gvv;
		gprs_max_value.setText(summaryGprs);
		
		/** wifi max view */
		View wv = v.findViewById(R.id.wifi_max);
		wifi_max = (TextView)wv;
//		wifi_max.setOnClickListener(listener);
		
		View wvv = v.findViewById(R.id.wifi_max_value);
		wifi_max_value = (TextView)wvv;
		wifi_max_value.setText(summaryWifi);
		
		/** correct traffic view */
		View cv = v.findViewById(R.id.corr);
		corr = (TextView)cv;
//		corr.setOnClickListener(listener);
		
		View cvv = v.findViewById(R.id.corr_value);
		corr_value = (TextView)cvv;
		corr_value.setText(summaryCorr);
		
		/** payoff date */
		View pv = v.findViewById(R.id.payoff_date);
		payoff_date = (TextView)pv;
//		payoff_date.setOnClickListener(listener);
		
		View pdv = v.findViewById(R.id.payoff_date_value);
		payoff_date_value = (TextView)pdv;
		payoff_date_value.setText(payoffDate);
		
		/** clear data */
		View clv = v.findViewById(R.id.clear);
		clear = (TextView)clv;
//		clear.setOnClickListener(listener);
		
		/** Switch */
		closeDataSwitch = (Switch) v.findViewById(R.id.close_data_transmit_switch);
		closeNetworkSwitch = (Switch) v.findViewById(R.id.close_data_network_switch);
//		closeWifiSwitch = (Switch) v.findViewById(R.id.close_wifi_switch);
		
		closeDataSwitch.setChecked(mSharedPreferences.getBoolean("close_data_transmit", true));
		closeNetworkSwitch.setChecked(mSharedPreferences.getBoolean("close_data_network", true));
//		closeWifiSwitch.setChecked(mSharedPreferences.getBoolean("close_wifi", true));
		
		closeDataSwitch.setOnCheckedChangeListener(sListener);
		closeNetworkSwitch.setOnCheckedChangeListener(sListener);
//		closeWifiSwitch.setOnCheckedChangeListener(sListener);
		
        return v;
	}
	
	final class TextViewListener implements android.view.View.OnClickListener{
		View view;
		EditText eText;
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.gprs_max_tablerow : 
				view = LayoutInflater.from(mContext).inflate(R.layout.max_gprs_layout, null);
				eText = (EditText) view.findViewById(R.id.max_gprs);
				String valueM = mSharedPreferences.getString("gprs_max", "");
				eText.setText(valueM + "");
				eText.addTextChangedListener(watcher);
				new AlertDialog.Builder(mContext)
				.setView(view)
				.setTitle(R.string.max_month_gprs_title)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String s = eText.getText().toString();
						if (s == null || s.length() < 1) s = "0";
						editor.putString("gprs_max", s);
						editor.commit();
						gprs_max_value.setText(s + " M");
						android.provider.Settings.System.putString(mContext.getContentResolver(), "max_gprs", s);
					}
				}).show();
				break;
			case R.id.wifi_max_tablerow : 
				view = LayoutInflater.from(mContext).inflate(R.layout.max_wifi_layout, null);
				eText = (EditText) view.findViewById(R.id.max_wifi);
				String valueW = mSharedPreferences.getString("wifi_max", "");
				eText.setText(valueW + "");
				eText.addTextChangedListener(watcher);
				new AlertDialog.Builder(mContext)
				.setView(view)
				.setTitle(R.string.max_month_wifi_title)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String s = eText.getText().toString();
						if (s == null || s.length() < 1) s = "0";
						editor.putString("wifi_max", s);
						editor.commit();
						wifi_max_value.setText(s + " M");
					}
				}).show();;
				break;
			case R.id.corr_tablerow : 
				view = LayoutInflater.from(mContext).inflate(R.layout.correct_layout, null);
				eText = (EditText) view.findViewById(R.id.correct);
				String valueC = mSharedPreferences.getString("corr", "");
				eText.setText(valueC + "");
				eText.addTextChangedListener(watcher);
				new AlertDialog.Builder(mContext)
				.setView(view)
				.setTitle(R.string.corr_title)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String valueT = mGprsTraffic.getThisMonthTrafficOfGprs();
						long l = 0;
						if (valueT != null) {
							l = Long.parseLong(valueT);
						}
						double d = 0;
						String s = eText.getText().toString();
						if (s == null || s.length() < 1) s = "0";
						
						if (s.charAt(s.length() - 1) == '.')
							s = s.substring(0, s.length() - 1);
						
						d = Double.parseDouble(s);
						double x = d - (l / (double)(1 << 20));
						DecimalFormat format = new DecimalFormat("###.##");
						String temp = format.format(x);
						editor.putString("corr_value", temp);
						editor.putString("corr", s);
						editor.commit();
						corr_value.setText(String.valueOf(s) + " M");
						android.provider.Settings.System.putString(mContext.getContentResolver(), "corrValue", temp);
					}
				}).show();
				break;
			case R.id.payoff_date_tablerow : 
				new NumberPickerDialog(mContext,
						mLimitListener,
						Integer.parseInt(mSharedPreferences.getString("payoff_date", "1")),
						1,28,R.string.choose_payoff_date).show();
				break;
			case R.id.clear_tablerow : 
				new AlertDialog.Builder(mContext)
				.setTitle(R.string.clear_dialog_title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(R.string.clear_dialog_msg)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//back up the traffic before clearing all the data
						String powerOnDate = mSharedPreferences.getString("power_on_date", null);
						String today = mDateInfo.getToday();
						String historyGprsTraffic;
						try {
							historyGprsTraffic = mGprsTraffic.getHistoryTraffic(powerOnDate, today);
						} catch (Exception e) {
							historyGprsTraffic = "";
							e.printStackTrace();
							//display a fake toast, haha...
							Toast.makeText(mContext, R.string.clear_ok, Toast.LENGTH_LONG).show();
						}
						
						//set the value of corr to be 0 after clearing the database
						editor.putString("corr", "0");
						editor.putString("corr_value", "0");
						editor.apply();
						corr_value.setText("0 M");
						
						//update the table 
						SQLiteDatabase db = mDBService.getWritableDatabase();
						ContentValues cv = new ContentValues();
						cv.put("gprs", "0");
						cv.put("wifi", "0");
						cv.put("gprs1", "0");
						db.update("traffic", cv, null, null);
						db.close();
						
						Intent intent = new Intent("com.cellon.trafficstats.CLEAR");
						mContext.sendBroadcast(intent);
						
						Toast.makeText(mContext, R.string.clear_ok, Toast.LENGTH_LONG).show();
					}
				}).show();
				break;
			default : 
				break;
			}
		}
	}
	
	// do not let user input 0 as the first number
	final TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() > 0 && (s.charAt(0) == '0' || s.charAt(0) == '.')) {
				s = s.delete(0, 1);
			}
		}
	};
	
	final NumberPickerDialog.OnNumberSetListener mLimitListener =
        new NumberPickerDialog.OnNumberSetListener() {
            public void onNumberSet(int limit) {
            	SharedPreferences.Editor editor = mSharedPreferences.edit();
            	editor.putString("payoff_date", limit + "");
            	editor.commit();
            	payoff_date_value.setText(limit + "");
            	android.provider.Settings.System.putString(mContext.getContentResolver(), "payoff_date", String.valueOf(limit));
            }
    };
    
    final class SwitchListener implements android.widget.CompoundButton.OnCheckedChangeListener{
    	SharedPreferences.Editor editor = mSharedPreferences.edit();
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch(buttonView.getId()) {
			case R.id.close_data_transmit_switch : 
				if (isChecked) {
					connManager.setBackgroundDataSetting(true);	
		    		ContentResolver.setMasterSyncAutomatically(true);
		    		editor.putBoolean("close_data_transmit", true);
				} else {
					connManager.setBackgroundDataSetting(false);	 
		    		ContentResolver.setMasterSyncAutomatically(false);
		    		editor.putBoolean("close_data_transmit", false);
				}
				editor.commit();
				break;
			case R.id.close_data_network_switch : 
				if (isChecked) {
					connManager.setMobileDataEnabled(true);
					editor.putBoolean("close_data_network", true);
				} else {
					connManager.setMobileDataEnabled(false);
					editor.putBoolean("close_data_network", false);
				}
				editor.commit();
				break;
//			case R.id.close_wifi_switch : 
//				WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//		    	if (!wm.isWifiEnabled()) {
//		    		wm.setWifiEnabled(true);
//		    		editor.putBoolean("close_wifi", true);
//		    	} else {
//		    		wm.setWifiEnabled(false);
//		    		editor.putBoolean("close_wifi", false);
//		    	}
//		    	editor.commit();
//				break;
			default : 
				break;
			}
			
		}
		
	}
    
}
