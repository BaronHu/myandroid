package com.unistrong.uniteqlauncher.secondview;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.unistrong.uniteqlauncher.R;

public class ViewTwo {
	
	private Activity mThis;
	private View mView;
	
	//自定义的GridView，可以拖动
	public static MyGridView mMyView;
	private ArrayList<HashMap<String, Object>> itemList;
	private HashMap<String, Object> itemMap;
	
    public ViewTwo(Activity a, View v){	  
    	
    	//初始化方法
		init(a, v);		
		//绑定监听器
		addListeners();
		
    }
	
	private void addListeners() {
		
		//监听MyGridView的item点击事件
		mMyView.setOnItemClickListener(new ListenerOnItemTwoViewClick(mThis, mMyView));
				
	}

	private void init(Activity a, View v) {
    	mThis = a;
    	mView = v;		
		//实例化各控件
    	mMyView = (MyGridView) mView.findViewById(R.id.mGridview);
		
		itemList = new ArrayList<HashMap<String, Object>>();

		itemMap = new HashMap<String, Object>();
		itemMap.put("label", "添加");
		itemMap.put("icon", mThis.getResources().getDrawable(R.drawable.ic_add));
				
		itemList.add(itemMap);
		
		Cursor cursor = DataHelper.getInstance(mThis).queryData();
		
		while(cursor.moveToNext()){

			String pName = cursor.getString(cursor.getColumnIndex("pname"));
			String aName = cursor.getString(cursor.getColumnIndex("aname"));
			String label = cursor.getString(cursor.getColumnIndex("label"));
			byte[] byteIcon = cursor.getBlob(cursor.getColumnIndex("icon"));
			ByteArrayInputStream inputStream=new ByteArrayInputStream(byteIcon);				   
    		Bitmap bmpIcon = BitmapFactory.decodeStream(inputStream);	
    		Drawable icon = new BitmapDrawable(bmpIcon);
			
    		itemMap = new HashMap<String, Object>();
    		//程序名称
    		itemMap.put("label", label);
    		//程序图标
    		itemMap.put("icon", icon);
    		//程序包名
    		itemMap.put("AName", aName);
    		//程序启动Activity的名称
    		itemMap.put("PName", pName);
    		
    		itemList.add(itemMap);
			
		}
		mMyView.setAdapter(new AdapterMyView(mThis, itemList));
	}

}