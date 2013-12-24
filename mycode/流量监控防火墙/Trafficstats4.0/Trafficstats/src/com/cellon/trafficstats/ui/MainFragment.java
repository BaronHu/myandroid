package com.cellon.trafficstats.ui;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.cellon.trafficstats.R;
import com.cellon.trafficstats.dao.GprsTrafficImpl;
import com.cellon.trafficstats.dao.GprsTrafficImpl2;
import com.cellon.trafficstats.dao.IGprsTraffic;
import com.cellon.trafficstats.dao.IWifiTraffic;
import com.cellon.trafficstats.dao.WifiTrafficImpl;
import com.cellon.trafficstats.util.DateInfo;
import com.cellon.trafficstats.util.MyNumberFormat;
import com.cellon.trafficstats.service.TrafficService;

/**
 * @author Baron.Hu
 * Traffic monitoring main view
 * */
public class MainFragment extends Fragment {
	
	private Context mContext;
	
	private IGprsTraffic mGprsTraffic;
//	private IGprsTraffic mGprsTraffic2;
	private IWifiTraffic mWifiTraffic;
	private DateInfo     mDateInfo;
	
	private TextView     titleView;
	private TextView     trafficOk;
	private TextView     trafficBad;
	
	private TextView     wifiTitleView;
	private TextView     gprsTitleView;
	
	private TextView     dayGprsTrafficView;
	private TextView     dayWifiTrafficView;
	private TextView     thisMonthGprsTrafficView;
	private TextView     thisMonthWifiTrafficView;
	
	private TextView     dayGprsTrafficViewValue;
//	private TextView     dayGprsTrafficViewValue2;
	private TextView     dayWifiTrafficViewValue;
	private TextView     thisMonthGprsTrafficViewValue;
//	private TextView     thisMonthGprsTrafficViewValue2;
	private TextView     thisMonthWifiTrafficViewValue;
	private TextView     thisMonthGprsTrafficLeftView;
	private TextView     thisMonthGprsTrafficLeftViewValue;
//	private TextView     thisMonthGprsTrafficLeftViewValue2;
//	private TextView     thisMonthGprsTrafficLeftOkView;
	private TextView     lastMonthGpfsTrafficView;
	private TextView     lastMonthGpfsTrafficViewValue;
//	private TextView     lastMonthGpfsTrafficViewValue2;
	private TextView     lastMonthWifiTrafficView;
	private TextView     lastMonthWifiTrafficViewValue;
	private TextView     wifiRemaining;
	private TextView     wifiRemainingValue;
	
	private String maxGprs = null;
	private String maxWifi = null;
	private String corrGprs = null;
	private String corrValue = null;
	
	private String maxGprs2 = null;
	private String corrGprs2 = null;
	private String corrValue2 = null;
	
	private SharedPreferences mSharedPreferences;
	
	MainFragment newInstance() {
		MainFragment mf = new MainFragment();
		Bundle args = new Bundle();
		args.putString("traffic", "traffic");
		mf.setArguments(args);
		return mf;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstaceState) {
		View v = inflater.inflate(R.layout.traffic_fragment, container, false);
		mContext = inflater.getContext();
		mGprsTraffic = new GprsTrafficImpl(mContext);
//		mGprsTraffic2 = new GprsTrafficImpl2(mContext);
        mWifiTraffic = new WifiTrafficImpl(mContext);
        mDateInfo = new DateInfo(mContext);
		if (isFirstTimeInstalled()) {
        	Intent service = new Intent(mContext, TrafficService.class);
        	mContext.startService(service);
        }
		
		mSharedPreferences = mContext.getSharedPreferences("setting",Activity.MODE_WORLD_READABLE);
		maxGprs = mSharedPreferences.getString("gprs_max", "0");
		maxWifi = mSharedPreferences.getString("wifi_max", "0");
		corrGprs = mSharedPreferences.getString("corr", "0");
		corrValue = mSharedPreferences.getString("corr_value", "0");
		
		maxGprs2 = mSharedPreferences.getString("gprs_max2", "0");
		corrGprs2 = mSharedPreferences.getString("corr2", "0");
		corrValue2 = mSharedPreferences.getString("corr_value2", "0");

//		trafficOk = (TextView) findViewById(R.id.traffic_ok);
//		trafficBad = (TextView) findViewById(R.id.traffic_bad);

		titleView = (TextView) v.findViewById(com.cellon.trafficstats.R.id.titleView);

		wifiTitleView = (TextView) v.findViewById(com.cellon.trafficstats.R.id.wifiTitleView);
		gprsTitleView = (TextView) v.findViewById(com.cellon.trafficstats.R.id.gprsTitleView);

		dayGprsTrafficView = (TextView) v.findViewById(R.id.dayGprsTrafficView);
		dayWifiTrafficView = (TextView) v.findViewById(R.id.dayWifiTrafficView);
		thisMonthGprsTrafficView = (TextView) v.findViewById(R.id.thisMonthGprsTrafficView);
		thisMonthWifiTrafficView = (TextView) v.findViewById(R.id.thisMonthWifiTrafficView);

		dayGprsTrafficViewValue = (TextView) v.findViewById(R.id.dayGprsTrafficViewValue);
//		dayGprsTrafficViewValue2 = (TextView) v.findViewById(R.id.dayGprsTrafficViewValue2);
		dayWifiTrafficViewValue = (TextView) v.findViewById(R.id.dayWifiTrafficViewValue);
		thisMonthGprsTrafficViewValue = (TextView) v.findViewById(R.id.thisMonthGprsTrafficViewValue);
//		thisMonthGprsTrafficViewValue2 = (TextView) v.findViewById(R.id.thisMonthGprsTrafficViewValue2);
		thisMonthWifiTrafficViewValue = (TextView) v.findViewById(R.id.thisMonthWifiTrafficViewValue);
		thisMonthGprsTrafficLeftView = (TextView) v.findViewById(R.id.thisMonthGprsTrafficLeftView);
		thisMonthGprsTrafficLeftViewValue = (TextView) v.findViewById(R.id.thisMonthGprsTrafficLeftViewValue);
//		thisMonthGprsTrafficLeftViewValue2 = (TextView) v.findViewById(R.id.thisMonthGprsTrafficLeftViewValue2);
		// thisMonthGprsTrafficLeftOkView = (TextView)
		// findViewById(R.id.thisMonthGprsTrafficLeftOkView);
		lastMonthGpfsTrafficView = (TextView) v.findViewById(R.id.lastMonthGpfsTrafficView);
		lastMonthGpfsTrafficViewValue = (TextView) v.findViewById(R.id.lastMonthGpfsTrafficViewValue);
//		lastMonthGpfsTrafficViewValue2 = (TextView) v.findViewById(R.id.lastMonthGpfsTrafficViewValue2);
		lastMonthWifiTrafficView = (TextView) v.findViewById(R.id.lastMonthWifiTrafficView);
		lastMonthWifiTrafficViewValue = (TextView) v.findViewById(R.id.lastMonthWifiTrafficViewValue);
		
		if (getDayGprsValue() == null || getDayGprsValue().length() < 1
				|| getDayGprsValue().trim().equals("0.0 M")) {
			dayGprsTrafficViewValue.setText("0 M");
		} else {
			dayGprsTrafficViewValue.setText(getDayGprsValue());
		}
//		if (getDayGprsValue2() == null || getDayGprsValue2().length() < 1
//				|| getDayGprsValue2().trim().equals("0.0 M")) {
//			dayGprsTrafficViewValue2.setText("0 M");
//		} else {
//			dayGprsTrafficViewValue2.setText(getDayGprsValue2());
//		}
		if (getDayWifiValue() == null || getDayWifiValue().length() < 1
				|| getDayWifiValue().trim().equals("0.0 M")) {
			dayWifiTrafficViewValue.setText("0 M");
		} else {
			dayWifiTrafficViewValue.setText(getDayWifiValue());
		}

		wifiRemaining = (TextView) v.findViewById(R.id.wifiRemaining);
		wifiRemainingValue = (TextView) v.findViewById(R.id.wifiRemainingValue);

		try {
			String monthGprsValue = getThisMonthGprsValue();
			if (monthGprsValue == null)
				monthGprsValue = "0";
			if (corrValue != null) {
				if (MyNumberFormat.isNum(corrValue)) {
					double corr = Double.parseDouble(corrValue);//corr - thismonthTraffic
					String thisMonthTraffic = mGprsTraffic.getThisMonthTrafficOfGprs();// BYTE
					String s = "0";
					if (thisMonthTraffic != null) s = MyNumberFormat.left2(thisMonthTraffic);
					double d = Double.parseDouble(s);
					double monthG = d + corr;
					DecimalFormat format = new DecimalFormat("###.###");
					String temp = format.format(monthG);
					SharedPreferences.Editor editor = mSharedPreferences.edit();
					editor.putString("corr", temp);
					editor.commit();
					if (temp.length() < 1) {
						thisMonthGprsTrafficViewValue.setText("0 M");
					} else {
						thisMonthGprsTrafficViewValue.setText(temp + " M");// need to plus the current traffic
					}
				} else {
					thisMonthGprsTrafficViewValue.setText(monthGprsValue);
				}
			} else {
				thisMonthGprsTrafficViewValue.setText(monthGprsValue);
			}
			
			//SIM2
//			String monthGprsValue2 = getThisMonthGprsValue2();
//			if (monthGprsValue2 == null)
//				monthGprsValue2 = "0";
//			if (corrValue2 != null) {
//				if (MyNumberFormat.isNum(corrValue2)) {
//					double corr2 = Double.parseDouble(corrValue2);//corr - thismonthTraffic
//					String thisMonthTraffic = mGprsTraffic2.getThisMonthTrafficOfGprs();// BYTE
//					String s = "0";
//					if (thisMonthTraffic != null) s = MyNumberFormat.left2(thisMonthTraffic);
//					double d = Double.parseDouble(s);
//					double monthG2 = d + corr2;
//					DecimalFormat format = new DecimalFormat("###.###");
//					String temp = format.format(monthG2);
//					SharedPreferences.Editor editor = mSharedPreferences.edit();
//					editor.putString("corr2", temp);
//					editor.commit();
//					if (temp.length() < 1) {
//						thisMonthGprsTrafficViewValue2.setText("0 M");
//					} else {
//						thisMonthGprsTrafficViewValue2.setText(temp + " M");// need to plus the current traffic
//					}
//				} else {
//					thisMonthGprsTrafficViewValue2.setText(monthGprsValue2);
//				}
//			} else {
//				thisMonthGprsTrafficViewValue2.setText(monthGprsValue2);
//			}
			
			if (getThisMonthWifiValue() != null && getThisMonthWifiValue().length() > 0) {
				if (getThisMonthWifiValue().equals("0.0 M")) {
					thisMonthWifiTrafficViewValue.setText("0 M");					
				} else {
					thisMonthWifiTrafficViewValue.setText(getThisMonthWifiValue());
				}
			} else {
				thisMonthWifiTrafficViewValue.setText("0 M");
			}

			if (maxGprs != null && maxGprs.length() > 0) {
				long a = Long.parseLong(maxGprs);
				if (a <= 0) {
					thisMonthGprsTrafficLeftViewValue.setText(R.string.max_month_gprs_setting);
				} else {
					String s = mGprsTraffic.getThisMonthTrafficOfGprs();
					if (s == null || s.length() < 1)
						s = "0";
					if (corrGprs == null || corrGprs.length() < 1)
						corrGprs = "0";
					long b = Long.parseLong(s);
					double x = Double.parseDouble(corrGprs);
					double c = 0;
					c = a - x;
					thisMonthGprsTrafficLeftViewValue
							.setText(MyNumberFormat.left(String.valueOf(c)));
//					if (x * (1 << 20) > b) {
//						c = a - x;
//						thisMonthGprsTrafficLeftViewValue
//								.setText(MyNumberFormat.left(String.valueOf(c)));
//					} else {
//						c = a * (1 << 20) - b;
//						String d = MyNumberFormat.round(String.valueOf(c));
//						thisMonthGprsTrafficLeftViewValue.setText(d);
//					}
				}
			} else {
				thisMonthGprsTrafficLeftViewValue.setText(R.string.max_month_gprs_setting);
			}
			
			//SIM2
//			if (maxGprs2 != null && maxGprs2.length() > 0) {
//				long a = Long.parseLong(maxGprs2);
//				if (a <= 0) {
//					thisMonthGprsTrafficLeftViewValue2.setText(R.string.max_month_gprs_setting);
//				} else {
//					String s = mGprsTraffic2.getThisMonthTrafficOfGprs();
//					if (s == null || s.length() < 1)
//						s = "0";
//					if (corrGprs2 == null || corrGprs2.length() < 1)
//						corrGprs2 = "0";
//					long b = Long.parseLong(s);
//					double x = Double.parseDouble(corrGprs2);
//					double c = 0;
//					if (x * (1 << 20) > b) {
//						c = a - x;
//						thisMonthGprsTrafficLeftViewValue2
//								.setText(MyNumberFormat.left(String.valueOf(c)));
//					} else {
//						c = a * (1 << 20) - b;
//						String d = MyNumberFormat.round(String.valueOf(c));
//						thisMonthGprsTrafficLeftViewValue2.setText(d);
//					}
//				}
//			} else {
//				thisMonthGprsTrafficLeftViewValue2.setText(R.string.max_month_gprs_setting);
//			}

			String lastMgt = getLastMonthGprsTraffic();
			if (lastMgt != null && lastMgt.length() > 0) {
				lastMgt = lastMgt.substring(0, lastMgt.indexOf("M")).trim();
				double mTemp = Double.parseDouble(lastMgt);//mCorr + lastM;
				DecimalFormat format = new DecimalFormat("###.###");
				String temp = format.format(mTemp);
				if (temp.length() < 1) {
					lastMonthGpfsTrafficViewValue.setText("0 M");
				} else {
					lastMonthGpfsTrafficViewValue.setText(temp + " M");
				}
			} else {
				lastMonthGpfsTrafficViewValue.setText("0 M");
			}
			
			//SIM2
//			String lastMgt2 = getLastMonthGprsTraffic2();
//			if (lastMgt2 != null && lastMgt2.length() > 0) {
//				lastMgt2 = lastMgt2.substring(0, lastMgt2.indexOf("M")).trim();
//				double mTemp = Double.parseDouble(lastMgt2);//mCorr + lastM;
//				DecimalFormat format = new DecimalFormat("###.###");
//				String temp = format.format(mTemp);
//				if (temp.length() < 1) {
//					lastMonthGpfsTrafficViewValue2.setText("0 M");
//				} else {
//					lastMonthGpfsTrafficViewValue2.setText(temp + " M");
//				}
//			} else {
//				lastMonthGpfsTrafficViewValue2.setText("0 M");
//			}
			
			if (getLastMonthWifiTraffc() != null && getLastMonthWifiTraffc().length() > 0) {
				if (getLastMonthWifiTraffc().equals("0.0 M")) {
					lastMonthWifiTrafficViewValue.setText("0 M");
				} else {
					lastMonthWifiTrafficViewValue.setText(getLastMonthWifiTraffc());
				}
				
			} else {
				lastMonthWifiTrafficViewValue.setText("0 M");
			}

			String monthWifiTraffic = mWifiTraffic.getThisMonthTrafficOfWifi();
			long wifi = 0;
			if (maxWifi != null && maxWifi.length() > 0) {
				if (MyNumberFormat.isNum(maxWifi)) {
					wifi = Long.parseLong(maxWifi);
				}
				if (wifi <= 0) {
					wifiRemainingValue.setText(R.string.max_month_wifi_setting);
				} else {
					if (monthWifiTraffic == null || monthWifiTraffic.length() < 1) monthWifiTraffic = "0";
					long l = wifi * (1 << 20) - Long.parseLong(monthWifiTraffic);
					wifiRemainingValue.setText(MyNumberFormat.round(String.valueOf(l)));
				}
			} else {
				wifiRemainingValue.setText(R.string.max_month_wifi_setting);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return v;
	}
	
	public boolean isFirstTimeInstalled() {
		final String path = mContext.getFilesDir() + "/" + "a";
		try {
			File file = new File(path);
			if (file.exists()) {
				return false;
			} else {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
    //close the data network
    public void restrictDataNetWork() {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    	boolean enabled = getNetworkState(mContext); 	
    	cm.setMobileDataEnabled(!enabled);
    }
    
    private boolean getNetworkState(Context context){
    	try{
    		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		if(cm != null){
    			return cm.getMobileDataEnabled();
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    } 
    
    //close wifi
    public void closeWifi() {
    	WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    	if (!wm.isWifiEnabled()) {
    		wm.setWifiEnabled(true);
    	} else {
    		wm.setWifiEnabled(false);
    	}
    }
    
    //SIM1
    public String getDayGprsValue() {
    	return MyNumberFormat.round(mGprsTraffic.getTodayTrafficOfGprs());
    }
    
    public String getDayWifiValue() {
    	return MyNumberFormat.round(mWifiTraffic.getTodayTrafficOfWifi());
    }
    
    public String getThisMonthGprsValue() {
        return MyNumberFormat.round(mGprsTraffic.getThisMonthTrafficOfGprs());
    }
    
    public String getThisMonthWifiValue() {
        return MyNumberFormat.round(mWifiTraffic.getThisMonthTrafficOfWifi());
    }
    
    public String getLastMonthGprsTraffic() {
    	return MyNumberFormat.round(mGprsTraffic.getLastMonthTrafficOfGprs());
    }
    
    public String getLastMonthWifiTraffc() {
    	return MyNumberFormat.round(mWifiTraffic.getLastMonthTrafficOfWifi());
    }
    
    //SIM2 
//    public String getDayGprsValue2() {
//    	return MyNumberFormat.round(mGprsTraffic2.getTodayTrafficOfGprs());
//    }
//    
//    public String getThisMonthGprsValue2() {
//        return MyNumberFormat.round(mGprsTraffic2.getThisMonthTrafficOfGprs());
//    }
//    
//    public String getLastMonthGprsTraffic2() {
//    	return MyNumberFormat.round(mGprsTraffic2.getLastMonthTrafficOfGprs());
//    }

}
