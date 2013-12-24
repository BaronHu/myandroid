package com.cellon.usbdiag;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

public class UsbDiagMain extends PreferenceActivity implements
OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
   
	private final String TAG = "UsbDiagMain";
	private CheckBoxPreference UsbDiag;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.usbdiag_preference);
        UsbDiag = (CheckBoxPreference)findPreference("usbdiag");
        //UsbDiag.setPersistent(false);
        UsbDiag.setOnPreferenceClickListener(this);
    }

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		updateDiagState();
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateDiagState(){
		boolean value = UsbDiag.isChecked();
		UsbDiag.setSummary(value ? "Enable"  : "Disable");
		try
	    {
			//"/sys/devices/virtual/usb_composite/usb_mass_storage/enable"
			File composition_file =new File("/sys/class/usb_composite/diag/enable");
			Writer outputStream = null;
			outputStream = new BufferedWriter(new FileWriter(composition_file));
			if(value){
				outputStream.write("1");
			}else{
				outputStream.write("0");
			}
		    outputStream.close();
	    }
	    catch (IOException e) {
	       Log.e(TAG, "IOException caught while writing stream", e);
	    }
	}
}