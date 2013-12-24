package com.unistrong.uniteqlauncher;

import java.util.ArrayList;

import com.unistrong.uniteqlauncher.adapter.AdapterPager;
import com.unistrong.uniteqlauncher.secondview.ListenerOnPagerChange;
import com.unistrong.uniteqlauncher.secondview.ViewTwo;
import com.unistrong.uniteqlauncher.ui.MainView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class TestMainActivity extends Activity {
	
	public static ViewPager mViewPager;
	private ArrayList<View> viewsList;
	private View mainView;
	private View secondView;
	private MainView mMainView;
	private ViewTwo mSecondView;
	
	public boolean isPaused = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init();
		
	}
	
	public void init() {
		setContentView(R.layout.test_main);
		// ������XML��Ⱦ��View
		LayoutInflater inflater = this.getLayoutInflater();
		mainView = inflater.inflate(R.layout.test_activity_main, null);
		secondView = inflater.inflate(R.layout.view_launcher_two, null);
		// ��ʼ��ViewPager������ҳ��
		mMainView = new MainView(this, mainView);
		mSecondView = new ViewTwo(this, secondView);

		// ������View����List�У���Ϊ����Դ����������
		viewsList = new ArrayList<View>();
		viewsList.add(mainView);
		viewsList.add(secondView);

		// ��ViewPager��װ������
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		mViewPager.setAdapter(new AdapterPager(viewsList));
		mViewPager.setOnPageChangeListener(new ListenerOnPagerChange());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMainView != null) mMainView.releaseMusic();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
		mMainView.unRegisterReciver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isPaused) {
			isPaused = false;
			this.init();
		}
		if (mMainView != null) mMainView.registerReceiver();
	}

}
