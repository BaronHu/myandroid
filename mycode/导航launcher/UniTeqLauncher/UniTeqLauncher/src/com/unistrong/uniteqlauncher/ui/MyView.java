package com.unistrong.uniteqlauncher.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.unistrong.uniteqlauncher.MainActivity;
import com.unistrong.uniteqlauncher.R;
import com.unistrong.uniteqlauncher.db.DBService;
import com.unistrong.uniteqlauncher.util.AppInfo;
import com.unistrong.uniteqlauncher.util.Constants;
import com.unistrong.uniteqlauncher.util.LoadingAllApp;

/**
 * 自定义viewgroup,加载APP子view
 * @author Baron.hu
 * */
@SuppressLint("NewApi")
public class MyView extends ViewGroup {
	
	public static final String LOG_TAG = "MyView";
	
	/* set the app icon to be sub view of ViewGroup */
	ImageView appIcon;
	
	/* if the device powers on at the first time, we will use the default Icon */
	Bitmap mBitMap;
	
	/* if user has replaced the default icon, we use this one */
	Drawable icon;
	
	/* for loading app name */
	TextView tv;
	
	DBService mDBService;
	private String pkgName;
	private String clsName;
	private String appName;
	
	/* whether the method initDefault() executed or not */
	//public static boolean isDefault = false;

	public MyView(Context context) {
		super(context);
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * initiation the sub view when the app launched
	 * @param context Context
	 * @return void
	 * */
	private void initDefault(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = inflater.inflate(R.layout.app_view, null);
	    appIcon = (ImageView) v.findViewById(R.id.appicon);
	    tv = (TextView) v.findViewById(R.id.appname);
		try {
			/* if icon and name is null, we will get it from database,
			 * if database has nothing, we will use the default ones */
	        icon = AppInfo.appIcon;
	        appName = AppInfo.appName;
	        Log.d(LOG_TAG, "initDefault()->AppInfo.appName=" + AppInfo.appName);
	        byte[] photo = null;
	        if (icon == null || appName == null) {
	        	//if there are not any icon in cache, we will get icon from DB
	        	mDBService = new DBService(getContext());
	    		SQLiteDatabase db = mDBService.getReadableDatabase();
	    		Cursor mCursor = db.rawQuery(Constants.SELECT_SQL, null);
	    		if (mCursor != null) {
	    			if (mCursor.moveToFirst()) {//just need to query one time
    					pkgName = mCursor.getString(mCursor.getColumnIndex("pkgname"));
    					clsName = mCursor.getString(mCursor.getColumnIndex("clsname"));
    					appName = mCursor.getString(mCursor.getColumnIndex("appname"));
    					photo = mCursor.getBlob(mCursor.getColumnIndex("photo"));
	    			}
	    		}
	    		if (mCursor != null) {
	    			mCursor.close();
	    		}
	    		db.close();
	    		/////////////////////DB operation end/////////////////////////////
	    		
	    		if (appName != null) tv.setText(appName);
	    		
	    		ByteArrayInputStream bais = null;
	    		if (photo != null) {
	    			bais = new ByteArrayInputStream(photo);
	    			appIcon.setImageDrawable(Drawable.createFromStream(bais, "photo"));
	    		} else {
	    			/* use the default icon and name */
		        	mBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_browser);
		    		appIcon.setImageBitmap(mBitMap);
		    		tv.setText(R.string.default_appname);
		    		Log.d(LOG_TAG, "photo is null, use the default one.");
	    		}
	        } else {
	    		appIcon.setImageDrawable(icon);
	    		tv.setText(appName);
	        }
			
			addView(v);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override  
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//l=163,t=0,r=315,b=137
		int childCount = getChildCount();
		if (childCount <= 0) {
			initDefault(getContext());
		} 
		final View child = getChildAt(0);
		child.setVisibility(View.VISIBLE);
		child.measure(r - l, b - t);
		child.layout(20, 25, child.getMeasuredWidth()+30, child.getMeasuredHeight()+25);
//		child.setScaleX(1.2f);
//		child.setScaleY(1.2f);
		float scale = getContext().getResources().getDisplayMetrics().density; 
		/* 获取长按事件,使得在MyView的任何一个位置都能长按 */
		child.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				new AllAppDialog(getContext(), LoadingAllApp.loadApps(getContext()), MyView.this);
				return false;
			}
		});
		
		child.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
					if (!AllAppDialog.hasAdd) {//user replaced icon during this power on time
						Log.d(LOG_TAG, "hasAdd is false");
						if (pkgName == null || clsName == null) {
							Intent in = new Intent();
							in.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
							getContext().startActivity(in);
						} else {
							Intent intent = new Intent();
							intent.setClassName(pkgName, clsName);
							getContext().startActivity(intent);
						}
					} else {
						String pkg_Name = AppInfo.pkgName;
						String cls_Name = AppInfo.clsName;
						ComponentName mComponentName = new ComponentName(pkg_Name, cls_Name);
						Intent i = new Intent();
						if (mComponentName != null) {
							i.setComponent(mComponentName);
							getContext().startActivity(i);
						}
					}
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
}
