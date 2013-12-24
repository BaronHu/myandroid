package com.cellon.trafficstats.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Baron.Hu
 * */
public class DBService extends SQLiteOpenHelper {
	
	private final static int VERSION = 1;
	private final static String DATABASE_NAME = "traffic.db";
	
	public DBService(Context context) {
		this(context, DATABASE_NAME, null, VERSION);
	}

	public DBService(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE [traffic]("
			+ "[_id] INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "[date] VACHAR(20) NOT NULL,"
			+ "[gprs] VACHAR(20),"
			+ "[wifi] VACHAR(20),"
			+ "[gprs1] VACHAR(20),"
			+ "[backup] VARCHAR(20))";
		
		db.execSQL(sql);
		
		sql = "CREATE TABLE [traffic2]("
			+ "[_id] INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "[date] VACHAR(20) NOT NULL,"
			+ "[gprs] VACHAR(20),"
			+ "[wifi] VACHAR(20),"
			+ "[gprs1] VACHAR(20),"
			+ "[total] VARCHAR(20))";
		
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion > oldVersion){
			db.execSQL("DROP TABLE IF EXISTS[traffic]");
			db.execSQL("DROP TABLE IF EXISTS[traffic2]");
		} else {
			return;
		}
		onCreate(db);
	}
	
	public Cursor queryTraffic(String sql, String[] selectionArgs) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sql, selectionArgs);
		return c;
	}
	
	public void execSQL(String sql, Object[] args) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql, args);
		db.close();
	}
	
}
