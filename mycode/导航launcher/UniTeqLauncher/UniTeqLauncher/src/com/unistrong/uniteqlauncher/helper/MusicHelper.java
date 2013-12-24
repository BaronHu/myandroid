package com.unistrong.uniteqlauncher.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.unistrong.uniteqlauncher.util.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

/**
 * @author Baron.hu
 * ���ֲ��ſ�����
 * */
public class MusicHelper {
	
	public static final String LOG_TAG = "MusicHelper";
	private Context mContext;
	
	/* ���������� */
	public MediaPlayer mMediaPlayer;
	
	/* �����б� */
	public List<String> mMusicList;
	
	/* ��ǰ���ŵĸ������� */
	public int currentPlayingItem = 0;
	
	/* ��ǰ���ŵ�λ�� */
	public long currentPosition = 0;
	
	/* �Ƿ��Զ�������һ�� */
	public boolean isPlayingNext = false;
	
	/* �Ƿ���ͣ�� */
	public boolean isPaused = false;
	
	public MusicHelper(Context context) {
		this.mContext = context;
		mMediaPlayer = new MediaPlayer();
		mMusicList = new ArrayList<String>();
		getAllMusicFiles(Constants.MEDIA_PATH);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		mContext.registerReceiver(sdMountedReceiver, filter);
	}
	
	/**
	 * ��ȡSD���е�����mp3����
	 * @param pathname the directory path
	 * @return List the array of all files
	 * */
	public List<String> getAllMusicFiles(String pathname) {
		if (pathname == null || pathname.length() < 1) {
			Log.d(LOG_TAG, "MusicHelper.getAllMusicFiles, inavailable path when retrive a path");
			return null;
		}
		File mFile = new File(pathname);
		if (mFile.exists()) {
			if (mFile.isDirectory()) {
				File[] files = mFile.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isDirectory()) {
							getAllMusicFiles(file.getPath()); // �ݹ�����
						} else {
							if (file.getAbsolutePath().endsWith(".mp3")) {
								mMusicList.add(file.getAbsolutePath());
							}
						}
					}
				} else {
					Log.d(LOG_TAG, "there are no any files under the path.");
				}
			} else if (mFile.getAbsolutePath().endsWith(".mp3")) {
				mMusicList.add(mFile.getAbsolutePath());
			} else {
				Log.d(LOG_TAG, "not a directory." );
			}
		} else {
			Log.d(LOG_TAG, "file not exists...");
		}
		return mMusicList;
	}
	
	/* ��ʼ�������� */
	public boolean playMusic() {
		Log.d(LOG_TAG, "playMusic()-->currentPlayingItem=" + currentPlayingItem);
		if (mMusicList == null || mMusicList.size() < 1) return false;
		try {
			if (!isPaused) {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(mMusicList.get(currentPlayingItem));
				mMediaPlayer.prepare();
			}
			mMediaPlayer.start();
			
			//�Զ�������һ��
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					isPlayingNext = true;
					isPaused = false;
					nextMusic();
					Intent intent = new Intent("com.unistrong.uniteqlauncher.AUTO_PLAY_NEXT_MUSIC");
					intent.putExtra("currentMusicItem", currentPlayingItem);
					mContext.sendBroadcast(intent); // ֪ͨ������UI���¸���
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/* ��һ�� */
	public void prevMusic() {
		Log.d(LOG_TAG, "prevMusic()-->currentPlayingItem=" + currentPlayingItem);
		isPaused = false;
		if (mMusicList == null || mMusicList.size() < 1) return;
		//�����ǰ����Ϊ��һ��,����һ��Ϊ���һ��
		if (currentPlayingItem-- <= 0) {
			currentPlayingItem = mMusicList.size() - 1;
		}
		//����ǲ���״̬,���Զ�������һ��
		if (mMediaPlayer.isPlaying()) {
			playMusic();
		} else if (isPlayingNext) {
			playMusic();
		} 
	}
	
	public void nextMusic() {
		Log.d(LOG_TAG, "nextMusic()-->currentPlayingItem=" + currentPlayingItem);
		isPaused = false;
		if (mMusicList == null || mMusicList.size() < 1) return;
		if (currentPlayingItem++ >= mMusicList.size() - 1) {
			currentPlayingItem = 0;
		}
		if (mMediaPlayer.isPlaying()) {
			playMusic();
		} else if (isPlayingNext) {
			playMusic();
			isPlayingNext = false;
		} 
	}
	
	public boolean pause() {
		isPaused = true;
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
		}else{
			mMediaPlayer.start();
		}
		return false;
	}
	
	public void stop() {
		mMediaPlayer.reset();
	}
	
	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}
		
		try {
			mContext.unregisterReceiver(sdMountedReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public boolean isPlaying() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.isPlaying();
		}
		return false;
	}
	
	/* ��ȡ��ǰ���ŵ�λ��,�Ѳ��ŵĳ��� */
	public long getCurrentPosition() {
		if (mMediaPlayer != null) {
			Log.d(LOG_TAG, "currentPosition=" + mMediaPlayer.getCurrentPosition());
			currentPosition =  mMediaPlayer.getCurrentPosition();
			return currentPosition;
		}
		return -1;
	}
	
	/* ��λ������λ��,���ڿ������� */
	public void seekTo() {
		long position = getCurrentPosition();
		mMediaPlayer.seekTo((int) position);
	}
	
	public String getMusicName() {
		if (mMusicList == null || mMusicList.size() < 1) return "";
		String name = "";
		if (mMusicList != null) {
			name = mMusicList.get(currentPlayingItem);
			name = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
		}
		return name;
	}
	
	private BroadcastReceiver sdMountedReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(LOG_TAG, "action=" + action);
			if (action.equals(Intent.ACTION_MEDIA_BAD_REMOVAL)) {
				if (mMediaPlayer != null) {
					Log.d(LOG_TAG, "sd card has removed...but MediaPlayer is not null");
				} else {
					Log.d(LOG_TAG, "SD CARD HAS REMOVED...AND MEDIAPLAYER IS NULL.");
				}
			}
			
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				if (mMediaPlayer != null) {
					Log.d(LOG_TAG, "sd card is ready...and mediaplayer is not null");
				} else {
					Log.d(LOG_TAG, "sd card is ready...but mediaplayer is null. ");
				}
			}
			
		}
	};

}
