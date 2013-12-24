package com.unistrong.uniteqlauncher.secondview;

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
public class MyView2 extends ViewGroup {
	
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

	public MyView2(Context context) {
		super(context);
	}

	public MyView2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override  
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
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
