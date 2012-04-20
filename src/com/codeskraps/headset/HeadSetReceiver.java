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

package com.codeskraps.headset;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

public class HeadSetReceiver extends BroadcastReceiver {

	private static final String TAG = "HeadSet";
	private static final String SHAREDPREFS = "sharedprefs";
	private static final String WAKEUP = "WAKEUP";
	private static final String CHKCONAUTOROTATE = "chkConAutoRotate";
	private static final String SPNCONAUTOONOFF = "spnConAutoOnOff";
	private static final String CHKCONRINGVIB = "chkconringvib";
	private static final String SPNCONRINGVIBONOFF = "spnconringvibonoff";
	private static final String CHKCONAPP = "chkConApp";
	private static final String CHKCONMEDIAVOL = "chkconmediavol";
	private static final String SKBCONMEDIAVOL = "skbconmediavol";
	private static final String CHKDISAUTOROTATE = "chkDisAutoRotate";
	private static final String SPNDISAUTOONOFF = "spnDisAutoOnOff";
	private static final String CHKDISRINGVIB = "chkdisringvib";
	private static final String SPNDISRINGVIBONOFF = "spndisringvibonoff";
	private static final String STARTAPP = "startapp";
	private static final String STARTAPPPACKAGE = "startapppackage";
	private static final String STARTAPPACTIVITY = "startactivity";
	private static final String ISCONNECTED = "isconnected";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		SharedPreferences prefs = context.getSharedPreferences(SHAREDPREFS,
				Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();
		String lab = prefs.getString(STARTAPP, STARTAPP);
		String pac = prefs.getString(STARTAPPPACKAGE, STARTAPPPACKAGE);
		String act = prefs.getString(STARTAPPACTIVITY, STARTAPPACTIVITY);
		boolean isConnected = prefs.getBoolean(ISCONNECTED, false); 
		
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock kl = km.newKeyguardLock(act);

		if (intent.getExtras().getInt("state") == 0) {
			Log.d(TAG, "HeadSet disconnected");
			
			editor.putBoolean(ISCONNECTED, false);
			editor.commit();

			if (prefs.getBoolean(CHKDISAUTOROTATE, false)) 
				if (prefs.getBoolean(SPNDISAUTOONOFF, true)) 
					android.provider.Settings.System.putInt(context.getContentResolver(),
									android.provider.Settings.System.ACCELEROMETER_ROTATION, 1);
				else 
					android.provider.Settings.System.putInt(context.getContentResolver(),
							android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);
			
			if (prefs.getBoolean(WAKEUP, false)){
				Log.d(TAG, "reenableKeyguard, release");
				kl.reenableKeyguard();
				//wl.release();
			}
			
			if (prefs.getBoolean(CHKDISRINGVIB, false)) {
				if (prefs.getBoolean(SPNDISRINGVIBONOFF, true)) {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
					//System.putInt(context.getContentResolver(), VIBRATE_IN_SILENT, 0);
				} else {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
				}
			}
			
		} else if (intent.getExtras().getInt("state") == 1 && isConnected == false) {
			
			Log.d(TAG, "HeadSet connected");

			editor.putBoolean(ISCONNECTED, true);
			editor.commit();
			
			if (prefs.getBoolean(CHKCONAUTOROTATE, false))
				if (prefs.getBoolean(SPNCONAUTOONOFF, true))
					android.provider.Settings.System.putInt(context.getContentResolver(),
									android.provider.Settings.System.ACCELEROMETER_ROTATION, 1);
				else
					android.provider.Settings.System.putInt(context.getContentResolver(),
							android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);

			if (prefs.getBoolean(CHKCONRINGVIB, false)) {
				if (prefs.getBoolean(SPNCONRINGVIBONOFF, true)) {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
					//System.putInt(context.getContentResolver(), VIBRATE_IN_SILENT, 0);
				} else {
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//					audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
				}
			}
			
			if (prefs.getBoolean(CHKCONMEDIAVOL, false)) {

				int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 
						(maxVol * prefs.getInt(SKBCONMEDIAVOL, 0)) / 100,
						AudioManager.FLAG_VIBRATE);
				
				Log.d(TAG, "Setting vol: " + prefs.getInt(SKBCONMEDIAVOL, 7));
			}
				
			if (prefs.getBoolean(CHKCONAPP, false)) {
				try {
					
					if (!lab.equals(STARTAPP)) {
						
						if (prefs.getBoolean(WAKEUP, false)) {
							
							kl.disableKeyguard();
							
							Intent i = new Intent(new Intent(context, WakeUpActivity.class));
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(i);
							
						} else {

							Intent i = new Intent(Intent.ACTION_MAIN, null);
							i.addCategory(Intent.CATEGORY_LAUNCHER);
							i.setComponent(new ComponentName(pac, act));
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(i);
						
						}
					}
				} catch (Exception e) {
					Toast.makeText(context, "HeadSet - Couldn't launch the app", Toast.LENGTH_SHORT).show();
					Log.d(TAG, "Error: " + e);
				}
			}			
		}
	}
}