package com.cellon.trafficstats.ui;

import com.cellon.trafficstats.R;
import com.cellon.trafficstats.ui.ExceedDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.content.Context;


public class ExceedDialog extends Activity {
	
	public static final String PREF_NAME = "setting";
	private Context mContext;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exceed_layout);
		mContext = getApplicationContext();
		mSharedPreferences = this.getSharedPreferences(PREF_NAME,Activity.MODE_WORLD_READABLE);
		editor = mSharedPreferences.edit();
		new AlertDialog.Builder(this)
		.setTitle(R.string.warning)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage(R.string.gprs_warning)
		.setNegativeButton(R.string.close_network, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		    	cm.setMobileDataEnabled(false);
		    	editor.putBoolean("close_data_network", false);
		    	editor.commit();
		    	finish();
			}
		})
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).show();
	}
}
