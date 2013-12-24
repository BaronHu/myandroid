package com.cellon.trafficstats.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Baron.Hu
 * write some information into SD card
 * */
public class Log2File {
	
	public static final String LOG_PATH = "/sdcard/cellon_traffic/";
	
	public static void writeLog2File(String flag, String str) {
		if (checkSDcard()) {
			try {
				FileWriter fw;
				//create the path of log
				File file = new File(LOG_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				//get all files that under this path
				List<String> list = getFileName(LOG_PATH);
				
				StringBuffer sb = new StringBuffer();
				
				//We will use the timestamp as the name of the log
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = new Date();
				String s = df.format(d);
				String name = s.substring(0, 10);
				
				if (list != null && list.size() > 0){
					if (list.get(list.size() - 1).equals(name)) {
						//append the content to name.txt if the file exists
						fw = new FileWriter(new File(LOG_PATH + name + ".txt"), true);
						sb.append(s + "/" + flag +" : " + str);
						fw.write(sb.toString() + "\n");
						fw.close();
						return;
					}
				}
				
				//create a new log in the other day, one log per day.
				File file2 = new File(LOG_PATH + name + ".txt");
				if (!file2.exists()) {
					file2.createNewFile();
				}
				fw = new FileWriter(file2);
				if (str == null || str.length() < 1) {
					return;
				}
				sb.append(s + " : " + str);
				try {
					fw.write(sb.toString() + "\n");
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static List<String> getFileName(String path){
		File file = new File(path);
		File fileNames[] = file.listFiles();
		List<String> listFileName = new ArrayList<String>();
		if (fileNames != null && fileNames.length > 0) {
			for (File files : fileNames) {
				if (!files.isDirectory()) {
					listFileName.add(files.getName().substring(0, files.getName().indexOf(".")));
				}
				
			}
			return listFileName;
		}
		
		return null;
	}
	
	public static boolean checkSDcard(){
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			//no SD card
			return false;
		}
		return true;
	}

}
