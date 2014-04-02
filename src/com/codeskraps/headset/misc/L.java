package com.codeskraps.headset.misc;

import android.util.Log;

import com.codeskraps.headset.BuildConfig;

public class L {

	private L() {}

	public static void v(String tag, String msg) {
		if (BuildConfig.DEBUG) Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable e) {
		if (BuildConfig.DEBUG) Log.v(tag, msg, e);
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG) Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable e) {
		if (BuildConfig.DEBUG) Log.d(tag, msg, e);
	}

	public static void i(String tag, String msg) {
		if (BuildConfig.DEBUG) Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable e) {
		if (BuildConfig.DEBUG) Log.i(tag, msg, e);
	}

	public static void w(String tag, String msg) {
		if (BuildConfig.DEBUG) Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable e) {
		if (BuildConfig.DEBUG) Log.w(tag, msg, e);
	}

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG) Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable e) {
		if (BuildConfig.DEBUG) Log.e(tag, msg, e);
	}
}
