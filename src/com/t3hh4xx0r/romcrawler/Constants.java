package com.t3hh4xx0r.romcrawler;

import java.io.File;

import android.os.Environment;

public class Constants {

	public static boolean AUTOMATICALLY_UPDATE = false;

	public static int REFRESH_TIME = 0;

	public static File extSD = Environment.getExternalStorageDirectory();

	public static String DEVICE = null;
	public static String FORUM = null;
	public static String THREADURL = null;
	public static String THREADTITLE = null;
	public static String REQUEST = android.os.Build.DEVICE.toUpperCase() + "\n\n";
	public static boolean sel = false;
	public static boolean deviceIsSet = false;
}
