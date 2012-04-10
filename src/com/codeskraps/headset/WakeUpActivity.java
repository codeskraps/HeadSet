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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class WakeUpActivity extends Activity {

	private static final String TAG = "HeadSet";
	private static final String SHAREDPREFS = "sharedprefs";
	private static final String STARTAPPPACKAGE = "startapppackage";
	private static final String STARTAPPACTIVITY = "startactivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "WakeUp onCreate");
		
		SharedPreferences prefs = getSharedPreferences(SHAREDPREFS,
				Context.MODE_PRIVATE);
		
		String pac = prefs.getString(STARTAPPPACKAGE, STARTAPPPACKAGE);
		String act = prefs.getString(STARTAPPACTIVITY, STARTAPPACTIVITY);
		
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		              | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); 
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		              | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		
		try {
			Intent i = new Intent(Intent.ACTION_MAIN, null);
			i.addCategory(Intent.CATEGORY_LAUNCHER);
			i.setComponent(new ComponentName(pac, act));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} catch (Exception e) {
			Log.d(TAG, "Error WakeUp: " + e);
			Toast.makeText(this, "HeadSet - Couldn't launch the app", Toast.LENGTH_SHORT).show();
		}
		
		finish();
	}
}
