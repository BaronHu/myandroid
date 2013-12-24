package com.cellon.trafficstats.provider;

import com.cellon.trafficstats.db.DBService;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class TrafficProvider extends ContentProvider {
	
	public static final String AUTHORITY = "com.cellon.trafficstats.provider";
	public static final Uri URI = Uri.parse("content://" + AUTHORITY);
	private static final UriMatcher uriMatcher;
	private static final int DAY_TRAFFIC_VALUE = 1;
	private static final int MONTH_TRAFFIC_VALUE = 2;
	
	private DBService dbService;
	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		uriMatcher.addURI(AUTHORITY, "day_traffic", DAY_TRAFFIC_VALUE);
		uriMatcher.addURI(AUTHORITY, "month_traffic", MONTH_TRAFFIC_VALUE);
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		dbService = new DBService(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbService.getReadableDatabase();
		Cursor cursor = null;
		switch (uriMatcher.match(uri)) {
		case DAY_TRAFFIC_VALUE :
			cursor = db.query("traffic", projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case MONTH_TRAFFIC_VALUE :
			String sql = "select sum(gprs) as gprs from traffic where date>=? and date<?";
			cursor = db.rawQuery(sql, selectionArgs);
			break;
		default :
			break;
		}
		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
