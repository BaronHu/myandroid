package com.unistrong.uniteqlauncher.adapter;

import java.util.List;

import com.unistrong.uniteqlauncher.R;
import com.unistrong.uniteqlauncher.util.AppsInfo;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  Adapter for GridView, for displaying all applications
 *  @author Baron.hu
 *  */
public class GridViewAdapter extends BaseAdapter {
	
	public static final String LOG_TAG = "GridViewAdapter";
	
	private List<ResolveInfo> apps; // 加载所有APP
	private Context mContext;       // 上下文
	private LayoutInflater mInflater; // 加载布局

	/* APP图标和名称  */
	class InfoHolder {
		ImageView imageView;
		TextView textView;
	}

	public GridViewAdapter(Context context, List<ResolveInfo> apps) {
		super();
		this.mContext = context;
		this.apps = apps;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return apps.size();
	}

	public Object getItem(int position) {
		return apps.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		InfoHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nine_path, null);
			holder = new InfoHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageViewIcon);
			holder.textView = (TextView) convertView
					.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (InfoHolder) convertView.getTag();
		}

		String appName = apps.get(position).activityInfo.loadLabel(
				mContext.getPackageManager()).toString();
		Drawable appIcon = apps.get(position).activityInfo
				.loadIcon(mContext.getPackageManager());
		AppsInfo mAppsInfo = new AppsInfo(appName, appIcon);
		if (mAppsInfo != null) {
			holder.imageView.setImageDrawable(mAppsInfo.getAppIcon());
			holder.textView.setText(mAppsInfo.getAppLable());
		}

		return convertView;
	}

}
