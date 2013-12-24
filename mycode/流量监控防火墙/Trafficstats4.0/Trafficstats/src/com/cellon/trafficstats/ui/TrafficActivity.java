package com.cellon.trafficstats.ui;

import java.util.ArrayList;

import com.cellon.trafficstats.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

/**
 * @author Baron.Hu
 * */
public class TrafficActivity extends FragmentActivity {
	
	TabHost mTabHost;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tabs_pager);
        
        mTabHost = (TabHost) findViewById(R.id.tabhost);
        mTabHost.setup();
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        
        CharSequence trafficTitle = getString(R.string.app_name);
        CharSequence firewallTitle = getString(R.string.app_name1);
        CharSequence settingsTitle = getString(R.string.menu_settings);
        
        mTabsAdapter.addTab(mTabHost.newTabSpec("traffic").setIndicator(trafficTitle),
                MainFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("firewall").setIndicator(firewallTitle),
                FirewallFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("settings").setIndicator(settingsTitle),
                SettingsFragment.class, null);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }
    
    public static class TabsAdapter extends FragmentPagerAdapter 
    	implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
    	
    	private final Context mContext;
    	private final TabHost mTabHost;
    	private final ViewPager mViewPager;
    	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    	
    	static class TabInfo {
    		private final String tag;
    		private final Class<?> clss;
    		private final Bundle args;
    		
    		TabInfo(String _tag, Class<?> _class, Bundle _args) {
    			tag = _tag;
    			clss = _class;
    			args = _args;
    		}
     	} 
    	
    	static class DummyTabFactory implements TabHost.TabContentFactory {
    		
    		private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
			}
    		
    	}
    	
    	public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
    		super(activity.getSupportFragmentManager());
    		mContext = activity;
    		mTabHost = tabHost;
    		mViewPager = pager;
    		mTabHost.setOnTabChangedListener(this);
    		mViewPager.setAdapter(this);
    		mViewPager.setOnPageChangeListener(this);
    	}
    	
    	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
    		tabSpec.setContent(new DummyTabFactory(mContext));
    		String tag = tabSpec.getTag();
    		TabInfo info = new TabInfo(tag ,clss, args);
    		mTabs.add(info);
    		mTabHost.addTab(tabSpec);
    		notifyDataSetChanged();
    	}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
			
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
			
		}
    	
    }
}