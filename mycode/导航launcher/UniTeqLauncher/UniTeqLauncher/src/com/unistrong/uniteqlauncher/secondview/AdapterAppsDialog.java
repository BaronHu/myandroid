package com.unistrong.uniteqlauncher.secondview;

import java.util.List;

import com.unistrong.uniteqlauncher.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class AdapterAppsDialog extends BaseAdapter {

	private List<PackageInfo> mList;
	private Activity mThis;
	private int size = 0;
	
	public AdapterAppsDialog(Activity a, List<PackageInfo> l){
		
		this.mThis = a;
		this.mList = l;
				
	}
	
	public int getCount() {

		return mList.size();
	}

	public Object getItem(int position) {
	
		return mList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}
	
	class InfoHolder {
		ImageView imageView;
		TextView textView;
	}

	public View getView(int position, View view, ViewGroup parent) {
		
		InfoHolder holder;
		if (view == null) {
			holder = new InfoHolder();
			view = LayoutInflater.from(mThis).inflate(R.layout.item_dialog_apps, null);
			holder.imageView = (ImageView) view.findViewById(R.id.mIcon);
			holder.textView = (TextView) view.findViewById(R.id.mLabel);
			view.setTag(holder);
		} else {
			holder = (InfoHolder) view.getTag();
		}
		
		RelativeLayout mBg = (RelativeLayout)view.findViewById(R.id.mBg);
				
//		if(size % 2 == 0){
//			mBg.setBackgroundResource(R.color.darkgreen);
//		}else{
//			mBg.setBackgroundResource(R.color.green);
//		}
			
		size++;
		
		PackageInfo mPI = (PackageInfo)mList.get(position);
		String label = (String) mPI.applicationInfo.loadLabel(mThis.getPackageManager());
		Drawable icon = mPI.applicationInfo.loadIcon(mThis.getPackageManager());
				
		holder.textView.setText(label);
		holder.imageView.setBackgroundDrawable(icon);
		
		return view;
		
	}
	
				
}