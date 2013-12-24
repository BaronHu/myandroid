package com.cellon.trafficstats.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cellon.trafficstats.db.DBService;
import com.cellon.trafficstats.util.DateInfo;

public class WifiTrafficImpl implements IWifiTraffic {
	
	public static final String WIFI = "wifi";
	
	private DateInfo mDateInfo;
	private DBService mDBService;
	private String sql;
	
	public WifiTrafficImpl(Context context) {
		mDateInfo = new DateInfo(context);
		mDBService = new DBService(context);
	}

	@Override
	public String getThisMonthTrafficOfWifi() {
		String monthWifiValue = "0";
		String firstDayOfMonth = mDateInfo.getFirstDayOfThisMonth();
		String endOfThisMonth = mDateInfo.getLastDayOfThisMonth();
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(wifi) as wifi from traffic where date>=? and date<=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{firstDayOfMonth, endOfThisMonth});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					monthWifiValue = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return monthWifiValue;
	}

	@Override
	public String getTodayTrafficOfWifi() {
		String dayWifiValue = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		String today = mDateInfo.getToday();
		sql = "select wifi from traffic where date=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{today});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					dayWifiValue = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return dayWifiValue;
	}

	@Override
	public String getLastMonthTrafficOfWifi() {
		String value = "0";
		String startOfLastMonth = mDateInfo.getFirstDayOfLastMonth();
		String endOfLastMonth = mDateInfo.getLastDayOfLastMonth();
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(wifi) as wifi from traffic where date>=? and date<=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{startOfLastMonth, endOfLastMonth});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					value = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return value;
	}

	@Override
	public String getYesterdayWifiTraffic() {
		String yesterdayWifiTraffic = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		String yesterday = mDateInfo.getDateOfYesterday();
		sql = "select wifi from traffic where date=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{yesterday});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					yesterdayWifiTraffic = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return yesterdayWifiTraffic;
	}

	@Override
	public String getDurationWifiTraffic(String powerOnDate, String today) {
		String durationWifiTraffic = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(wifi) as wifi from traffic where date>=? and date<?";
		Cursor mCursor = db.rawQuery(sql, new String[]{powerOnDate, today});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					durationWifiTraffic = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return durationWifiTraffic;
	}

	@Override
	public String getBeforeWifiTraffic(String today) {
		String beforeWifiTraffic = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(wifi) as wifi from traffic where date<?";
		Cursor mCursor = db.rawQuery(sql, new String[]{today});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					beforeWifiTraffic = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return beforeWifiTraffic;
	}

	@Override
	public String getWifiTrafficByUid(String uid) {
		String oldWifi = null;
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select wifi from uidtraffic where uid=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{uid});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					oldWifi = mCursor.getString(mCursor.getColumnIndex(WIFI));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return oldWifi;
	}

	@Override
	public List<String> getEveryDayTraffic() {
		List<String> wifi = new ArrayList<String>();
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select wifi from traffic where date>=? and date <=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{mDateInfo.getFirstDayOfThisMonth(),mDateInfo.getToday()});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					String date = mCursor.getString(mCursor.getColumnIndex(WIFI));
					wifi.add(date);
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return wifi;
	}

}
