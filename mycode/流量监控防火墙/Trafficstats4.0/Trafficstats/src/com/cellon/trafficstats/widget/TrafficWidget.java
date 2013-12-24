package com.cellon.trafficstats.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.cellon.trafficstats.R;
import com.cellon.trafficstats.dao.GprsTrafficImpl;
import com.cellon.trafficstats.dao.IGprsTraffic;
import com.cellon.trafficstats.util.MyNumberFormat;

/**
 * @author Baron.Hu
 * create the Widget
 * */
public class TrafficWidget extends AppWidgetProvider {
	
	private static final String TOGGLE_REQUEST = "com.cellon.trafficstats.intent.action.TOGGLE_REQUEST";
	
	private Timer timer = new Timer();
	private AppWidgetManager appWidgetManager;
	private Context mContext;
	
	private IGprsTraffic mGprsTraffic;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		if (TOGGLE_REQUEST.equals(intent.getAction())) {
//			Intent i = new Intent();
//			i.setClass(context, TrafficActivity.class);
//			context.startActivity(i);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.appWidgetManager = appWidgetManager;
		this.mContext = context;
		timer = new Timer();
		timer.schedule(task, 0, 3000);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 1 :
				final RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.traf_widget);
				mGprsTraffic = new GprsTrafficImpl(mContext);
				String text = mContext.getString(R.string.today) + MyNumberFormat.round(mGprsTraffic.getTodayTrafficOfGprs());
				
				views.setTextViewText(R.id.textview, text);
				final int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, TrafficWidget.class));
				appWidgetManager.updateAppWidget(widgetIds, views);
				break;
			}
		}
	};
	
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
			
		}
	};
	
}
