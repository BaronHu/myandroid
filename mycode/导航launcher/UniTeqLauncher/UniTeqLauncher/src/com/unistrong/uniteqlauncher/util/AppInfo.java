package com.unistrong.uniteqlauncher.util;

import android.graphics.drawable.Drawable;

/**
 * Application info cache
 * @author Baron.Hu
 * */
public class AppInfo {

	public static String pkgName;
	public static String clsName;
	public static Drawable appIcon;
	public static String appName;
	
	public String toString() {
		return "AppInfo: [pkgName=" + pkgName + ", clsName=" + clsName + ", appName=" + appName + "]";
	}

}
