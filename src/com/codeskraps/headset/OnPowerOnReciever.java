package com.codeskraps.headset;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class OnPowerOnReciever extends BroadcastReceiver{

	private static final String SHAREDPREFS = "sharedprefs";
	private static final String CHKAUTOSTART = "chkautostart";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
		
		if (prefs.getBoolean(CHKAUTOSTART, false))
			context.startService(new Intent(context, ServiceListener.class));
	}

}
