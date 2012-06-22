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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DonationActivity extends Activity implements OnClickListener {
	private static final String TAG = DonationActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		setContentView(R.layout.donation);
		
		TextView txtGlass = (TextView) findViewById(R.id.txtGlass);
		TextView txtPint = (TextView) findViewById(R.id.txtPint);
		TextView txtMug = (TextView) findViewById(R.id.txtMug);
		TextView txtBarrel = (TextView) findViewById(R.id.txtBarrel);
		
		txtGlass.setOnClickListener(this);
		txtPint.setOnClickListener(this);
		txtMug.setOnClickListener(this);
		txtBarrel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		Intent marketIntent = new Intent(Intent.ACTION_VIEW);
		marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | 
				Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		String packageName = null;
		
		switch(v.getId()){
		case R.id.txtGlass:
			packageName = new String("com.codeskraps.glassdonation");
			break;
		case R.id.txtPint:
			packageName = new String("com.codeskraps.pintdonation");
			break;
		case R.id.txtMug:
			packageName = new String("com.codeskraps.mugdonation");
			break;
		case R.id.txtBarrel:
			packageName = new String("com.codeskraps.barreldonation");
			break;
		}
		
		try {
			startActivity(marketIntent.setData(Uri.parse("market://details?id=" + packageName)));
		} catch (Exception e) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
			browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | 
					Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivity(browserIntent);
			Log.e(TAG, e.getMessage());
		}
	}
}
