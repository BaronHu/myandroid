package com.unistrong.uniteqlauncher.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

/**
 * this class is not used, but maybe use someday
 * @author Baron.hu
 * */
public class FileHelper {
	
	//path + file name
	public static void saveAppIcon(String imageDir, String saveDir) {
		if (imageDir == null || saveDir == null) {
			return;
		}
		
		File saveDirFile = new File(saveDir);
		File[] files = saveDirFile.listFiles();
		if (files != null) {
			for (File file : files) {
				file.delete();
			}
		}
		
		try {
			FileInputStream fis = new FileInputStream(new File(imageDir));
			FileOutputStream fos = new FileOutputStream(new File(saveDir));
			byte[] b = new byte[1];
			while(fis.read(b) != -1) {
				fos.write(b);
				fos.flush();
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean saveBitmap2file(Bitmap bmp,String filename) { 
		CompressFormat format= Bitmap.CompressFormat.PNG; 
		int quality = 100; 
		OutputStream stream = null; 
		try { 
			stream = new FileOutputStream("/mnt/sdcard/" + filename);
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
		}
		return bmp.compress(format, quality, stream); 
		
	}
  

}
