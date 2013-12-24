package com.unistrong.uniteqlauncher.db;

import com.unistrong.uniteqlauncher.util.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Database Helper
 * 创建数据库,表
 * @author Baron.hu
 * */
public class DBService extends SQLiteOpenHelper {
	
	public DBService(Context context) {
		this(context, Constants.DATABASE_NAME, null, Constants.VERSION);
	}

	public DBService(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Constants.CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion > oldVersion){
			db.execSQL(Constants.DROP_TABLE_SQL);
		} else {
			return;
		}
		onCreate(db);
	}

}
