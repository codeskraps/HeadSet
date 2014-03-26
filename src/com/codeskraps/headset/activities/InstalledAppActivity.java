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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeskraps.headset.R;
import com.codeskraps.headset.misc.AppWrapper;
import com.codeskraps.headset.misc.Cons;
import com.codeskraps.headset.misc.ResolveInfoWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InstalledAppActivity extends ListActivity implements OnClickListener {
	private static final String TAG = InstalledAppActivity.class.getSimpleName();

	private LinearLayout llProgress = null;
	private TextView txtProgress = null;

	private ArrayList<AppWrapper> apps = new ArrayList<AppWrapper>();

	private class ActivityAdapter extends ArrayAdapter<ResolveInfoWrapper> implements
			OnCheckedChangeListener {
		LayoutInflater mInflater;

		public ActivityAdapter(Activity activity, ArrayList<ResolveInfoWrapper> activities) {
			super(activity, 0, activities);
			mInflater = activity.getLayoutInflater();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ResolveInfoWrapper info = getItem(position);
			ViewHolder vHolder = null;

			if (convertView != null) {
				vHolder = (ViewHolder) convertView.getTag();
			} else {
				convertView = (View) mInflater.inflate(R.layout.row, null);

				vHolder = new ViewHolder();
				vHolder.imgView = ((ImageView) convertView.findViewById(R.id.rowImage));
				vHolder.txtView = ((TextView) convertView.findViewById(R.id.rowText));
				vHolder.chkView = ((CheckBox) convertView.findViewById(R.id.rowCheck));
				vHolder.chkView.setOnCheckedChangeListener(this);

				convertView.setTag(vHolder);
			}

			vHolder.txtView.setText(info.toString());
			vHolder.imgView.setImageDrawable(info.getIcon());
			vHolder.chkView.setTag(position);
			vHolder.chkView.setChecked(info.getChecked());

			return convertView;
		}

		@Override
		public void onCheckedChanged(CompoundButton button, boolean state) {
			int position = (Integer) button.getTag();
			final ResolveInfoWrapper info = getItem(position);
			info.setChecked(state);

			if (state) {
				String app = info.toString();
				String packageName = info.getPackageName();
				String activity = info.getActivity();

				boolean found = false;
				for (AppWrapper wrapper : apps) {
					if (info.getPackageName().equals(wrapper.getPackageName())) found = true;
				}
				if (!found) {
					apps.add(new AppWrapper(app, packageName, activity));
					Log.v(TAG, "Added: " + info.getPackageName());
				}

			} else {
				apps.remove(info.getPackageName());
				for (AppWrapper wrapper : apps) {
					if (info.getPackageName().equals(wrapper.getPackageName())) {
						boolean result = apps.remove(wrapper);
						Log.v(TAG, "removed:" + info.getPackageName() + ", result:" + result);
						break;
					}
				}
			}

			Log.v(TAG, "lstCount:" + apps.size());
		}
	}

	public class ViewHolder {
		CheckBox chkView;
		ImageView imgView;
		TextView txtView;
	}

	private final class LoadingTask extends AsyncTask<Object, Object, ActivityAdapter> {
		public void onPreExecute() {
			llProgress.setVisibility(View.VISIBLE);
			txtProgress.setText("Loading installed apps...");
		}

		public ActivityAdapter doInBackground(Object... params) {
			try {
				// Load the activities
				Intent queryIntent = new Intent(Intent.ACTION_MAIN);
				queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				List<ResolveInfo> list = getPackageManager().queryIntentActivities(queryIntent, 0);

				// Sort the list
				Collections.sort(list, new ResolveInfo.DisplayNameComparator(getPackageManager()));

				// Make the wrappers
				ArrayList<ResolveInfoWrapper> activities = new ArrayList<ResolveInfoWrapper>(
						list.size());
				SharedPreferences prefs = getSharedPreferences(Cons.SHAREDPREFS, MODE_PRIVATE);
				String json = prefs.getString(Cons.STARTUPAPPS, new String());
				Log.v(TAG, "Json:" + json);

				if (json.length() > 1) {
					apps = new Gson().fromJson(json,
							new TypeToken<ArrayList<AppWrapper>>() {}.getType());
					Log.v(TAG, "app:" + apps.size());
				}

				for (ResolveInfo item : list) {
					ResolveInfoWrapper wrapper = new ResolveInfoWrapper(getPackageManager(), item);
					if (apps != null) {
						for (AppWrapper launch : apps) {
							if (launch.getPackageName().equals(
									item.activityInfo.applicationInfo.packageName))
								wrapper.setChecked(true);
						}
					}

					activities.add(wrapper);
				}
				return new ActivityAdapter(InstalledAppActivity.this, activities);
			} catch (Exception e) {
				Log.i(TAG, "Handled: " + e.getMessage(), e);
			}
			return null;
		}

		public void onPostExecute(ActivityAdapter result) {
			if (result != null) {
				llProgress.setVisibility(View.GONE);
				setListAdapter(result);
			} else Toast.makeText(InstalledAppActivity.this, "PackageManager just died!!!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		setContentView(R.layout.listapps);

		getListView().setTextFilterEnabled(true);
		findViewById(R.id.btnDone).setOnClickListener(this);

		llProgress = (LinearLayout) findViewById(R.id.llProgress);
		txtProgress = (TextView) findViewById(R.id.txtProgress);

		// Start loading the data
		new LoadingTask().execute((Object[]) null);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onClick(View v) {
		SharedPreferences prefs = getSharedPreferences(Cons.SHAREDPREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		String json = new String();

		if (apps.size() > 0) {
			json = new Gson().toJson(apps);
			Log.v(TAG, "json app:" + json);
			editor.putString(Cons.STARTUPAPPS, json);
		} else editor.putString(Cons.STARTUPAPPS, new String());

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else editor.apply();

		finish();
	}
}
