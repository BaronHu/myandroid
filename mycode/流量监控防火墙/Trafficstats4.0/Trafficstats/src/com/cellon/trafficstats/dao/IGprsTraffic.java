package com.cellon.trafficstats.dao;

import java.util.List;

/**
 * @author Baron.Hu
 * 
 * */
public interface IGprsTraffic {

	/**
	 * get GPRS/3G traffic of this month
	 * @param firstDayOfMonth first day of this month
	 * @param now current date
	 * */
	String getThisMonthTrafficOfGprs();
	
	/**
	 * the GPRS/3G traffic of today
	 * */
	String getTodayTrafficOfGprs();
	
	/**
	 * last month 3g traffic, we do not need to do that
	 * */
	String getLastMonthTrafficOfGprs();
	
	/**
	 * get GPRS/3G traffic of yesterday
	 * */
	String getYesterdayGprsTraffic();
	
	/**
	 * get gprs1 traffic
	 * */
	String getGprs1Traffic(String powerOnDate, String today);
	
	/**
	 * get the traffic that the date is between power on date and yesterdayGPRS/3G
	 * @param powerOnDate Power on date
	 * @param yesterday 
	 * */
	String getDurationGprsTraffic(String powerOnDate, String maxDate);
	
	/**
	 * get this history 2g/3g traffic
	 * @param today current
	 * */
	String getBeforeGprsTraffic(String today);
	
	/**
	 * 
	 * */
	String getHistoryTraffic(String powerOnDate, String today);
	
	/**
	 * get the max date in the database
	 * */
	String getMaxDate();
	
	/**
	 * get UID total traffic
	 * */
	String getUidTraffic(String uid);
	
	/**
	 * get every day traffic
	 * */
	List<String> getEveryDayTraffic();
}
