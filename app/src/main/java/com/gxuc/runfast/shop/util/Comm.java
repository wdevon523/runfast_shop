package com.gxuc.runfast.shop.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;


import java.io.File;


public class Comm {
	public static final String TAG = "shop";
   public static final boolean DEBUG=true;
	public static final String SDCardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static final String doFolder = buildString(SDCardPath,
			File.separator, TAG, File.separator);
	public static final String doCacheFolder = buildString(doFolder, ".cache",
			File.separator);
	public static final String doImageCacheFolder = buildString(doFolder, "ImageCache",
			File.separator);



//	public static final int SDK = Build.VERSION.SDK_INT;// 系统版本号
	public static final String MODEL = Build.MODEL;// 手机型号

	/**
	 * SDCard是否已经挂载
	 * 
	 * @return
	 */
	public static boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * 保存App版本号
	 * 
	 * @param context
	 * @param version
	 */
	public static void saveFlag(Context context, int version) {
		Editor prefs = context.getSharedPreferences(TAG, 0).edit();
		prefs.putInt("version", version);
		prefs.commit();
	}

	public static int loadFlag(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(TAG, 0);
		return prefs.getInt("version", 0);
	}

	/**
	 * 创建字符串方法，当需要组装2个以上的字符串时请使用这个方法
	 * 
	 * @param element
	 * @return
	 */
	public static String buildString(Object... element) {
		StringBuffer sb = new StringBuffer();
		for (Object str : element) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 文字提醒
	 * 
	 * @param context
	 * @param id
	 */
	public static final void alert(Context context, int id) {
		Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
	}

	public static final void alertL(Context context, int id) {
		Toast.makeText(context, id, Toast.LENGTH_LONG).show();
	}

	public static final void alert(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static final void alertL(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

}
