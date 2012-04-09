package com.codeskraps.headset;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ServiceListener extends Service {

	private static final String TAG = "HeadSet";
	private HeadSetReceiver apr = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "ServiceListener onCreate");
		/* register receiver */
		apr = new HeadSetReceiver(); /* replace this by your receiver class */
		IntentFilter inf = new IntentFilter();
		inf.addAction("android.intent.action.HEADSET_PLUG");
		registerReceiver(apr, inf);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.d(TAG, "ServiceListener onDestroy");
		unregisterReceiver(apr);
	}
}
