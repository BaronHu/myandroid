package com.unistrong.uniteqlauncher.util;

import android.graphics.drawable.Drawable;

/** APP图标和名称的bean对象 
 * @author Baron.hu
 * */
public class AppsInfo {
	String appLable;
	Drawable appIcon;

	public AppsInfo() {

	}

	public AppsInfo(String appLable, Drawable appIcon) {
		this.appLable = appLable;
		this.appIcon = appIcon;
	}

	public void setAppLable(String appLable) {
		this.appLable = appLable;
	}

	public String getAppLable() {
		return this.appLable;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public Drawable getAppIcon() {
		return this.appIcon;
	}
	
	public String toString() {
		return "AppsInfo : [appLable=" + appLable + "]";
	}

}
