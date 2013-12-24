/*
 ============================================================================
 Name        : MainActivity.java
 Author      : Baron.Hu
 Version     : 1.0
 Copyright   : all copyright is reserved by Unistrong
 Description : Uniteq-Launcher Main UI Interface
 ============================================================================
 */

package com.unistrong.uniteqlauncher;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unistrong.uniteqlauncher.helper.MusicHelper;
import com.unistrong.uniteqlauncher.ui.AllAppDialog;
import com.unistrong.uniteqlauncher.ui.DateView;
import com.unistrong.uniteqlauncher.ui.MyView;
import com.unistrong.uniteqlauncher.util.LoadingAllApp;
import com.unistrong.uniteqlauncher.util.Utils;

public class MainActivity extends Activity {

	public static final String LOG_TAG = "MainActivity";
	
	private MusicHelper mMusicHelper;
	
	public boolean isLaunchered = false;

	private MyView navigation;
	private ImageButton bt;
	private ImageButton fm;
	private ImageButton photo;
	private ImageButton video;
	private DateView date_time;
	private ImageButton setting;
	private ImageButton internet;
	
	/* music player components */
	private ImageButton preBtn;
	private ImageButton playBtn;
	private ImageButton nextBtn;
	private TextView musicInfo;
	
	private LinearLayout leftLayout;
	private LinearLayout rightLayout;
	private LinearLayout mainLayout;

	private List<ResolveInfo> apps;
	
	private AlertDialog menuDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMusicHelper = new MusicHelper(MainActivity.this);
		setupView();
		isLaunchered = true;
		
		////////////////////////////  Test density  /////////////////////////////
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();  // ÆÁÄ»¿í£¨ÏñËØ£¬Èç£º480px£©  
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // ÆÁÄ»¸ß£¨ÏñËØ£¬Èç£º800px£©  
		Log.d(LOG_TAG, "getDefaultDisplay screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);  
		  
		// »ñÈ¡ÆÁÄ»ÃÜ¶È
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();  
		  
		float density  = dm.density;    // ÆÁÄ»ÃÜ¶È£¨ÏñËØ±ÈÀý£º0.75/1.0/1.5/2.0£©  
		int densityDPI = dm.densityDpi; // ÆÁÄ»ÃÜ¶È£¨Ã¿´çÏñËØ£º120/160/240/320£©
		float xdpi = dm.xdpi;             
		float ydpi = dm.ydpi;  
		  
		Log.d(LOG_TAG, "DisplayMetrics xdpi=" + xdpi + "; ydpi=" + ydpi);  
		Log.d(LOG_TAG, "DisplayMetrics density=" + density + "; densityDPI=" + densityDPI);  
		////////////////////////////////////////////////////////////////////////
		
	}
	
	public void setupView() {
		setContentView(R.layout.activity_main1);
		apps = LoadingAllApp.loadApps(getApplicationContext());
		
		final View musicview = (View) findViewById(R.id.musicview);
		musicview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				performAnimation(musicview);
				
			}
		});

		navigation = (MyView) findViewById(R.id.navigation);
		bt         = (ImageButton) findViewById(R.id.bt);
		fm         = (ImageButton) findViewById(R.id.fm);
		photo      = (ImageButton) findViewById(R.id.photo);
		video      = (ImageButton) findViewById(R.id.video);
		date_time  = (DateView) findViewById(R.id.date_time);
		internet   = (ImageButton) findViewById(R.id.internet);
		setting    = (ImageButton) findViewById(R.id.setting);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		leftLayout = (LinearLayout) findViewById(R.id.left_layout);
		rightLayout= (LinearLayout) findViewById(R.id.right_layout);		
		musicInfo  = (TextView) findViewById(R.id.music_info);
		preBtn     = (ImageButton) findViewById(R.id.previousBtn);
		playBtn    = (ImageButton) findViewById(R.id.playBtn);
		nextBtn    = (ImageButton) findViewById(R.id.nextBtn);
		
		musicInfo.setText(mMusicHelper.getMusicName());
		
		if (mMusicHelper.isPlaying()) {
			playBtn.setImageDrawable(getResources().getDrawable(R.drawable.stop));
		} else {
			playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play));
		}
		
		preBtn.setOnClickListener(musicListener);
		playBtn.setOnClickListener(musicListener);
		nextBtn.setOnClickListener(musicListener);
		
		/* long press of GroupView */
		navigation.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				new AllAppDialog(MainActivity.this, apps, navigation);
				return false;
			}
		});
		
		navigation.setOnClickListener(btnlistener);
		bt.setOnClickListener(btnlistener);
		fm.setOnClickListener(btnlistener);
		photo.setOnClickListener(btnlistener);
		video.setOnClickListener(btnlistener);
		date_time.setOnClickListener(btnlistener);
		internet.setOnClickListener(btnlistener);
		setting.setOnClickListener(btnlistener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			unregisterReceiver(date_time_changed_receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "---onPause()---");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mMusicHelper == null) mMusicHelper = new MusicHelper(MainActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_DATE_CHANGED);
		filter.addAction("com.unistrong.uniteqlauncher.AUTO_PLAY_NEXT_MUSIC");
		registerReceiver(date_time_changed_receiver, filter);
		if (isLaunchered) {
			setupView();
		}
		Log.d(LOG_TAG, "---onResume()---");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMusicHelper.release();
		Log.d(LOG_TAG, "---onDestroy()---");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(LOG_TAG, "keycode=" + keyCode);
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private Button.OnClickListener btnlistener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mIntent = new Intent();
			switch (v.getId()) {
			case R.id.navigation :
				mIntent.setClassName("com.chartcross.gpstestplus",
						"com.chartcross.gpstestplus.GPSTestPlus");
				startActivity(mIntent);
				break;
			case R.id.bt :
				performAnimation(bt);
				break;
			case R.id.fm :
				performAnimation(fm);
				break;
			case R.id.photo :
				performAnimation(photo);
				break;
			case R.id.video :
				performAnimation(video);
				break;
			case R.id.date_time :
				mIntent.setClassName("com.android.settings",
						"com.android.settings.Settings$DateTimeSettingsActivity");
				startActivity(mIntent);
				break;
			case R.id.internet :
				performAnimation(internet);
				break;
			case R.id.setting :
				performAnimation(setting);
				break;
			default :
				break;
			}
		}
		
	};
	
	/**
	 * ÔËÐÐ¶¯»­
	 * @param imgView µã»÷µÄÍ¼±ê
	 * */
	private void performAnimation(final View imgView) {
		final Intent mIntent = new Intent();
		imgView.setVisibility(View.GONE);
		Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
		
		leftLayout.setAnimation(leftOutAnimation);
		rightLayout.setAnimation(rightOutAnimation);
		
		leftOutAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				Log.d(LOG_TAG, "onAnimationStart....");
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.d(LOG_TAG, "onAnimationRepeat...");
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
				switch (imgView.getId()) {
				case R.id.bt :
					mIntent.setClassName("com.csr.BTApp",
							"com.csr.BTApp.CSRBluetoothDemoActivity");
					startActivity(mIntent);
					break;
				case R.id.fm :
					mIntent.setClassName("com.uni.lei.fm",
							"com.uni.lei.fm.FMActivity");
					startActivity(mIntent);
					break;
				case R.id.photo :
					mIntent.setClassName("com.android.gallery3d",
							"com.android.gallery3d.app.Gallery");
					startActivity(mIntent);
					break;
				case R.id.video :
					mIntent.setClassName("com.android.gallery3d",
							"com.android.gallery3d.app.Gallery");
					startActivity(mIntent);
					break;
				case R.id.musicview :
					mIntent.setClassName("com.android.music",
							"com.android.music.MediaPlaybackActivity");
					startActivity(mIntent);
					break;
				case R.id.internet :
					mIntent.setClassName("com.android.browser",
							"com.android.browser.BrowserActivity");
					startActivity(mIntent);
					break;
				case R.id.setting :
					mIntent.setClassName("com.android.settings",
							"com.android.settings.Settings");
					startActivity(mIntent);
					break;
				default :
					break;
				}
				Log.d(LOG_TAG, "onAnimationEnd");
			}
		});
		
	}
	
	private OnClickListener musicListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.previousBtn :
				if (Utils.isSdExist()) {
					mMusicHelper.prevMusic();
					musicInfo.setText(mMusicHelper.getMusicName());
				} else {
					showToast(R.string.no_sdcard);
				}
				break;
			case R.id.playBtn :
				if (Utils.isSdExist()) {
					if (mMusicHelper.isPlaying()) {
						playBtn.setImageDrawable(getResources().getDrawable(R.drawable.play));
						mMusicHelper.pause();
					} else {
						playBtn.setImageDrawable(getResources().getDrawable(R.drawable.stop));
						mMusicHelper.playMusic();
					}
				} else {
					showToast(R.string.no_sdcard);
				}
				break;
			case R.id.nextBtn :
				if (Utils.isSdExist()) {
					mMusicHelper.nextMusic();
					musicInfo.setText(mMusicHelper.getMusicName());
				} else {
					showToast(R.string.no_sdcard);
				}
				
				break;
			default :
				break;
			}
			
		}
	};
	
	private BroadcastReceiver date_time_changed_receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(LOG_TAG, "action=" + action);
			if (action.equals(Intent.ACTION_TIME_CHANGED)
					|| action.equals(Intent.ACTION_TIME_TICK)
					|| action.equals(Intent.ACTION_TIMEZONE_CHANGED)
					|| action.equals(Intent.ACTION_DATE_CHANGED)) {
				date_time.postInvalidate();
			}
			
			if (action.equals("com.unistrong.uniteqlauncher.AUTO_PLAY_NEXT_MUSIC")) {
				musicInfo.setText(mMusicHelper.getMusicName());
			}
		}
	};
	
	public void showToast(int prompt){
		Toast.makeText(MainActivity.this, prompt, Toast.LENGTH_LONG).show();
	}
}
