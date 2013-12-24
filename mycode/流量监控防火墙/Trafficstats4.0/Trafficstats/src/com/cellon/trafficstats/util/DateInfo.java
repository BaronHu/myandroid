package com.cellon.trafficstats.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cellon.trafficstats.db.DBService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Baron.Hu
 * Handling the date
 * */
public class DateInfo {
	
	private SimpleDateFormat sdf;
	private static DBService dbService;
	private SharedPreferences mSharedPreferences;

	public DateInfo(Context context) {
		sdf = new SimpleDateFormat("yyyyMMdd");
		dbService = new DBService(context);
		mSharedPreferences = context.getSharedPreferences("setting", Activity.MODE_WORLD_READABLE);
	}
	
	//get current date
	public String getToday() {
		Date d = new Date();
		String date = sdf.format(d);
		return date;
	}
	
	public String getCurrentMonth() {
		Date d = new Date();
		String t = sdf.format(d);
		String m = t.substring(4, 6);
		return m;
	}
	
	public String getCurrentYear() {
		Date d = new Date();
		String t = sdf.format(d);
		String y = t.substring(0, 4);
		return y;
	}
	
	//make sure if the date is today
	public boolean isToday() {
		String today = getToday();
		List<String> dateList = getDateFromDatabase();
		if (dateList != null && dateList.size() > 0) {
			if (dateList.contains(today)) {
				return true;
			}
				
		} else {
			return false;
		}
		return false;
	}
	
	//make sure if it is in the save month
	public boolean isThisMonth() {
		String date = getToday();
		List<String> dateList = getDateFromDatabase();
		for (int i = 0; i < dateList.size(); i++) {
			if (dateList != null) {
				if (dateList.get(i).substring(0,6).equals(date.substring(0,6))) 
					return true;
			}
		}
		return false;
	}
	
	//get all of date from database
	public static List<String> getDateFromDatabase() {
		List<String> dateList = null;
		String date;
		String sql = "select date from traffic";
		SQLiteDatabase db = dbService.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql, null);
		if (mCursor != null) {
			dateList = new ArrayList<String>();
			if (mCursor.moveToFirst()) {
				do {
					date = mCursor.getString(mCursor.getColumnIndex("date"));
					if (date != null) {
						dateList.add(date);
					}
				} while (mCursor.moveToNext());
			} 
			mCursor.close();
		} 
		return dateList;
	}
	
	//get the date of yesterday
	public String getDateOfYesterday() {
		Calendar c = Calendar.getInstance();
		long t = c.getTimeInMillis();
		long l = t - 24 * 3600 * 1000;
		Date d = new Date(l);
		String s = sdf.format(d);
		return s;
	}
	
	//get first day of last month
	public String getFirstDayOfLastMonth() {
		String str = "";  
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE,1); //set the date to be 1
		lastDate.add(Calendar.MONTH,-1);//reduce a month to be last month
//		lastDate.add(Calendar.DATE,-1);//reduce one day to be the first day of last month
			         
		str=sdf.format(lastDate.getTime()); 
		return str;
	}
	
	// get last day of last month
	public String getLastDayOfLastMonth() {
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 
		lastDate.add(Calendar.MONTH, -1);//
		lastDate.roll(Calendar.DATE, -1);// 
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	//get the first day of this month
	public String getFirstDayOfThisMonth() {
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE,1);//
//		lastDate.add(Calendar.MONTH,-1);//
//		lastDate.add(Calendar.DATE,-1);//
			         
		str=sdf.format(lastDate.getTime()); 
		return str;
	}
	
	//get the last day of this month
	public String getLastDayOfThisMonth() {
		String str = "";  
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE,1);//
		lastDate.add(Calendar.MONTH,1);//
		lastDate.add(Calendar.DATE,-1);//
			         
		str = sdf.format(lastDate.getTime()); 
		return str;
	}
	
	//get pay date
	public String[] getPayDate() {
		Calendar c = Calendar.getInstance();
		String [] str = new String[3];
		String curPayDate, nextPayDate, lastPayDate;
		curPayDate = mSharedPreferences.getString("payoff_date", "1");
		int d = 1;
		if (curPayDate != null) {
			d = Integer.parseInt(curPayDate);
		}		
		c.set(Calendar.DATE,d);
		c.add(Calendar.MONTH,0);
		curPayDate = sdf.format(c.getTime());
		str[0] = curPayDate;//this month's pay date
		
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.DATE,d);
		c2.add(Calendar.MONTH,1);
		nextPayDate = sdf.format(c2.getTime());
		str[1] = nextPayDate;//next month's pay date
		
		Calendar c3 = Calendar.getInstance();
		c3.set(Calendar.DATE,d);
		c3.add(Calendar.MONTH,-1);
		lastPayDate = sdf.format(c3.getTime());
		str[2] = lastPayDate;//last month's pay date
		return str;
		
	}
	
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		}
		return false;
	}

}
