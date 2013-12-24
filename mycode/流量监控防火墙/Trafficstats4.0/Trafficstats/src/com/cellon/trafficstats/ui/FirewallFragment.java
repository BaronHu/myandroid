package com.cellon.trafficstats.ui;

import java.io.DataOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.util.Log;

import com.cellon.trafficstats.R;
import com.cellon.trafficstats.ui.Api;
import com.cellon.trafficstats.ui.Api.DroidApp;

/**
 * @author Baron.Hu
 * */
public class FirewallFragment extends Fragment {
	
	private ProgressDialog progress = null;
	private ListView listview;
	
	private String powerOnDate;
	SharedPreferences mSetting;
	SharedPreferences mSharedPreferences;
	private long oldg, oldg1;
	private long oldw, oldw1;
	private boolean isActive;
	private Context mContext;
	private LayoutInflater mInflater;
	
	FirewallFragment newInstance() {
		FirewallFragment ff = new FirewallFragment();
		Bundle args = new Bundle();
		args.putString("firewall", "firewall");
		ff.setArguments(args);
		return ff;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	 /**
     * Check if the stored preferences are OK
     */
    private void checkPreferences() {
    	final SharedPreferences prefs = mContext.getSharedPreferences(Api.PREFS_NAME, 0);
    	final Editor editor = prefs.edit();
    	if (prefs.getString(Api.PREF_MODE, "").length() == 0) {
    		editor.putString(Api.PREF_MODE, Api.MODE_BLACKLIST);
    		editor.commit();
    	}
    }
    
    /**
	 * If the applications are cached, just show them, otherwise load and show
	 */
	private void showOrLoadApplications() {
    	if (Api.applications == null) {
    		// The applications are not cached.. so lets display the progress dialog
    		progress = ProgressDialog.show(mContext, mContext.getString(R.string.working), mContext.getString(R.string.reading_apps), true);
        	final Handler handler = new Handler() {
        		public void handleMessage(Message msg) {
        			if (progress != null) progress.dismiss();
        			showApplications();
        		}
        	};
        	new Thread() {
        		public void run() {
        			Api.getApps(mContext);
        			handler.sendEmptyMessage(0);
        		}
        	}.start();
    	} else {
    		// the applications are cached, just show the list
        	showApplications();
    	}
	}
    /**
     * Show the list of applications
     */
    private void showApplications() {
        final DroidApp[] apps = Api.getApps(mContext);
        // Sort applications - selected first, then alphabetically
        Arrays.sort(apps, new Comparator<DroidApp>() {
			@Override
			public int compare(DroidApp o1, DroidApp o2) {
				if ((o1.selected_wifi|o1.selected_3g) == (o2.selected_wifi|o2.selected_3g)) {
					return o1.names[0].compareTo(o2.names[0]);
				}
				if (o1.selected_wifi || o1.selected_3g) return -1;
				return 1;
			}
        });
        final LayoutInflater inflater = mInflater;//getLayoutInflater();
		final ListAdapter adapter = new ArrayAdapter<DroidApp>(mContext, R.layout.listitem1,R.id.itemtext,apps) {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
       			ListEntry entry;
        		if (convertView == null) {
        			// Inflate a new view
        			convertView = inflater.inflate(R.layout.listitem1, parent, false);
       				entry = new ListEntry();
       				entry.box_wifi = (CheckBox) convertView.findViewById(R.id.itemcheck_wifi);
       				entry.box_3g   = (CheckBox) convertView.findViewById(R.id.itemcheck_3g);
       				
       				//baron.hu
       				if (!Api.hasRootAccess(mContext, true)) {
       					entry.box_wifi.setEnabled(false);
       					entry.box_3g.setEnabled(false);
       				} else {
       					entry.box_wifi.setEnabled(true);
       					entry.box_3g.setEnabled(true);
       				}
       				
       				entry.text 	   = (TextView) convertView.findViewById(R.id.itemtext);
       				entry.appIcons = (ImageView)convertView.findViewById(R.id.app_icon);//baron.hu
       				entry.upload   = (TextView) convertView.findViewById(R.id.upload);//baron.hu
       				entry.download = (TextView) convertView.findViewById(R.id.download);//baron.hu
       				
       				convertView.setTag(entry);
       				
       				entry.box_wifi.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							final DroidApp app = (DroidApp) buttonView.getTag();
							if (app != null) {
								switch (buttonView.getId()) {
									case R.id.itemcheck_wifi: app.selected_wifi = isChecked; break;
									case R.id.itemcheck_3g: app.selected_3g = isChecked; break;
								}
							}
						}
					});
       				entry.box_3g.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							final DroidApp app = (DroidApp) buttonView.getTag();
							if (app != null) {
								switch (buttonView.getId()) {
									case R.id.itemcheck_wifi: app.selected_wifi = isChecked; break;
									case R.id.itemcheck_3g: app.selected_3g = isChecked; break;
								}
							}
							
						}
					});
       				entry.box_wifi.setOnClickListener(new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							applyOrSaveRules();
						}
					});
       				entry.box_3g.setOnClickListener(new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							applyOrSaveRules();
						}
					});
        		} else {
        			// Convert an existing view
        			entry = (ListEntry) convertView.getTag();
        		}
        		final DroidApp app = apps[position];
        		long[] values = getTrafficByUid(app);//baron.hu
        		entry.text.setText(app.toString());
        		entry.appIcons.setImageDrawable(app.appIcon);//baron.hu
        		convertAndSetColor(values[0], entry.upload);//baron.hu , 3g traffic
				convertAndSetColor(values[1], entry.download);//baron.hu , wifi traffic
        		final CheckBox box_wifi = entry.box_wifi;
        		box_wifi.setTag(app);
        		box_wifi.setChecked(app.selected_wifi);
        		final CheckBox box_3g = entry.box_3g;
        		box_3g.setTag(app);
        		box_3g.setChecked(app.selected_3g);
       			return convertView;
        	}
        };
        listview.setAdapter(adapter);
    }
    
    /*
	 * baron.hu
	 */
	private void convertAndSetColor(long uid, TextView text) {
		String value = null;
		if (uid <= 0) {
			value = "0B";
			text.setText(value);
			text.setTextColor(0xff919191);
			return;
		} else {
			value = unitHandler(uid);
		}
		text.setText(value);
		text.setTextColor(0xffff0300);
	}
    
    //baron.hu
    private String unitHandler(long uid) {
		String value = null;
		long temp = uid;
		float floatnum = uid;
		if ((temp = temp / 1000) < 1) {
			value = uid + "B";
		} else if ((floatnum = (float)temp / 1000) < 1) {
			value = temp + "K";
		} else {
			DecimalFormat format = new DecimalFormat("0.#");
			value = format.format(floatnum) + "M";
		}
		return value;
	}
    
    /**
     * baron.hu
     * save the 3g/wifi traffic for every application
     * */
    private void saveUidTraffic(DroidApp app) {
    	if (isActive) {
    		oldg = mSharedPreferences.getLong(app.uid + "g", 0);
    		oldw = mSharedPreferences.getLong(app.uid + "w", 0);
    	}
		oldg1 = oldg;
		oldw1 = oldw;
    	SharedPreferences.Editor editor = mSharedPreferences.edit();
    	long newTraffic = TrafficStats.getUidRxBytes(app.uid) + TrafficStats.getUidTxBytes(app.uid);
    	if (Api.isWiFiActive(mContext)) {
    		if (oldw1 <= 0) {
    			if (oldg1 <= 0) {
    				editor.putLong(app.uid + "w", newTraffic);
    			} else {
    				if (newTraffic < oldg1) {
    					editor.putLong(app.uid + "w", newTraffic);
    				} else {
    					newTraffic -= oldg1;
    					editor.putLong(app.uid + "w", newTraffic);
    				}
    			}
    		} else {
    			if (newTraffic > oldg1) {
    				newTraffic -= oldg1;
    				if (newTraffic > oldw1) {
    					editor.putLong(app.uid + "w", newTraffic);
    				} else {
    					newTraffic += oldw1;
    					editor.putLong(app.uid + "w", newTraffic);
    				}
    			}
    		}
    	} else {
    		if (oldg1 <= 0) {
    			if (oldw1 <= 0) {
    				editor.putLong(app.uid + "g", newTraffic);
    			} else {
    				if (newTraffic < oldw1) {
    					editor.putLong(app.uid + "g", newTraffic);
    				} else {
    					newTraffic -= oldw1;
    					editor.putLong(app.uid + "g", newTraffic);
    				}
    			}
    		} else {
    			if (newTraffic > oldw1) {
    				newTraffic -= oldw1;
    				if (newTraffic > oldg1) {
    					editor.putLong(app.uid + "g", newTraffic);
    				} else {
    					newTraffic += oldg1;
    					editor.putLong(app.uid + "g", newTraffic);
    				}
    			}
    		}
    	}
    	editor.commit();
    	isActive = false;
    }
    
    /**
     * baron.hu
     * get uid traffic
     * */
    private long[] getTrafficByUid(DroidApp app) {
    	saveUidTraffic(app);
    	long[] values = new long[2];
    	long appg = mSharedPreferences.getLong(app.uid + "g", 0);
    	long appw  = mSharedPreferences.getLong(app.uid + "w", 0);
    	values[0] = appg;
    	values[1] = appw;
    	return values;
    }

	/**
	 * Apply or save iptable rules, showing a visual indication
	 */
	private void applyOrSaveRules() {
		final Handler handler;
		final boolean enabled = Api.isEnabled(mContext);
//		progress = ProgressDialog.show(this, mContextgetString(R.string.working), (enabled?"Applying":"Saving") + " iptables rules.", true);
		handler = new Handler() {
			public void handleMessage(Message msg) {
//				if (progress != null) progress.dismiss();
				if (!Api.hasRootAccess(mContext, true)) return;
				if (enabled) {
					if (Api.applyIptablesRules(mContext, true)) {
						Toast.makeText(mContext, R.string.rules_applied, Toast.LENGTH_SHORT).show();
					}
				} else {
					Api.saveRules(mContext);
					Toast.makeText(mContext, R.string.rules_saved, Toast.LENGTH_SHORT).show();
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}

	private static class ListEntry {
		private CheckBox box_wifi;
		private CheckBox box_3g;
		private TextView text;
		
		//baorn.hu
		private TextView upload;
		private TextView download;
		private ImageView appIcons;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstaceState) {
		mInflater = inflater;
		mContext = inflater.getContext();
		mSetting = mContext.getSharedPreferences("setting", Activity.MODE_WORLD_WRITEABLE);
		powerOnDate = mSetting.getString("power_on_date", "0");
		mSharedPreferences = mContext.getSharedPreferences("uidtraffic", Activity.MODE_WORLD_WRITEABLE);
		View v = inflater.inflate(R.layout.firewall_fragment, container, false);
		if (this.listview == null) {
    		this.listview = (ListView) v.findViewById(R.id.listview);
    	}
		checkPreferences();
		Api.assertBinaries(mContext, true);
		showOrLoadApplications();
        return v;
	}

}
