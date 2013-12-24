package com.unistrong.uniteqlauncher.secondview;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class DataHelper extends SQLiteOpenHelper {

	public final static String DB_NAME = "LittleDB";
	public final static int DB_VERSION = 2;
	private static DataHelper instance = null;
	private SQLiteDatabase db;
	private final boolean WRITABLE = true;
	private final boolean READABLE = false;
	//根据不同的上下文context创建不同的数据库
	private DataHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	public static DataHelper getInstance(Context context){
		if(instance == null){
			instance = new DataHelper(context);
		}
		return instance;
	}
	
	private void openDatabase(boolean boo){
		if(db == null && boo == WRITABLE){
			db = this.getWritableDatabase();
		}else if(db == null && boo == READABLE) {
			db = this.getReadableDatabase();
		}
	}
	@Override
	public void onCreate(SQLiteDatabase db) {

		StringBuffer twoView = new StringBuffer();

		
		twoView.append("create table twoview(_id integer primary key autoincrement,")
						   .append("pname text,")
						   .append("aname text,")
						   .append("label text,")						  
						   .append("icon blob);");				
				
		db.execSQL(twoView.toString()); 	
		
		return;
	}
	


	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql1 = "drop table if exists twoview;";

		db.execSQL(sql1);

		onCreate(db);
		
	}
	
	
	public void insertData(HashMap<String, Object> map) {
		//打开数据库
		openDatabase(WRITABLE);
		
		String pName = map.get("PName").toString();
		String aName = map.get("AName").toString();
		String label = map.get("label").toString();
		Drawable icon = (Drawable) map.get("icon");
		Bitmap bmp = ((BitmapDrawable)icon).getBitmap();
		ByteArrayOutputStream os = new ByteArrayOutputStream();		
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
		
		ContentValues values = new ContentValues();
		values.put("pname", pName);
		values.put("aname", aName);
		values.put("label", label);
		values.put("icon", os.toByteArray());
		
		db.insert("twoview", null, values);		
		
	}
	
	public void deleteData() {
		
		
		
	}
	public Cursor queryData() {

		openDatabase(READABLE);
		
		return db.query("twoview", null, null, null, null, null, "_id asc");
		
	}
	
	
	
	
}
