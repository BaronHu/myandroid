package com.yayi.rc;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.yayi.rc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/* a simple controller of transfer data */
public class ControllerActivity extends Activity implements OnClickListener,OnTouchListener,OnGestureListener,OnDoubleTapListener{
	
	public static final String TAG = "ControllerActivity";
	
	private String hostAddress ;
	
	private Button btn_left;
	private Button btn_right;
	private Button btn_up;
	private Button btn_down;
	private Button btn_ok;
	private Button btn_power;
	private Button btn_info;
	private Button btn_home;
	private Button btn_search;
	private Button btn_back;
	private Button btn_option;//what it is?
	private Button btn_fast_back;
	private Button btn_fast_forward;
	private Button btn_pause;
	private Button btn_previous;
	private Button btn_next;
	
	private LinearLayout touchLayout;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controller);
		
		Intent data = getIntent();
		hostAddress = data.getStringExtra("host");
		
		touchLayout = (LinearLayout) findViewById(R.id.touchLayout);
		touchLayout.setOnTouchListener(this);
		touchLayout.setLongClickable(true);
		
		tv = (TextView) findViewById(R.id.tv);
		
		btn_left         = (Button) findViewById(R.id.btn_left);
		btn_right        = (Button) findViewById(R.id.btn_right);
		btn_up           = (Button) findViewById(R.id.btn_up);
		btn_down         = (Button) findViewById(R.id.btn_down);
		btn_ok           = (Button) findViewById(R.id.btn_ok);
		btn_power        = (Button) findViewById(R.id.btn_power);
		btn_info         = (Button) findViewById(R.id.btn_info);
		btn_home         = (Button) findViewById(R.id.btn_home);
		btn_search       = (Button) findViewById(R.id.btn_search);
		btn_back         = (Button) findViewById(R.id.btn_back);
		btn_option       = (Button) findViewById(R.id.btn_option);
		btn_fast_back    = (Button) findViewById(R.id.btn_fast_back);
		btn_fast_forward = (Button) findViewById(R.id.btn_fast_forward);
		btn_pause        = (Button) findViewById(R.id.btn_pause);
		btn_previous     = (Button) findViewById(R.id.btn_previous);
		btn_next         = (Button) findViewById(R.id.btn_next);
		
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_up.setOnClickListener(this);
		btn_down.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_power.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_home.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_option.setOnClickListener(this);
		btn_fast_back.setOnClickListener(this);
		btn_fast_forward.setOnClickListener(this);
		btn_pause.setOnClickListener(this);
		btn_previous.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		
		
		
	}
	
	public void sendMsg(final String msg) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					Socket client = new Socket();
					Log.d("baron", "Opening client socket - ");
					client.bind(null);
					client.connect((new InetSocketAddress(hostAddress, 8999)));
					Log.d("baron", "Client socket - " + client.isConnected());
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					bw.write(msg);
					bw.flush();
					client.close();
					bw.close();
					Log.d("baron", msg + " send ok.");

				} catch (Exception e) {
					Log.e("baron", e.getMessage());
				} finally {
				}
				
			}
			
		};
		new Thread(r).start();
	}
	
	public void sendMoveMsg(final float x, final float y, final float x1, final float y1) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					Socket client = new Socket();
					Log.d("baron", "Opening client socket - ");
					client.bind(null);
					client.connect((new InetSocketAddress(hostAddress, 8999)));
					Log.d("baron", "Client socket - " + client.isConnected());
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					bw.write(x + ":" + y + ":" + x1 + ":" + y1);
					bw.flush();
					client.close();
					bw.close();
					Log.d("baron", x + " & " + y + " & " + x1 + " & " + y1 + " send ok.");

				} catch (Exception e) {
					Log.e("baron", e.getMessage());
				} finally {
				}
				
			}
			
		};
		new Thread(r).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			sendMsg("" + KeyEvent.KEYCODE_DPAD_LEFT);
			break;
		case R.id.btn_right:
			sendMsg("" + KeyEvent.KEYCODE_DPAD_RIGHT);
			break;
		case R.id.btn_up:
			sendMsg("" + KeyEvent.KEYCODE_DPAD_UP);
			break;
		case R.id.btn_down:
			sendMsg("" + KeyEvent.KEYCODE_DPAD_DOWN);
			break;
		case R.id.btn_ok:
			sendMsg("" + KeyEvent.KEYCODE_DPAD_CENTER);
			break;
		case R.id.btn_power:
			sendMsg("" + KeyEvent.KEYCODE_POWER);
			break;
		case R.id.btn_info:
			sendMsg("" + KeyEvent.KEYCODE_APP_SWITCH);
			break;
		case R.id.btn_home:
			sendMsg("" + KeyEvent.KEYCODE_HOME);
			break;
		case R.id.btn_search:
			sendMsg("" + KeyEvent.KEYCODE_SEARCH);
			break;
		case R.id.btn_back:
			sendMsg("" + KeyEvent.KEYCODE_BACK);
			break;
		case R.id.btn_option:
			sendMsg("" + KeyEvent.KEYCODE_MENU);
			break;
		case R.id.btn_fast_back:
			sendMsg("" + KeyEvent.KEYCODE_MEDIA_REWIND);
			break;
		case R.id.btn_fast_forward:
			sendMsg("" + KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
			break;
		case R.id.btn_pause:
			sendMsg("" + KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
			break;
		case R.id.btn_previous:
			sendMsg("" + KeyEvent.KEYCODE_MEDIA_PREVIOUS);
			break;
		case R.id.btn_next:
			sendMsg("" + KeyEvent.KEYCODE_MEDIA_NEXT);
			break;
		default:
			break;
		}
		
	}
	
	private float touchMoveX = 0;
	private float touchMoveY = 0;
	/* implements method from OnTouchListener */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			Log.d("baron", TAG + " MontionEevent.ACTION_MOVE");
			if (touchMoveX == 0) {
				touchMoveX = event.getX();
				touchMoveY = event.getY();
			} else {
				sendMoveMsg(touchMoveX, touchMoveY, event.getX(), event.getY());
				Log.d("baron",
						"onTouch DOWN : x = " + (event.getX() - touchMoveX)
								+ "; y = " + (event.getY() - touchMoveY));
				tv.setText("onTouch : x = " + (event.getX() - touchMoveX)
						+ "; y = " + (event.getY() - touchMoveY));
				//touchMoveX = event.getX();
				//touchMoveY = event.getY();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d("baron", TAG + " MontionEevent.ACTION_UP");
			//sendMoveMsg(event.getX(), event.getY());
			touchMoveX = event.getX();
			touchMoveY = event.getY();
			Log.d("baron", "onTouch UP : x = " + touchMoveX + "; y = "
					+ touchMoveY);
		} else {
			touchMoveX = 0;
			touchMoveY = 0;
		}
		return false;
	}
	/* end of implements OnTouchListener */
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("baron", TAG + " onTouchEvent x=" + event.getX() + "; y=" + event.getY());
		return super.onTouchEvent(event);
	}

	/* implements method from OnGestureListener */
	@Override
	public boolean onDown(MotionEvent event) {
		Log.d("baron", "onDown()");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY) {
		Log.d("baron", "onFling()...");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent evnet) {
		Log.d("baron", "onLongPress()..");
		
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
			float distanceY) {
		Log.d("baron", "onScroll()...");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		Log.d("baron", "onShowPress()...");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		Log.d("baron", "onSingleTapUp()....");
		return false;
	}
	/* end of implements */

	/* implements method from OnDoubleTapListener */
	@Override
	public boolean onDoubleTap(MotionEvent event) {
		Log.d("baron", "onDoubleTap()");
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {
		Log.d("baron", "onDoubleTapEvent()");
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		Log.d("baron", "onSingleTapConfirmed()");
		return false;
	}
	/* end of implements of OnDoubleTapListener */

}
