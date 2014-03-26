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

package com.codeskraps.headset.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

import com.codeskraps.headset.R;
import com.codeskraps.headset.misc.Cons;
import com.codeskraps.headset.services.ServiceListener;

public class HeadSetActivity extends Activity implements OnItemSelectedListener, OnClickListener,
		OnCheckedChangeListener, OnSeekBarChangeListener {
	private static final String TAG = HeadSetActivity.class.getSimpleName();

	private SharedPreferences prefs = null;
	private CheckBox chkAutoStart = null;
	private CheckBox chkWakeUp = null;
	private CheckBox chkPowerConnected = null;
	private CheckBox chkConAutoRotate = null;
	private Spinner spnConRotateOnOff = null;
	private CheckBox chkConRingVib = null;
	private Spinner spnConRingVibOnOff = null;
	private CheckBox chkConApp = null;
	private Button btnConApp = null;
	private CheckBox chkConMediaVol = null;
	private SeekBar skbConMediaVol = null;
	private CheckBox chkDisAutoRotate = null;
	private Spinner spnDisRotateOnOff = null;
	private CheckBox chkDisRingVib = null;
	private Spinner spnDisRingVibOnOff = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "HeadSetActivity onCreate");

		setContentView(R.layout.main);

		prefs = getSharedPreferences(Cons.SHAREDPREFS, MODE_PRIVATE);

		chkAutoStart = (CheckBox) findViewById(R.id.chkAutoStart);
		chkWakeUp = (CheckBox) findViewById(R.id.chkWakeUp);
		chkPowerConnected = (CheckBox) findViewById(R.id.chkPowerConnected);
		chkConAutoRotate = (CheckBox) findViewById(R.id.chkConAutoRotate);
		spnConRotateOnOff = (Spinner) findViewById(R.id.spnConAutoOnOff);
		chkConRingVib = (CheckBox) findViewById(R.id.chkConRingVib);
		spnConRingVibOnOff = (Spinner) findViewById(R.id.spnConRingVibOnOff);
		chkConApp = (CheckBox) findViewById(R.id.chkConApp);
		btnConApp = (Button) findViewById(R.id.btnConApp);
		chkConMediaVol = (CheckBox) findViewById(R.id.chkConMediaVolume);
		skbConMediaVol = (SeekBar) findViewById(R.id.skbConVolume);
		chkDisAutoRotate = (CheckBox) findViewById(R.id.chkDisAutoRotate);
		spnDisRotateOnOff = (Spinner) findViewById(R.id.spnDisAutoOnOff);
		chkDisRingVib = (CheckBox) findViewById(R.id.chkDisRingVib);
		spnDisRingVibOnOff = (Spinner) findViewById(R.id.spnDisRingVibOnOff);

		chkAutoStart.setOnCheckedChangeListener(this);
		chkWakeUp.setOnCheckedChangeListener(this);
		chkPowerConnected.setOnCheckedChangeListener(this);
		chkConAutoRotate.setOnCheckedChangeListener(this);
		spnConRotateOnOff.setOnItemSelectedListener(this);
		chkConRingVib.setOnCheckedChangeListener(this);
		spnConRingVibOnOff.setOnItemSelectedListener(this);
		chkConApp.setOnCheckedChangeListener(this);
		btnConApp.setOnClickListener(this);
		chkConMediaVol.setOnCheckedChangeListener(this);
		skbConMediaVol.setOnSeekBarChangeListener(this);
		chkDisAutoRotate.setOnCheckedChangeListener(this);
		spnDisRotateOnOff.setOnItemSelectedListener(this);
		chkDisRingVib.setOnCheckedChangeListener(this);
		spnDisRingVibOnOff.setOnItemSelectedListener(this);
		findViewById(R.id.btnStartService).setOnClickListener(this);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.onOff));

		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnConRotateOnOff.setAdapter(arrayAdapter);
		spnConRingVibOnOff.setAdapter(arrayAdapter);
		spnDisRotateOnOff.setAdapter(arrayAdapter);
		spnDisRingVibOnOff.setAdapter(arrayAdapter);

		skbConMediaVol.setProgress(prefs.getInt(Cons.SKBCONMEDIAVOL, 0));
	}

	@Override
	protected void onResume() {
		super.onResume();

		chkAutoStart.setChecked(prefs.getBoolean(Cons.CHKAUTOSTART, false));
		chkWakeUp.setChecked(prefs.getBoolean(Cons.WAKEUP, false));
		chkPowerConnected.setChecked(prefs.getBoolean(Cons.CHKPOWERCONNECTED, false));
		chkConAutoRotate.setChecked(prefs.getBoolean(Cons.CHKCONAUTOROTATE, false));

		if (prefs.getBoolean(Cons.SPNCONAUTOONOFF, true)) {
			spnConRotateOnOff.setSelection(0);
		} else spnConRotateOnOff.setSelection(1);

		chkConRingVib.setChecked(prefs.getBoolean(Cons.CHKCONRINGVIB, false));

		if (prefs.getBoolean(Cons.SPNCONRINGVIBONOFF, true)) {
			spnConRingVibOnOff.setSelection(0);
		} else spnConRingVibOnOff.setSelection(1);

		chkConApp.setChecked(prefs.getBoolean(Cons.CHKCONAPP, false));

		btnConApp.setText("Set apps...");

		boolean mediaVol = prefs.getBoolean(Cons.CHKCONMEDIAVOL, false);
		chkConMediaVol.setChecked(mediaVol);
		skbConMediaVol.setEnabled(mediaVol);

		chkDisAutoRotate.setChecked(prefs.getBoolean(Cons.CHKDISAUTOROTATE, false));

		if (prefs.getBoolean(Cons.SPNDISAUTOONOFF, true)) {
			spnDisRotateOnOff.setSelection(0);
		} else spnDisRotateOnOff.setSelection(1);

		chkDisRingVib.setChecked(prefs.getBoolean(Cons.CHKDISRINGVIB, false));

		if (prefs.getBoolean(Cons.SPNDISRINGVIBONOFF, true)) {
			spnDisRingVibOnOff.setSelection(0);
		} else spnDisRingVibOnOff.setSelection(1);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onItemSelected(AdapterView<?> view, View v, int position, long arg3) {
		Log.d(TAG, "onItemSelected: " + position);
		SharedPreferences.Editor editor = prefs.edit();

		boolean state = position == 0 ? true : false;

		switch (view.getId()) {
		case R.id.spnConAutoOnOff:
			Log.d(TAG, "spnConAutoOnOff, position: " + position);
			editor.putBoolean(Cons.SPNCONAUTOONOFF, state);
			break;

		case R.id.spnConRingVibOnOff:
			Log.d(TAG, "spnConRingVibOnOff, position: " + position);
			editor.putBoolean(Cons.SPNCONRINGVIBONOFF, state);
			break;

		case R.id.spnDisAutoOnOff:
			Log.d(TAG, "spnDisAutoOnOff, position: " + position);
			editor.putBoolean(Cons.SPNDISAUTOONOFF, state);
			break;

		case R.id.spnDisRingVibOnOff:
			Log.d(TAG, "spnDisRingVibOnOff, position: " + position);
			editor.putBoolean(Cons.SPNDISRINGVIBONOFF, state);
			break;
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else editor.apply();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConApp:
			startActivity(new Intent(this, InstalledAppActivity.class));
			break;

		case R.id.btnStartService:
			startService(new Intent(this, ServiceListener.class));
			finish();
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCheckedChanged(CompoundButton view, boolean state) {
		SharedPreferences.Editor editor = prefs.edit();

		switch (view.getId()) {
		case R.id.chkAutoStart:
			editor.putBoolean(Cons.CHKAUTOSTART, state);
			break;

		case R.id.chkWakeUp:
			editor.putBoolean(Cons.WAKEUP, state);
			break;

		case R.id.chkPowerConnected:
			editor.putBoolean(Cons.CHKPOWERCONNECTED, state);
			break;

		case R.id.chkConAutoRotate:
			editor.putBoolean(Cons.CHKCONAUTOROTATE, state);
			break;

		case R.id.chkConRingVib:
			editor.putBoolean(Cons.CHKCONRINGVIB, state);
			break;

		case R.id.chkConApp:
			editor.putBoolean(Cons.CHKCONAPP, state);
			break;

		case R.id.chkConMediaVolume:
			editor.putBoolean(Cons.CHKCONMEDIAVOL, state);
			skbConMediaVol.setEnabled(state);
			break;

		case R.id.chkDisAutoRotate:
			editor.putBoolean(Cons.CHKDISAUTOROTATE, state);
			break;

		case R.id.chkDisRingVib:
			editor.putBoolean(Cons.CHKDISRINGVIB, state);
			break;
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else editor.apply();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menuFeedback:
			String aEmailList[] = { "codeskraps@gmail.com" };

			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HeadSet - Feedback");
			emailIntent.setType("plain/text");

			startActivity(Intent.createChooser(emailIntent, "Send your feedback in:"));
			break;

		case R.id.menuBuyMeAPint:
			startActivity(new Intent(this, DonationActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Cons.SKBCONMEDIAVOL, progress);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else editor.apply();
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {}
}