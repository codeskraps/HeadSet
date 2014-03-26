package com.codeskraps.headset.misc;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.Gson;

public class HeadSetApplication extends Application {
	private static final String TAG = HeadSetApplication.class.getSimpleName();

	private static HeadSetApplication _instance = null;
	private PowerManager.WakeLock wl = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		_instance = this;
		super.onCreate();

		final SharedPreferences prefs = getSharedPreferences(Cons.SHAREDPREFS, MODE_PRIVATE);
		String startpackage = prefs.getString(Cons.STARTAPPPACKAGE, new String());
		if (startpackage != null && startpackage.length() > 0) {
			SharedPreferences.Editor editor = prefs.edit();

			ArrayList<AppWrapper> apps = new ArrayList<AppWrapper>();

			String startapp = prefs.getString(Cons.STARTAPP, new String());
			String startactivity = prefs.getString(Cons.STARTAPPACTIVITY, new String());

			AppWrapper launch = new AppWrapper(startapp, startpackage, startactivity);
			apps.add(launch);

			String json = new Gson().toJson(apps);
			Log.v(TAG, "json app:" + json);

			editor.putString(Cons.STARTAPP, new String());
			editor.putString(Cons.STARTAPPPACKAGE, new String());
			editor.putString(Cons.STARTAPPACTIVITY, new String());
			editor.putString(Cons.STARTUPAPPS, json);

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				editor.commit();
			} else editor.apply();
		}
	}

	public static HeadSetApplication getInstance() {
		return _instance;
	}

	public void WakeLock_aquire() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		@SuppressWarnings("deprecation")
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
		wl.acquire();
	}

	public void WakeLock_release() {
		if (wl != null && wl.isHeld()) wl.release();
	}
}
