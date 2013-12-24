package com.cellon.trafficstats.dao;

import java.util.List;

/**
 * @author Baron.Hu
 * Actually, we do not intent to do any thing about WIFI
 * */
public interface IWifiTraffic {

	/**
	 * get the wifi traffic of this month
	 * @param firstDayOfMonth first day of this month
	 * @param now current date
	 * @return wifi traffic
	 * */
	String getThisMonthTrafficOfWifi();
	
	/**
	 * get the wifi traffic of today
	 * @return 
	 * */
	String getTodayTrafficOfWifi();
	
	/**
	 * get last month's wifi traffic. Things of idiocy
	 * */
	String getLastMonthTrafficOfWifi();
	
	/**
	 * get yesterday's wifi traffic
	 * */
	String getYesterdayWifiTraffic();
	
	/**
	 * get the wifi traffic that the date was between power on date and yesterday
	 * @param powerOnDate 
	 * @param yesterday 
	 * */
	String getDurationWifiTraffic(String powerOnDate, String today);
	
	/**
	 * get the history wifi traffic. this is no use.
	 * @param today current
	 * */
	String getBeforeWifiTraffic(String today);
	
	/**
	 * get uid wifi traffic by
	 * */
	String getWifiTrafficByUid(String uid);
	
	/**
	 * get every day traffic
	 * */
	List<String> getEveryDayTraffic();
}
