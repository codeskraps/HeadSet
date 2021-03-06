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

package com.codeskraps.headset.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.codeskraps.headset.misc.L;
import com.codeskraps.headset.receivers.HeadSetReceiver;

public class ServiceListener extends Service {
	private static final String TAG = ServiceListener.class.getSimpleName();

	private HeadSetReceiver apr = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		L.d(TAG, "ServiceListener onCreate");
		/* register receiver */
		apr = new HeadSetReceiver(); /* replace this by your receiver class */
		IntentFilter inf = new IntentFilter();
		inf.addAction("android.intent.action.HEADSET_PLUG");
		registerReceiver(apr, inf);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		L.d(TAG, "ServiceListener onDestroy");
		unregisterReceiver(apr);
	}
}
