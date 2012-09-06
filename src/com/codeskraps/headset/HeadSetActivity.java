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
import android.content.Intent;
import android.content.SharedPreferences;
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

public class HeadSetActivity extends Activity implements OnItemSelectedListener, OnClickListener, OnCheckedChangeListener, OnSeekBarChangeListener {

	private static final String TAG = "HeadSet";
	private static final String SHAREDPREFS = "sharedprefs";
	private static final String CHKAUTOSTART = "chkautostart";
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
		
	private SharedPreferences prefs = null;
	private CheckBox chkAutoStart = null;
	private CheckBox chkWakeUp = null;
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
	private Button btnStartService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "HeadSetActivity onCreate");
		
		setContentView(R.layout.main);
		
		prefs = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
		
		chkAutoStart = (CheckBox) findViewById(R.id.chkAutoStart);
		chkWakeUp = (CheckBox) findViewById(R.id.chkWakeUp);
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
		btnStartService = (Button) findViewById(R.id.btnStartService);
		
		chkAutoStart.setOnCheckedChangeListener(this);
		chkWakeUp.setOnCheckedChangeListener(this);
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
		btnStartService.setOnClickListener(this);
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item, 
        		getResources().getStringArray(R.array.onOff));
		
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnConRotateOnOff.setAdapter(arrayAdapter);
		spnConRingVibOnOff.setAdapter(arrayAdapter);
		spnDisRotateOnOff.setAdapter(arrayAdapter);
		spnDisRingVibOnOff.setAdapter(arrayAdapter);
		
		skbConMediaVol.setProgress(prefs.getInt(SKBCONMEDIAVOL, 0));
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (prefs.getBoolean(CHKAUTOSTART, false))
			chkAutoStart.setChecked(true);
		else chkAutoStart.setChecked(false);
		
		if (prefs.getBoolean(WAKEUP, false))
			chkWakeUp.setChecked(true);
		else chkWakeUp.setChecked(false);
		
		if (prefs.getBoolean(CHKCONAUTOROTATE, false))
			chkConAutoRotate.setChecked(true);
		else chkConAutoRotate.setChecked(false);
		
		if (prefs.getBoolean(SPNCONAUTOONOFF, true)) 
			spnConRotateOnOff.setSelection(0);
		else spnConRotateOnOff.setSelection(1);
		
		if (prefs.getBoolean(CHKCONRINGVIB, false))
			chkConRingVib.setChecked(true);
		else chkConRingVib.setChecked(false);
		
		if (prefs.getBoolean(SPNCONRINGVIBONOFF, true)) 
			spnConRingVibOnOff.setSelection(0);
		else spnConRingVibOnOff.setSelection(1);
		
		if (prefs.getBoolean(CHKCONAPP, false))
			chkConApp.setChecked(true);
		else chkConApp.setChecked(false);
		
		if (prefs.getString(STARTAPP, STARTAPP).equals(STARTAPP))
			btnConApp.setText("Set app...");
		else btnConApp.setText(prefs.getString(STARTAPP, STARTAPP));
		
		if (prefs.getBoolean(CHKCONMEDIAVOL, false)) {
			chkConMediaVol.setChecked(true);
			skbConMediaVol.setEnabled(true);
		} else {
			chkConMediaVol.setChecked(false);
			skbConMediaVol.setEnabled(false);
		}
		
		if (prefs.getBoolean(CHKDISAUTOROTATE, false))
			chkDisAutoRotate.setChecked(true);
		else chkDisAutoRotate.setChecked(false);
		
		if (prefs.getBoolean(SPNDISAUTOONOFF, true))
			spnDisRotateOnOff.setSelection(0);
		else spnDisRotateOnOff.setSelection(1);
		
		if (prefs.getBoolean(CHKDISRINGVIB, false))
			chkDisRingVib.setChecked(true);
		else chkDisRingVib.setChecked(false);
		
		if (prefs.getBoolean(SPNDISRINGVIBONOFF, true)) 
			spnDisRingVibOnOff.setSelection(0);
		else spnDisRingVibOnOff.setSelection(1);
	}

	@Override
	public void onItemSelected(AdapterView<?> view, View v, int position, long arg3) {
		Log.d(TAG, "onItemSelected: " + position);
		SharedPreferences.Editor editor = prefs.edit();
		
		switch (view.getId()){
		case R.id.spnConAutoOnOff:
			Log.d(TAG, "spnConAutoOnOff, position: " + position);
			
			if (position == 0) 
				editor.putBoolean(SPNCONAUTOONOFF, true);
			else editor.putBoolean(SPNCONAUTOONOFF, false);
			
			break;
		
		case R.id.spnConRingVibOnOff:
			Log.d(TAG, "spnConRingVibOnOff, position: " + position);
			
			if (position == 0) 
				editor.putBoolean(SPNCONRINGVIBONOFF, true);
			else editor.putBoolean(SPNCONRINGVIBONOFF, false);
			
			break;
			
		case R.id.spnDisAutoOnOff: 
			Log.d(TAG, "spnDisAutoOnOff, position: " + position);
			
			if (position == 0) 
				editor.putBoolean(SPNDISAUTOONOFF, true);
			else editor.putBoolean(SPNDISAUTOONOFF, false);
			
			break;
			
		case R.id.spnDisRingVibOnOff:
			Log.d(TAG, "spnDisRingVibOnOff, position: " + position);
			
			if (position == 0) 
				editor.putBoolean(SPNDISRINGVIBONOFF, true);
			else editor.putBoolean(SPNDISRINGVIBONOFF, false);
			
			break;
		}
	    editor.commit();
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

	@Override
	public void onCheckedChanged(CompoundButton view, boolean state) {
		SharedPreferences.Editor editor = prefs.edit();
		
		switch(view.getId()){
		case R.id.chkAutoStart:
			
			if (state) editor.putBoolean(CHKAUTOSTART, true);
			else editor.putBoolean(CHKAUTOSTART, false);
			
			break;
		
		case R.id.chkWakeUp:
			
			if (state) editor.putBoolean(WAKEUP, true);
			else editor.putBoolean(WAKEUP, false);
			
			break;
			
		case R.id.chkConAutoRotate:
			
			if (state) editor.putBoolean(CHKCONAUTOROTATE, true);
			else editor.putBoolean(CHKCONAUTOROTATE, false);
			
			break;
		
		case R.id.chkConRingVib:
			
			if (state) editor.putBoolean(CHKCONRINGVIB, true);
			else editor.putBoolean(CHKCONRINGVIB, false);
			
			break;
			
		case R.id.chkConApp:
			
			if (state) editor.putBoolean(CHKCONAPP, true);
			else editor.putBoolean(CHKCONAPP, false);
			
			break;
		
		case R.id.chkConMediaVolume:
			
			if (state) {
				editor.putBoolean(CHKCONMEDIAVOL, true);
				skbConMediaVol.setEnabled(true);
			} else {
				editor.putBoolean(CHKCONMEDIAVOL, false);
				skbConMediaVol.setEnabled(false);
			}
			
			break;
		
		case R.id.chkDisAutoRotate:
			
			if (state) editor.putBoolean(CHKDISAUTOROTATE, true);
			else editor.putBoolean(CHKDISAUTOROTATE, false);
			
			break;
		
		case R.id.chkDisRingVib:
			
			if (state) editor.putBoolean(CHKDISRINGVIB, true);
			else editor.putBoolean(CHKDISRINGVIB, false);
			
			break;
		}
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.menuFeedback:
			
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
					  
			String aEmailList[] = { "codeskraps@gmail.com" };  
			  
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

	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(SKBCONMEDIAVOL, progress);
		
		editor.commit();
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {}
}