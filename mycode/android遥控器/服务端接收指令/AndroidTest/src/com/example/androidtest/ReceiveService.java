package com.example.androidtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/* a service for receiving command from client */
public class ReceiveService extends Service {
	
	public static final String TAG = "ReceiveService";
	
	public static final String CONN_ACTION = "com.yayi.rc.CONNECTED";
	public static final String DIS_CONN_ACTION = "com.yayi.rc.DISCONNECTED";
	
	private static final IntentFilter filter = new IntentFilter();
	
	private Context mContext;
	private boolean isConnected = false;
	
	private View viewMouse; // mouse pointer
	private WindowManager wm;
	private WindowManager.LayoutParams params;
	private ImageView iv;
	private int x = 0;
	private int y = 0;
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		
		//we may need broadcast to notify the service 
		//of connection or disconnection
		filter.addAction(CONN_ACTION);
		filter.addAction(DIS_CONN_ACTION);
		mContext.registerReceiver(connectionReceiver, filter);
		
		Log.d("baron", "ReceiveService->onCreate()");
	}
	
	/**
	 * initiate the view of vitrual mouse
	 * @param
	 * @return
	 * */
	public void initView() {
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		iv = new ImageView(mContext);
		iv.setImageResource(R.drawable.arraw);
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		x += 20;
		y += 20;
		params.x = x;
		params.y = y;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//if (wm != null)wm.removeView(iv);
		try {
			mContext.unregisterReceiver(connectionReceiver);
		} catch (Exception e) {
			Log.d("baron", TAG + ":" + e.getMessage());
		}
		Log.d("baron", "ReceiveService onDestroy()...");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//initView();
		new Thread(new ReadThread()).start();
		Log.d("baron", "ReceiveService onStartCommand()...");
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * multi thread to receive message from Socket client
	 * these messages include an action of down/up/move
	 * */
	public class ReadThread implements Runnable {
		ServerSocket ss = null;
		Socket s = null;
		BufferedReader br = null;
		//BufferedWriter bw = null;
		String str = null;

		@Override
		public void run() {
			try {
				ss = new ServerSocket(8999);
				Log.d("baron", "ServerSocket is null ? " + (ss == null));
				
				while (true) {
					synchronized (this) {
						try {
							s = ss.accept();
							Log.d("baron", "ServerSocket done.");
							br = new BufferedReader(new InputStreamReader(
									s.getInputStream()));
							str = br.readLine();
							Log.d("baron", "message from client is : " + str);
							Instrumentation ins = new Instrumentation();
							if (str != null && !str.contains(":")){
								ins.sendKeyDownUpSync(Integer.parseInt(str));
								Log.d("baron", "oh yeah, shend by instrumention.");
							}
							
							Log.d("baron", "send keycode out.");
							
							//receive move event message
							if (str != null) {
								if (str.contains(":")) {
									String[] move = str.split(":");
									float x = Float.parseFloat(move[0]);
									float y = Float.parseFloat(move[1]);
									float x1 = Float.parseFloat(move[2]);
									float y1 = Float.parseFloat(move[3]);
									
									Log.d("baron", "x=" + x + "; y=" + y + "; x1=" + x1 + "; y1=" + y1);
									
									MotionEvent e = MotionEvent.obtain(SystemClock.uptimeMillis(), 
											SystemClock.uptimeMillis(), 
											MotionEvent.ACTION_DOWN, x, y, 0);
									ins.sendPointerSync(e);
									
									e = MotionEvent.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(), 
											MotionEvent.ACTION_MOVE, x, y, 0);
									ins.sendPointerSync(e);
									
									e = MotionEvent.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(), 
											MotionEvent.ACTION_MOVE, x, y, 0);
									ins.sendPointerSync(e);
									
									e = MotionEvent.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(), 
											MotionEvent.ACTION_MOVE, x1, y1, 0);
									ins.sendPointerSync(e);
									
									e = MotionEvent.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(), 
											MotionEvent.ACTION_MOVE, x1, y1, 0);
									ins.sendPointerSync(e);
									
									e = MotionEvent.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(), 
											MotionEvent.ACTION_UP, x1, y1, 0);
									ins.sendPointerSync(e);
								}
							}
							
							
							if (str != null && str.equalsIgnoreCase("end")) {
								close();
								break;
							}
							br.close();
					
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
		
		public void close() {
			try {
				if (br != null) br.close();
				//if (bw != null) bw.close();
				if (ss != null) ss.close();
				if (s != null)  s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void doInject(String cmd){
		try {
			java.lang.Process p = Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("baron", e.getMessage());
		}
	}
	
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("baron", TAG + ": action=" + action);
			if (action.equals(CONN_ACTION)) {
				//display mouse and transfer the operation command
			} else if (action.equals(DIS_CONN_ACTION)) {
				//hide the mouse and stop service
				//stopSelf();
			}
			
		}
	};

}
