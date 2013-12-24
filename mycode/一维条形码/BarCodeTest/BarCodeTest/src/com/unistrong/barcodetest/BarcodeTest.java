package com.unistrong.barcodetest;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author HuBin
 * */
public class BarcodeTest extends Activity implements OnClickListener{

	public static final String LOG_TAG = "BarcodeTest";
	
	private ProgressDialog progress = null;
	private Context mContext;
	
	private Button btnScan;
	private Button btnExit;
	private Button btnUpdate;
	private Button btnClear;
	private TextView text;
	
	private StringBuilder sb; //用来加载第一次进入应用时的数据
	
	private StringBuilder updateSB; //加载更新数据
	
	private int len = 0; //已经保存的文件的长度,用来比较更新数据时判断是否有数据需要更新
	
	private boolean cleared = false; //是否点击了清空数据按钮,用来控制改变更新数据按钮的名称
	
	MyFileObserver mfo = null; //监控文件变化
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BarcodeNative.initUart(0);//初始化串口
		setContentView(R.layout.activity_main);
		mContext = getApplicationContext();
		sb = FileHelper.read(this);
		
		btnScan     = (Button) findViewById(R.id.scan);
		btnExit     = (Button) findViewById(R.id.exit);
		btnUpdate   = (Button) findViewById(R.id.update);
		btnClear    = (Button) findViewById(R.id.clear); 
		
		btnScan  .setOnClickListener(this);
		btnExit  .setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnClear .setOnClickListener(this);
		
		text      = (TextView) findViewById(R.id.textview);
		
//		if (sb != null) {
//			len = sb.length();
//			text.setText(sb.toString());
//		}
		
//		Intent uIntent = new Intent(this, ReadService.class);
//		mContext.startService(uIntent);
		
		mfo = new MyFileObserver(mContext.getFilesDir() + "/data.txt");
		
		new Thread(new ReadThread()).start();
		
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.scan :
			BarcodeNative.scan(1);//开始扫描一维码
			Log.d(LOG_TAG, "scan service started...");
			break;
		case R.id.exit :
//			Intent ioff = new Intent(BarcodeTest.this, ReadService.class);
//			mContext.stopService(ioff);
			BarcodeNative.exit(1);
			Log.d(LOG_TAG, "ReadService has been stopped...");
			finish();
//			ioff = null;
			break;
		case R.id.update :
			String s = btnUpdate.getText().toString();
			if (s.equals(getString(R.string.reload))) {
				 cleared = true;
			} else {
				 cleared = false;
			}
			btnUpdate.setText(R.string.update);
			progress = ProgressDialog.show(BarcodeTest.this, 
					BarcodeTest.this.getString(R.string.working), 
					BarcodeTest.this.getString(R.string.reading_apps), 
					true);
			///////// Handler + Thread 更新UI ///////
        	final Handler handler = new Handler() {
        		public void handleMessage(Message msg) {
        			update();
        			if (progress != null) progress.dismiss();
        			if (updateSB != null) {
        				text.setText(updateSB.toString());
        			}
        		}
        	};
        	new Thread() {
        		public void run() {
        			handler.sendEmptyMessage(0);
        		}
        	}.start();
        	///////////////////////////////////////
        	break;
		case R.id.clear :
			text.setText("");
			/*File file = new File(BarcodeTest.this.getFilesDir() + "/data.txt");
			if (!file.exists()) {
				return;
			} else {
				file.delete();
			}*/
			btnUpdate.setText(R.string.reload);
			break;
		default :
			break;
		}
		
	}
	
	/**
	 * 更新数据
	 * @param
	 * @return void
	 * */
	public void update() {
		if (updateSB != null) {
			len = updateSB.length();
		}
		updateSB = FileHelper.read(this);
		if (updateSB != null) {
			Log.d(LOG_TAG, "updateSB.length=" + updateSB.length() + "; len="
					+ len);
			if (updateSB.length() == len) {
				if (!cleared) {
					Toast.makeText(BarcodeTest.this, R.string.toast,
							Toast.LENGTH_LONG).show();
				}
			}
		} else {
			if (!cleared) {
				Toast.makeText(BarcodeTest.this, R.string.toast,
						Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	public StringBuilder refresh() {
		return FileHelper.read(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bool = false;
//		mfo.stopWatching();
		Log.d(LOG_TAG, "----------onDestroy()-----------");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "----------onPause()-----------");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, "THREAD NAME IS : " + Thread.currentThread().getName());
//		mfo.startWatching();//启动文件监听
		Log.d(LOG_TAG, "----------onResume()-----------");
	}
	
	public class MyFileObserver extends FileObserver {

		public MyFileObserver(String path) {
			super(path);
		}

		@Override
		public void onEvent(int event, String path) {
			switch (event) {
			case android.os.FileObserver.CREATE:
				// 文件被创建
				Log.d("FileObserver", "---file create---");
				break;
			case android.os.FileObserver.OPEN:
				// 文件被打开
				//Log.d("FileObserver", "---file open---");
				break;
			case android.os.FileObserver.ACCESS:
				// 打开文件后，读文件内容操作
				//Log.d("FileObserver", "---file access---");
				break;
			case android.os.FileObserver.MODIFY:
				Log.d("FileObserver", "---file MODIFY---");
				text.post(new Runnable() {
					
					@Override
					public void run() {
						StringBuilder ssb = refresh();
						if(ssb != null) {
							text.setText(ssb.toString());
						}
					}
				});
				break;
			}

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK){
			Log.d(LOG_TAG, "back key pressed...");
//			Intent uIntent = new Intent(this, ReadService.class);
//			mContext.stopService(uIntent);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private boolean bool = true;
	public class ReadThread implements Runnable{

		@Override
		public void run() {
			while (bool) {
				synchronized (this) {
					try {
						final String str = BarcodeNative.get(3);//获取一维码扫描结果
					  Log.d(LOG_TAG, "str=" + str);					
					  if (str != null) {
						  FileHelper.write(str, mContext);
					  } 
						
						//读取一次就刷新一次TextView
						text.post(new Runnable() {
							
							@Override
							public void run() {
								text.append(str + "\n");
							}
						});
				
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}

}
