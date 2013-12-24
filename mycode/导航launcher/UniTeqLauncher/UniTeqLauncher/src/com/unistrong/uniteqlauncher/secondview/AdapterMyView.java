package com.unistrong.uniteqlauncher.secondview;

import java.util.HashMap;
import java.util.List;

import com.unistrong.uniteqlauncher.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMyView extends BaseAdapter {

	private Context mContext;
	private List<HashMap<String, Object>> mList;
	private ImageView mIcon;
	private TextView mLabel;

	public AdapterMyView(Context c, List<HashMap<String, Object>> l) {
		this.mContext = c;
		mList = l;
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

	@SuppressWarnings("unchecked")
	public void exchange(int startPosition, int endPosition) {
		Object endObject = getItem(endPosition);
		Object startObject = getItem(startPosition);
		mList.add(startPosition, (HashMap<String, Object>) endObject);
		mList.remove(startPosition + 1);
		mList.add(endPosition, (HashMap<String, Object>) startObject);
		mList.remove(endPosition + 1);
		notifyDataSetChanged();
	}
	
	public void addItem(HashMap<String, Object> map){
		mList.add(map);
		notifyDataSetChanged();
	}

	public List<HashMap<String, Object>> getItemsList(){
		return mList;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view_two, null);
		
		mIcon = (ImageView)convertView.findViewById(R.id.mIcon);
		mLabel = (TextView) convertView.findViewById(R.id.mLabel);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) this.getItem(position);
		String label = map.get("label").toString();
		Drawable icon = (Drawable)map.get("icon");		

		if(icon != null){
			
			mIcon.setBackgroundDrawable(icon);
			
		}
		if(label != null){
			
			mLabel.setText(label);
			
		}


		
		
		return convertView;
	}
	


}
