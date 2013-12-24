package com.cellon.trafficstats.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cellon.trafficstats.db.DBService;
import com.cellon.trafficstats.util.DateInfo;

public class GprsTrafficImpl2 implements IGprsTraffic {
	
public static final String GPRS = "gprs";
	
	private DateInfo mDateInfo;
	private DBService mDBService;
	private String sql;
	
	public GprsTrafficImpl2(Context context) {
		mDateInfo = new DateInfo(context);
		mDBService = new DBService(context);
	}

	@Override
	public String getThisMonthTrafficOfGprs() {
		String[] payDate = mDateInfo.getPayDate();
		String now = mDateInfo.getToday();
		String monthGprsValue = "0";
		String firstDayOfMonth = null;
		String endOfThisMonth = null;
		if (now.compareTo(payDate[0]) >= 0) {
			firstDayOfMonth = payDate[0];
			endOfThisMonth = payDate[1];
		} else {
			firstDayOfMonth = payDate[2];
			endOfThisMonth = payDate[0];
		}
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(gprs) as gprs from traffic2 where date>=? and date<?";
		Cursor mCursor = db.rawQuery(sql, new String[]{firstDayOfMonth, endOfThisMonth});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					monthGprsValue = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return monthGprsValue;
	}

	@Override
	public String getTodayTrafficOfGprs() {
		String dayGprsValue = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		String today = mDateInfo.getToday();
		sql = "select gprs from traffic2 where date=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{today});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					dayGprsValue = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return dayGprsValue;
	}

	@Override
	public String getLastMonthTrafficOfGprs() {
		String[] payDate = mDateInfo.getPayDate();
		String value = "0";
		String startOfLastMonth = payDate[2];
		String endOfLastMonth = payDate[0];
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(gprs) as gprs from traffic2 where date>=? and date<?";
		Cursor mCursor = db.rawQuery(sql, new String[]{startOfLastMonth, endOfLastMonth});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					value = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return value;
	}

	@Override
	public String getYesterdayGprsTraffic() {
		String yesterdayGprsTraffic = "0";
		String yesterdayDate = mDateInfo.getDateOfYesterday();
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select gprs from traffic2 where date=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{yesterdayDate});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					yesterdayGprsTraffic = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return yesterdayGprsTraffic;
	}

	@Override
	public String getDurationGprsTraffic(String powerOnDate, String today) {
		String durationGprsTraffic = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(gprs) as gprs from traffic2 where date>=? and date<?";
		Cursor mCursor = db.rawQuery(sql, new String[]{powerOnDate, today});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					durationGprsTraffic = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return durationGprsTraffic;
	}

	@Override
	public String getBeforeGprsTraffic(String today) {
		String beforeGprsTraffic = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(gprs) as gprs from traffic2 where date<?";
		Cursor mCursor = db.rawQuery(sql, new String[]{today});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					beforeGprsTraffic = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return beforeGprsTraffic;
	}

	@Override
	public String getHistoryTraffic(String powerOnDate, String maxDate) {
		String historyGprsTraffic = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select sum(gprs) as gprs from traffic2 where date>=? and date<=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{powerOnDate, maxDate});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					historyGprsTraffic = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return historyGprsTraffic;
	}

	@Override
	public String getMaxDate() {
		String maxDate = null;
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select max(date) as date from traffic2";
		Cursor mCursor = db.rawQuery(sql, null);
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					maxDate = mCursor.getString(mCursor.getColumnIndex("date"));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return maxDate;
	}

	@Override
	public String getUidTraffic(String uid) {
		String old3g = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select gprs from uidtraffic where uid=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{uid});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					old3g = mCursor.getString(mCursor.getColumnIndex(GPRS));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return old3g;
	}

	@Override
	public List<String> getEveryDayTraffic() {
		List<String> gprs = new ArrayList<String>();
		SQLiteDatabase db = mDBService.getReadableDatabase();
		sql = "select gprs from traffic2 where date>=? and date <=?";
		Cursor mCursor = db.rawQuery(sql, new String[]{mDateInfo.getFirstDayOfThisMonth(), mDateInfo.getToday()});
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					String value = mCursor.getString(mCursor.getColumnIndex(GPRS));
					gprs.add(value);
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return gprs;
	}

	@Override
	public String getGprs1Traffic(String powerOnDate, String today) {
		String gprs1 = "0";
		SQLiteDatabase db = mDBService.getReadableDatabase();
		String sql = "select sum(gprs1) as gprs1 from traffic2 where date>='" + powerOnDate
					+ "' and date<'" + today + "'";
		Cursor mCursor = db.rawQuery(sql, null);
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					gprs1 = mCursor.getString(mCursor.getColumnIndex("gprs1"));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
		}
		db.close();
		return gprs1;
	}

}
