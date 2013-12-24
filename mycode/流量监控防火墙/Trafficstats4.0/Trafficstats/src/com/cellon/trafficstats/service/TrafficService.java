package com.cellon.trafficstats.service;

import static android.net.NetworkTemplate.buildTemplateMobileAll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.ServiceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.android.internal.telephony.MSimPhoneFactory;
import com.cellon.trafficstats.R;
import com.cellon.trafficstats.dao.GprsTrafficImpl;
import com.cellon.trafficstats.dao.GprsTrafficImpl2;
import com.cellon.trafficstats.dao.IGprsTraffic;
import com.cellon.trafficstats.dao.IWifiTraffic;
import com.cellon.trafficstats.dao.WifiTrafficImpl;
import com.cellon.trafficstats.db.DBService;
import com.cellon.trafficstats.ui.Api;
import com.cellon.trafficstats.ui.Api.DroidApp;
import com.cellon.trafficstats.util.DateInfo;
import com.cellon.trafficstats.util.Log2File;
import com.cellon.trafficstats.util.MyNumberFormat;

/**
 * @author Baron.Hu
 * 
 * Observe the traffic
 * the traffic is saved in the path proc/net/dev
 * there are 2g/3g and wifi traffic.
 * */
public class TrafficService extends Service {
	
	private Context 		  mContext;
	private DBService 		  mDBService;
	private DateInfo 		  mDateInfo;
	private IGprsTraffic 	  mGprsTraffic;
	private IGprsTraffic 	  mGprsTraffic2;
	private IWifiTraffic 	  mWifiTraffic;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor;
	
	private String powerOnDate = null;//power on date
	private String[] maxValue = new String[2];//max value of user setting, saved in preference

	private static final String DEV_FILE = "/proc/net/dev"; //path of traffic file
	
	//for mapping the format of the file /proc/net/dev
	private String[] gprsdata = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
	private String[] wifidata = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};  
	
	private long dataGPRS;
	private long dataWIFI;
	
	private String dayGprs = null;
	private String dayGprs2 = null;
	private String dayWifi = null;
	private String todayWifi = null;
	private String todayWifi1 = null;
	private boolean isSelected = true;
	private boolean isChanged = false;
	private boolean is3g = true;
	private boolean isFromSIM0;
	private boolean isFromSIM1;
	private boolean isFromWIFI;
	private long tempValue;
	
	private boolean onoffView;//floating view
	private double oldValue;
	
	private Handler handler = new Handler();

	private Runnable mTask = new Runnable() {

		@Override
		public void run() {
			insertOrUpdateData();
			clearCorr();
			notifyWhenExceed();
			handler.postDelayed(this, 10000);//perform it every 10 second
		}
		
	};
	
	/**
	 * Insert or Update the traffic data that saved in the database
	 * we will insert the data if the date is today and there are no records under that column,
	 * else update it.
	 * */
	public void insertOrUpdateData() {
		String today = mDateInfo.getToday().trim();//current date
		
		long[] netValue = getInfo();
		String gprs = String.valueOf(netValue[0]);
		String wifi = String.valueOf(netValue[1]);
		
		ContentValues mContentValues = new ContentValues();
		mContentValues.clear();
		
		String sumDayGprs = "";
		String sumDayWifi = "";
		
		String newDayGprs = "";
		String newDayWifi = "";
		
		long mHistoryWifiTraffic = 0;
		
		String gprs1 = mGprsTraffic.getGprs1Traffic(powerOnDate, today);
		String gprs2 = mGprsTraffic2.getGprs1Traffic(powerOnDate, today);
		
		try {
			if (Api.isWiFiActive(mContext)) {// WIFI
				is3g = false;
				isFromWIFI = true;
//				Log.d("baron.hu", "isChanged=" + isChanged + "; isSelected=" + isSelected);
//				Log.d("baron.hu", "todayWifi1=" + todayWifi1 + "; todayWifi=" + todayWifi + 
//						"; dayWifi=" + dayWifi + "; netValue[1]=" + netValue[1]);
				if (powerOnDate == null || powerOnDate.equals(today)) {// power on more than one time in the same day
					if (dayWifi != null && Long.parseLong(dayWifi) > 0) {
						sumDayWifi = String.valueOf(netValue[1] + Long.parseLong(dayWifi));
						mContentValues.put("wifi", sumDayWifi);
					} else {
						if (isChanged) {
							isChanged = false;
							todayWifi1 = mWifiTraffic.getTodayTrafficOfWifi();
						}
//						Log.d("baron.hu", "todayWifi1=" + todayWifi1);
						if (todayWifi1 != null){
							mContentValues.put("wifi", String.valueOf((netValue[1] + Long.parseLong(todayWifi1))));
						} else {
							mContentValues.put("wifi", wifi);
						}
					}
				} else {
					String historyWifiTraffic = mWifiTraffic.getDurationWifiTraffic(powerOnDate, today);
					if (historyWifiTraffic != null) {
						mHistoryWifiTraffic = Long.parseLong(historyWifiTraffic);
					}
					if (isSelected) {
						isSelected = false;
						todayWifi = mWifiTraffic.getTodayTrafficOfWifi();
					}
//					Log.d("baron.hu", "todayWifi=" + todayWifi);
					if (todayWifi == null) {
						mContentValues.put("wifi", netValue[1] + "");
					} else {
						if (!mDateInfo.isToday()) todayWifi = "0";
						mContentValues.put("wifi", (netValue[1] + Long.parseLong(todayWifi)) + "");
					}
				}
				String insertSql = "insert into traffic(date,wifi) values(?,?)";
				SQLiteDatabase db = mDBService.getWritableDatabase();
				if (mDateInfo.isToday()) {
					db.update("traffic", mContentValues, "date=?", new String[]{today});
				} else {
					mDBService.execSQL(insertSql, new String[]{today, wifi});
				}
				db.close();
			} else {
				int sub = MSimPhoneFactory.getDataSubscription();
				Log.d("baron.hu", "TrafficService sub=" + sub);
				isSelected = true;
				isChanged = true;
				isFromSIM0 = true;
				dayWifi = mWifiTraffic.getTodayTrafficOfWifi();
				if (powerOnDate == null || powerOnDate.equals(today)) {
					if (dayGprs != null && Long.parseLong(dayGprs) > 0) {
						
						// if there are records under this column,that means the
						// user has
						// power on/off the phone more than once in the same day.
						// So, we should plus the old traffic as the total traffic
						
						/**
						 * if the user power on the phone more than one time
						 * and changed the network mode(3g/wifi) very frequently
						 * (at list changed twice)
						 * */ 
						if (!is3g) {
							String mTodayValue = mGprsTraffic.getDurationGprsTraffic(
									powerOnDate, today);
							if (mTodayValue != null) {
								tempValue = Long.parseLong(mTodayValue) - Long.parseLong(dayGprs);
							}
							mContentValues.put("gprs", sumDayGprs);
							mContentValues.put("gprs1", gprs);
						} else {
							// if there are no traffic records in current date,
							// we will insert the date as a new record of today
							mContentValues.put("gprs", gprs);
						}
					} else {
						// if user didn't power off the phone about N days, then we
						// should get the old traffic value
						// which the date was between "power on date" and "yesterday"
						String beforeGprsValue = mGprsTraffic.getDurationGprsTraffic(
								powerOnDate, today);
						if (gprs1 == null) {
							if (beforeGprsValue == null)
								beforeGprsValue = "0";
							long beforeGprs = Long.parseLong(beforeGprsValue);
							long todayGprsTraffic = Long.parseLong(gprs) - beforeGprs;
							newDayGprs = String.valueOf(todayGprsTraffic);
							mContentValues.put("gprs", newDayGprs);
						} else {
							gprs1 = String.valueOf((Long.parseLong(gprs) - Long.parseLong(gprs1)));
							mContentValues.put("gprs1", gprs1);
							mContentValues.put("gprs", gprs1);
						}
					}
					String insertSql = "insert into traffic(date,gprs,gprs1) values(?,?,?)";
					SQLiteDatabase db = mDBService.getWritableDatabase();
					if (mDateInfo.isToday()) {
						db.update("traffic", mContentValues, "date=?", new String[]{today});
					} else {
						mDBService.execSQL(insertSql, new String[]{today, gprs, gprs1});
					}
					db.close();
				} else {
					isFromSIM1 = true;
					String sim0TodayTraffic;
					if (isFromSIM0) {
						sim0TodayTraffic = mGprsTraffic.getTodayTrafficOfGprs();
						isFromSIM0 = false;
					}
					if (powerOnDate == null || powerOnDate.equals(today)) {
						if (dayGprs2 != null && Long.parseLong(dayGprs2) > 0) {
							if (!is3g) {
								String mTodayValue = mGprsTraffic2.getDurationGprsTraffic(
										powerOnDate, today);
								if (mTodayValue != null) {
									tempValue = Long.parseLong(mTodayValue) - Long.parseLong(dayGprs2);
								}
								sumDayGprs = String.valueOf(netValue[0] + Long.parseLong(dayGprs2) - tempValue);
							} else {
								sumDayGprs = String.valueOf(netValue[0] + Long.parseLong(dayGprs2));
							}
							mContentValues.put("gprs", sumDayGprs);
							mContentValues.put("gprs1", gprs);
						} else {
							mContentValues.put("gprs", gprs);
						}
					} else {
						String beforeGprsValue = mGprsTraffic2.getDurationGprsTraffic(
								powerOnDate, today);
						if (gprs2 == null) {
							if (beforeGprsValue == null)
								beforeGprsValue = "0";
							long beforeGprs = Long.parseLong(beforeGprsValue);
							long todayGprsTraffic = Long.parseLong(gprs) - beforeGprs;
							newDayGprs = String.valueOf(todayGprsTraffic);
							mContentValues.put("gprs", newDayGprs);
						} else {
							gprs1 = String.valueOf((Long.parseLong(gprs) - Long.parseLong(gprs2)));
							mContentValues.put("gprs1", gprs2);
							mContentValues.put("gprs", gprs2);
						}
					}
					String insertSql = "insert into traffic2(date,gprs,gprs1) values(?,?,?)";
					SQLiteDatabase db = mDBService.getWritableDatabase();
					if (mDateInfo.isToday()) {
						db.update("traffic2", mContentValues, "date=?", new String[]{today});
					} else {
						mDBService.execSQL(insertSql, new String[]{today, gprs, gprs1});
					}
					db.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log2File.writeLog2File("insert or update", e.getMessage());
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext 		   = this.getApplicationContext();
		mDBService 		   = new DBService(mContext);
		mDateInfo 	       = new DateInfo(mContext);
		mGprsTraffic 	   = new GprsTrafficImpl(mContext);
		mGprsTraffic2      = new GprsTrafficImpl2(mContext);
		mWifiTraffic 	   = new WifiTrafficImpl(mContext);
		mSharedPreferences = mContext.getSharedPreferences("setting", Activity.MODE_WORLD_READABLE);
		editor = mSharedPreferences.edit();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("baron.hu", "TrafficService started...");
		dayGprs 	= mGprsTraffic.getTodayTrafficOfGprs();
		dayGprs2 	= mGprsTraffic2.getTodayTrafficOfGprs();
		dayWifi 	= mWifiTraffic.getTodayTrafficOfWifi();
		powerOnDate = mSharedPreferences.getString("power_on_date", null);
		handler.postDelayed(mTask, 0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("baron.hu", "TrafficService destroyed...");
		handler.removeCallbacks(mTask);
	}
	
	/**
	 * Read the file /proc/net/dev
	 * this file records all of the traffic,so we need to distinguish which one is gprs/3g or wifi
	 * */
	public void readDev() {
		File file = new File(DEV_FILE);
		BufferedReader br = null;
		String str;
		String[] segs;
		String[] netdata;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((str = br.readLine()) != null) {
				segs = str.trim().split(":");
				if (str.trim().toLowerCase().startsWith("rmnet0")) {//rmnet0/eth0
					netdata = segs[1].trim().split(" ");
					for (int i = 0, j = 0; i < netdata.length; i++) {
						if (netdata[i].length() > 0) {
							gprsdata[j] = netdata[i];
							j++;
						}
					}
				}
				
				if (str.trim().toLowerCase().startsWith("wlan0")) {//wifi
					netdata = segs[1].trim().split(" ");
					for (int i = 0, j = 0; i < netdata.length; i++) {
						if (netdata[i].length() > 0) {
							wifidata[j] = netdata[i];
							j++;
						}
					}
				}
			}//end while
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log2File.writeLog2File("IOExceptoin", e.getMessage());
		}
	}
	
	public long[] getInfo() {
		readDev();
		long d[] = new long[2];
		dataGPRS = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
		dataWIFI = Long.parseLong(wifidata[0])+ Long.parseLong(wifidata[8]);// + Long.parseLong(wifidata[1])
				// + Long.parseLong(wifidata[9]);
		d[0] = dataGPRS;
		d[1] = dataWIFI;
		return d;
		
	}

    //clear the correct traffic if the day equals pay date
    public void clearCorr() {
    	String corrGprs = mSharedPreferences.getString("corr", "0");
    	if (corrGprs == null || corrGprs.equals("0")) return; 
    	String today = mDateInfo.getToday();
    	String today1 = today.substring(6);
    	String today2 = today.substring(7);
    	String payDate = mSharedPreferences.getString("payoff_date", null);
    	String hasPay = mSharedPreferences.getString("has_pay", null);
    	if (today1.equals(payDate) || today2.equals(payDate)) {
    		if (hasPay == null || hasPay.equals("0")) {
    			editor.putString("corr", "0");
        		editor.putString("corr_value", "0");
        		editor.putString("has_pay", "1");
        		editor.commit();
        		android.provider.Settings.System.putString(getContentResolver(), "corrValue", "0");
    		} 
    	} else {
    		hasPay = "0";
    		editor.putString("has_pay", hasPay);
    		editor.commit();
    	}
    }
    
    private boolean hasSend = false;
    private String monthGprsTraffic;
    public void notifyWhenExceed() {
    	monthGprsTraffic = mGprsTraffic.getThisMonthTrafficOfGprs();
    	long monthG = 0;
    	if (monthGprsTraffic != null) 
    		monthG = Long.parseLong(monthGprsTraffic);
    	else 
    		return;
    	
    	monthG = monthG / (1 << 20);
    	
    	String maxGprsTraffic   = mSharedPreferences.getString("gprs_max", "0");
    	long maxG = Long.parseLong(maxGprsTraffic);
    	if (maxG <= 0)
    		return;
    	if (monthG >= maxG && !hasSend) {
			Intent intent = new Intent("com.cellon.trafficstats.EXCEED");
			mContext.sendBroadcast(intent);
			hasSend = true;
    	}
    }

}
