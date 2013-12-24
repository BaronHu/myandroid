package com.unistrong.uniteqlauncher.secondview;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListenerOnItemTwoViewClick implements OnItemClickListener {
	
	private static Activity mThis;
	private static MyGridView mMyView;
	public static DialogApps mApps;
	public static ProgressDialog mPD;
	public static Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//当加载完成之后，会接收到来自子线程的消息，然后在主线程中更新UI
				//1、显示应用列表Dialog
			mApps.show();
				//2、使ProgressDialog消失			
			mPD.dismiss();
				//3、为列表绑定监听器
			mApps.setOnItemClickListener(new ListenerOnItemAppsListClick(mThis, mMyView, mApps));
			
		}
		
	};
	
	@SuppressWarnings("static-access")
	public ListenerOnItemTwoViewClick(Activity a, MyGridView mv){ 
		
		this.mThis = a;
		this.mMyView = mv;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		
		HashMap<String, Object> itemMap = (HashMap<String, Object>)arg0.getItemAtPosition(arg2);
		
		String label = itemMap.get("label").toString();
		
		if(label != null && label.equals("添加")){
						
			//显示所有程序列表的对话框
			displayAppsDialog();
												
		}else{

			String PName = itemMap.get("PName").toString();
			String AName = itemMap.get("AName").toString();	
			System.out.println(PName);
			System.out.println(AName);
			if(PName.equals("com.littletools")){
				Toast.makeText(mThis, "sorry, has lanuchered", Toast.LENGTH_LONG).show();	
				return;
			}
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(PName, AName));
			try{
				mThis.startActivity(intent);
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(mThis, "sorry", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}


	private void displayAppsDialog() {
		
		//1、先显示ProgressDialog,增加用户体验，因为加载过程需要时间
		mPD = ProgressDialog.show(mThis, "请稍候...", "正在加载应用程序...", true, false);
		
		//2.1、开启子线程，加载所有应用程序。
		//2.2、加载完成之后，通知主线程更新UI
		mApps = new DialogApps(mThis);	
		
		
	}

}