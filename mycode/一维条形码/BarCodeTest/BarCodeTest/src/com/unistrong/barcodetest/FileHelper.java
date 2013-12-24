package com.unistrong.barcodetest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

/**
 * �ļ���д�����İ�����
 * @author HuBin
 * */
public class FileHelper {
	
	public static final String LOG_TAG = "FileHelper";

	
	/**
	 * д�ļ�
	 * @param str Ҫд�������,����Ϊnull
	 * @param context
	 * @return void
	 * */
	public static void write(String str, Context context) {
		if (str == null || str.length() < 0) {
			Log.d(LOG_TAG, "the content is null, you can not write.");
			return;
		}
		try {
			File file = new File(context.getFilesDir() + "/data.txt");
			Log.d("FileHelper", "======create file=========");
			if (!file.exists()) {
				file.createNewFile();
			} 
			FileWriter writer = new FileWriter(file, true);
			if (str != null) {
				writer.write(str + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ļ� 
	 * @param context
	 * @return StringBuilder ���ر��ζ�ȡ�����ݱ��浽StringBuilder����
	 * */
	public static StringBuilder read(Context context) {
		StringBuilder sb = new StringBuilder();
		File file = new File(context.getFilesDir() + "/data.txt");
		if (!file.exists()) return null;
		BufferedReader br = null;
		String str = "";
		try {
			br = new BufferedReader(new FileReader(file));
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb;
	}

}
