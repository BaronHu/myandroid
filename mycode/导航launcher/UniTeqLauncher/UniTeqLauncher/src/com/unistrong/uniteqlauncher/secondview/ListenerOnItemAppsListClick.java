package com.unistrong.uniteqlauncher.secondview;

import java.util.HashMap;

//import com.littletools.main.datas.DataHelper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListenerOnItemAppsListClick implements OnItemClickListener {

	private Activity mThis;
	private AdapterMyView mAdapt;
	private MyGridView mMyView;
	private DialogApps mApps;
	
	public ListenerOnItemAppsListClick(Activity a, MyGridView mv, DialogApps d){ 

		this.mThis = a;
		this.mMyView = mv;
		this.mApps = d;

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		PackageInfo mPI = (PackageInfo)arg0.getItemAtPosition(arg2);
		String label = (String) mPI.applicationInfo.loadLabel(mThis.getPackageManager());
		Drawable icon = mPI.applicationInfo.loadIcon(mThis.getPackageManager());

		ActivityInfo mAInfo = null;

		try{
			
			mAInfo = mPI.activities[0];
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		if(mAInfo == null){
			Toast.makeText(mThis, "无法添加该程序!", Toast.LENGTH_SHORT).show();
			return;
		}			
		
		
		String AName = mAInfo.name;
		String PName = mPI.packageName;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		//程序名称
		map.put("label", label);
		//程序图标
		map.put("icon", icon);
		//程序包名
		map.put("AName", AName);
		//程序启动Activity的名称
		map.put("PName", PName);

		//把该程序的HashMap添加到MyView的Adapter中
		mAdapt = (AdapterMyView) mMyView.getAdapter();
		mAdapt.addItem(map);
		
		//AppsDialog消失
		mApps.dismiss();
		
		//将数据插入数据库
		DataHelper.getInstance(mThis).insertData(map);
		
	}


}