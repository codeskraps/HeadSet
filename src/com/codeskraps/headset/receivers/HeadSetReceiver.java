/**
 * HeadSet
 * Copyright (C) Carles Sentis 2011 <codeskraps@gmail.com>
 *
 * HeadSet is free software: you can
 * redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later
 * version.
 *  
 * HeadSet is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *  
 * You should have received a copy of the GNU
 * General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.codeskraps.headset.receivers;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;

import com.codeskraps.headset.activities.GhostActivity;
import com.codeskraps.headset.misc.Cons;
import com.codeskraps.headset.misc.L;

public class HeadSetReceiver extends BroadcastReceiver {
	private static final String TAG = HeadSetReceiver.class.getSimpleName();

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = context.getSharedPreferences(Cons.SHAREDPREFS,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();

		boolean isConnected = prefs.getBoolean(Cons.ISCONNECTED, false);
		boolean isPowerTrigger = prefs.getBoolean(Cons.CHKPOWERCONNECTED, false);

		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		KeyguardManager.KeyguardLock kl = null;
		KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		kl = km.newKeyguardLock(TAG);

		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);

		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
				|| status == BatteryManager.BATTERY_STATUS_FULL;

		if (isPowerTrigger && isCharging == false) return;

		if (intent.getExtras().getInt("state") == 0 && isConnected == true) {
			L.d(TAG, "HeadSet disconnected");

			editor.putBoolean(Cons.ISCONNECTED, false);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				editor.commit();
			} else editor.apply();

			if (prefs.getBoolean(Cons.CHKDISAUTOROTATE, false))
				if (prefs.getBoolean(Cons.SPNDISAUTOONOFF, true)) android.provider.Settings.System
						.putInt(context.getContentResolver(),
								android.provider.Settings.System.ACCELEROMETER_ROTATION, 1);
				else android.provider.Settings.System.putInt(context.getContentResolver(),
						android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);

			if (prefs.getBoolean(Cons.WAKEUP, false)) {
				L.d(TAG, "reenableKeyguard, release");
				kl.reenableKeyguard();
			}

			if (prefs.getBoolean(Cons.CHKDISRINGVIB, false)) {
				if (prefs.getBoolean(Cons.SPNDISRINGVIBONOFF, true)) {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					/*-
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
							AudioManager.VIBRATE_SETTING_ON);
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
							AudioManager.VIBRATE_SETTING_ON);
					System.putInt(context.getContentResolver(), VIBRATE_IN_SILENT, 0);*/
				} else {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					/*-
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
							AudioManager.VIBRATE_SETTING_OFF);
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
							AudioManager.VIBRATE_SETTING_OFF);*/
				}
			}

		} else if (intent.getExtras().getInt("state") == 1 && isConnected == false) {
			L.d(TAG, "HeadSet connected");

			editor.putBoolean(Cons.ISCONNECTED, true);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				editor.commit();
			} else editor.apply();

			if (prefs.getBoolean(Cons.CHKCONAUTOROTATE, false))
				if (prefs.getBoolean(Cons.SPNCONAUTOONOFF, true)) android.provider.Settings.System
						.putInt(context.getContentResolver(),
								android.provider.Settings.System.ACCELEROMETER_ROTATION, 1);
				else android.provider.Settings.System.putInt(context.getContentResolver(),
						android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);

			if (prefs.getBoolean(Cons.CHKCONRINGVIB, false)) {
				if (prefs.getBoolean(Cons.SPNCONRINGVIBONOFF, true)) {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					/*-
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
							AudioManager.VIBRATE_SETTING_ON);
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
							AudioManager.VIBRATE_SETTING_ON);
					System.putInt(context.getContentResolver(), VIBRATE_IN_SILENT, 0);*/
				} else {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					/*-
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
							AudioManager.VIBRATE_SETTING_OFF);
					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
							AudioManager.VIBRATE_SETTING_OFF);*/
				}
			}

			if (prefs.getBoolean(Cons.CHKCONMEDIAVOL, false)) {

				int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						(maxVol * prefs.getInt(Cons.SKBCONMEDIAVOL, 0)) / 100,
						AudioManager.FLAG_VIBRATE);

				L.d(TAG, "Setting vol: " + prefs.getInt(Cons.SKBCONMEDIAVOL, 7));
			}

			if (prefs.getBoolean(Cons.CHKCONAPP, false)) {
				kl.disableKeyguard();

				Intent i = new Intent(new Intent(context, GhostActivity.class));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		}
	}
}