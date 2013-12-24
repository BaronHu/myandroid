package com.unistrong.barcodetest;

public class BarcodeNative {
	static {
		System.loadLibrary("barcodejni");
	}
	public static native void initUart(int cmd);//(0)
	public static native void exit(int cmd);//(1)
	public static native String get(int cmd);//(3)
	public static native void scan(int cmd);
}