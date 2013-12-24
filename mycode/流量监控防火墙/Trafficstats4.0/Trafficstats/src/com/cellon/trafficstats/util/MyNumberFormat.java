package com.cellon.trafficstats.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Baron.Hu
 * Handling the format of number
 * */
public class MyNumberFormat {
	
	/*
	 * Byte --> M
	 * do not round the date, this will lead a big gap between the real traffic
	 * */
	public static String pad(String s) {
    	StringBuilder sb = new StringBuilder();
    	if (s != null) {
    		double d = Double.parseDouble(s) / (1 << 20);
    		if (d < 1) {
    			sb.append(" ").append(s).append(" B");
    		} else {
    			String ss = String.valueOf(d);
    			if (ss.indexOf(".") != -1) {
    				String str = ss.substring(ss.indexOf("."), ss.length());
    				if (str.length() > 3) {
    					String x = String.valueOf(d).substring(0, String.valueOf(d).indexOf(".") + 3);
    					sb.append(x).append(" M");
    				} else {
    					sb.append(ss).append(" M");
    				}
    			}
    		}
    	}
    	return sb.toString();
    }
	
	/* 
	 * byte --> M
	 * save 3 effective number after dot, and get rid of it by Math.round();
	 * 
	 *  */
	public static String round(String s) {
		if (s != null) {
			double d = Double.parseDouble(s);
			double m = d / (1 << 20);
			m *= 1000;
			m = Math.round(m);
			m /= 1000;
			return m + " M";
		}
		
		return s;
	}
	
	/**
	 * B->K
	 * this will be used in the floating view
	 * 
	 * */
	public static String b2k(String s) {
		if (s != null) {
			double d = Double.parseDouble(s);
			double k = d / (1 << 10);
			k *= 10;
			k = Math.round(k);
			k /= 10;
			return k + "K/s";
		}
		return s;
	}
	
	public static String left(String s) {
		if (s != null && s.length() > 0) {
			double d = Double.parseDouble(s);
			d *= 1000;
			d = Math.round(d);
			d /= 1000;
			return d + " M";
		}
		return s;
	}
	
	public static String left2(String s) {
		if (s != null) {
			double d = Double.parseDouble(s);
			double m = d / (1 << 20);
			m *= 1000;
			m = Math.round(m);
			m /= 1000;
			return m + "";
		}
		
		return s;
	}
	
	public static boolean isNum(String str) {
    	String regex = "[0-9\\.]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			return true;
		}
		return false;
    }
    
    public static String cutNum(String str) {
		StringBuilder sb = new StringBuilder();
		String regex = "[0-9\\.]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			sb.append(m.group(0));
		}
		return sb.toString();
    }

}
