package com.unistrong.uniteqlauncher.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.util.Log;

/**
 * This class is a tool of Date handler
 * @author Baron.hu
 */
@SuppressLint("SimpleDateFormat")
public class Utils {
	
	public static final String TAG = "Utils";
	
	/* get hour */
	public static int getHour(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date d = new Date();
	    String s = sdf.format(d);
	    int hour = Integer.parseInt(s.substring(0, 2));
	    Log.d(TAG, "hour=" + hour);
	    return hour;
	
	}
	
	/* get minutes */
	public static int getMin() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date d = new Date();
	    String s = sdf.format(d);
	    int mi = Integer.parseInt(s.substring(3));
	    Log.d(TAG, "mi=" + mi);
	    return mi;
	}
	
	/* get current date */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Date d = new Date();
		String s = sdf.format(d);
		Log.d(TAG, "date=" + s);
		return s;
	}
	
	/* get week of date */
	public static String getWeekOfDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();
		String s = sdf.format(d);
		Log.d(TAG, "week = " + s);
		return s;
	}
	
	/**
	 * SD卡是否存在
	 * @return boolean true 存在
	 *                 false 不存在
	 * */
	public static boolean isSdExist() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
	
}
