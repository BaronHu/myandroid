package com.unistrong.uniteqlauncher.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unistrong.uniteqlauncher.R;
import com.unistrong.uniteqlauncher.helper.MusicHelper;
import com.unistrong.uniteqlauncher.secondview.MyView2;
import com.unistrong.uniteqlauncher.util.Utils;

public class MainView {

	public static final String LOG_TAG = "MainView";

	private MusicHelper mMusicHelper;

	public boolean isLaunchered = false;

	private MyView2 navigation;
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

	private Activity mThis;
	private View mView;

	public MainView(Activity a, View v) {
		mThis = a;
		mView = v;
		Log.d("hubin", "mMusicHelper == null ? " + (mMusicHelper == null));
		if (mMusicHelper == null)
			mMusicHelper = new MusicHelper(mThis);
		setupView();
		registerReceiver();
		isLaunchered = true;
	}

	public void setupView() {
		final View musicview = (View) mView.findViewById(R.id.musicview);
		musicview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				performAnimation(musicview);

			}
		});

		navigation  = (MyView2) mView.findViewById(R.id.navigation);
		bt          = (ImageButton) mView.findViewById(R.id.bt);
		fm          = (ImageButton) mView.findViewById(R.id.fm);
		photo       = (ImageButton) mView.findViewById(R.id.photo);
		video       = (ImageButton) mView.findViewById(R.id.video);
		date_time   = (DateView) mView.findViewById(R.id.date_time);
		internet    = (ImageButton) mView.findViewById(R.id.internet);
		setting     = (ImageButton) mView.findViewById(R.id.setting);
		leftLayout  = (LinearLayout) mView.findViewById(R.id.left_layout);
		rightLayout = (LinearLayout) mView.findViewById(R.id.right_layout);
		musicInfo   = (TextView) mView.findViewById(R.id.music_info);
		preBtn      = (ImageButton) mView.findViewById(R.id.previousBtn);
		playBtn     = (ImageButton) mView.findViewById(R.id.playBtn);
		nextBtn     = (ImageButton) mView.findViewById(R.id.nextBtn);
		
		if (mMusicHelper.isPlaying()) {
			playBtn.setImageDrawable(mThis.getResources().getDrawable(
					R.drawable.stop));
		} else {
			playBtn.setImageDrawable(mThis.getResources().getDrawable(
					R.drawable.play));
		}
		
		musicInfo.setText(mMusicHelper.getMusicName());
		preBtn.setOnClickListener(musicListener);
		playBtn.setOnClickListener(musicListener);
		nextBtn.setOnClickListener(musicListener);

		navigation.setOnClickListener(btnlistener);
		bt.setOnClickListener(btnlistener);
		fm.setOnClickListener(btnlistener);
		photo.setOnClickListener(btnlistener);
		video.setOnClickListener(btnlistener);
		date_time.setOnClickListener(btnlistener);
		internet.setOnClickListener(btnlistener);
		setting.setOnClickListener(btnlistener);
	}

	public void unRegisterReciver() {
		try {
			mThis.unregisterReceiver(date_time_changed_receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "---unRegisterReciver()---");
	}

	public void registerReceiver() {
		if (mMusicHelper == null)
			mMusicHelper = new MusicHelper(mThis);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_DATE_CHANGED);
		filter.addAction("com.unistrong.uniteqlauncher.AUTO_PLAY_NEXT_MUSIC");
		mThis.registerReceiver(date_time_changed_receiver, filter);
		Log.d(LOG_TAG, "---registerReceiver()---");
	}

	public void releaseMusic() {
		mMusicHelper.release();
		Log.d(LOG_TAG, "---releaseMusic()---");
	}

	private Button.OnClickListener btnlistener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mIntent = new Intent();
			switch (v.getId()) {
			case R.id.navigation:
				mIntent.setClassName("com.chartcross.gpstestplus",
						"com.chartcross.gpstestplus.GPSTestPlus");
				mThis.startActivity(mIntent);
				break;
			case R.id.bt:
				performAnimation(bt);
				break;
			case R.id.fm:
				performAnimation(fm);
				break;
			case R.id.photo:
				performAnimation(photo);
				break;
			case R.id.video:
				performAnimation(video);
				break;
			case R.id.date_time:
				mIntent.setClassName("com.android.settings",
						"com.android.settings.Settings$DateTimeSettingsActivity");
				mThis.startActivity(mIntent);
				break;
			case R.id.internet:
				performAnimation(internet);
				break;
			case R.id.setting:
				performAnimation(setting);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 运行动画
	 * 
	 * @param imgView
	 *            点击的图标
	 * */
	private void performAnimation(final View imgView) {
		final Intent mIntent = new Intent();
		imgView.setVisibility(View.GONE);
		Animation leftOutAnimation = AnimationUtils.loadAnimation(mThis,
				R.anim.translate_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(mThis,
				R.anim.translate_right);

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
				case R.id.bt:
					mIntent.setClassName("com.csr.BTApp",
							"com.csr.BTApp.CSRBluetoothDemoActivity");
					mThis.startActivity(mIntent);
					break;
				case R.id.fm:
					mIntent.setClassName("com.uni.lei.fm",
							"com.uni.lei.fm.FMActivity");
					mThis.startActivity(mIntent);
					break;
				case R.id.photo:
					mIntent.setClassName("com.android.gallery3d",
							"com.android.gallery3d.app.Gallery");
					mThis.startActivity(mIntent);
					break;
				case R.id.video:
					mIntent.setClassName("com.android.gallery3d",
							"com.android.gallery3d.app.Gallery");
					mThis.startActivity(mIntent);
					break;
				case R.id.musicview:
					mIntent.setClassName("com.android.music",
							"com.android.music.MediaPlaybackActivity");
					mThis.startActivity(mIntent);
					break;
				case R.id.internet:
					mIntent.setClassName("com.android.browser",
							"com.android.browser.BrowserActivity");
					mThis.startActivity(mIntent);
					break;
				case R.id.setting:
					mIntent.setClassName("com.android.settings",
							"com.android.settings.Settings");
					mThis.startActivity(mIntent);
					break;
				default:
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
			case R.id.previousBtn:
				if (Utils.isSdExist()) {
					mMusicHelper.prevMusic();
					musicInfo.setText(mMusicHelper.getMusicName());
				} else {
					showToast(R.string.no_sdcard);
				}
				break;
			case R.id.playBtn:
				if (Utils.isSdExist()) {
					if (mMusicHelper.isPlaying()) {
						playBtn.setImageDrawable(mThis.getResources()
								.getDrawable(R.drawable.play));
						mMusicHelper.pause();
					} else {
						playBtn.setImageDrawable(mThis.getResources()
								.getDrawable(R.drawable.stop));
						mMusicHelper.playMusic();
					}
				} else {
					showToast(R.string.no_sdcard);
				}
				break;
			case R.id.nextBtn:
				if (Utils.isSdExist()) {
					mMusicHelper.nextMusic();
					musicInfo.setText(mMusicHelper.getMusicName());
				} else {
					showToast(R.string.no_sdcard);
				}

				break;
			default:
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
				date_time.invalidate();
			}

			if (action
					.equals("com.unistrong.uniteqlauncher.AUTO_PLAY_NEXT_MUSIC")) {
				musicInfo.setText(mMusicHelper.getMusicName());
			}
		}
	};

	public void showToast(int prompt) {
		Toast.makeText(mThis, prompt, Toast.LENGTH_LONG).show();
	}

}
