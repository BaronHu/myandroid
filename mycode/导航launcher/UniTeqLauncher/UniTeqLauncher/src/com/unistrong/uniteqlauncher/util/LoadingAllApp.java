package com.unistrong.uniteqlauncher.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

/**
 * 查询所有已安装的APP
 * @author Baron.hu
 * */
public class LoadingAllApp {

	private static List<ResolveInfo> apps;
	
	/**
	 * query all applications which category is CATEGORY_LAUNCHER
	 * @return void
	 * @param
	 * */
	public static List<ResolveInfo> loadApps(Context mContext) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		apps = mContext.getPackageManager().queryIntentActivities(
				intent, 0);
		if (apps != null)
			return apps;
		
		return null;
	}
}
