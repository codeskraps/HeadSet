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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class InstalledAppActivity extends ListActivity {

	private static final String TAG = "HeadSet";
	private static final String SHAREDPREFS = "sharedprefs";
	private static final String STARTAPP = "startapp";
	private static final String STARTAPPPACKAGE = "startapppackage";
	private static final String STARTAPPACTIVITY = "startactivity";
	
	private PackageManager mPackageManager;
	private LinearLayout llProgress = null;
	private TextView txtProgress = null;

	/**
	 * This class is used to wrap ResolveInfo so that it can be filtered using
	 * ArrayAdapter's built int filtering logic, which depends on toString().
	 */
	private final class ResolveInfoWrapper {
		private ResolveInfo mInfo;

		public ResolveInfoWrapper(ResolveInfo info) {
			mInfo = info;
		}
		
		@Override
		public String toString() {
			return mInfo.loadLabel(mPackageManager).toString();
		}

		public ResolveInfo getInfo() {
			return mInfo;
		}
	}

	private class ActivityAdapter extends ArrayAdapter<ResolveInfoWrapper> {
		LayoutInflater mInflater;

		public ActivityAdapter(Activity activity,
				ArrayList<ResolveInfoWrapper> activities) {
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
				
				convertView.setTag(vHolder);
			}

			vHolder.txtView.setText(info.getInfo().loadLabel(mPackageManager));
			Drawable d = info.getInfo().loadIcon(mPackageManager);
			vHolder.imgView.setImageDrawable(d);

			return convertView;
		}
	}
	
	public class ViewHolder {
		ImageView imgView;
		TextView txtView;	
	}
	
	 private final class LoadingTask extends AsyncTask<Object, Object, ActivityAdapter> {
	        public void onPreExecute() {
	            llProgress.setVisibility(View.VISIBLE);
	            txtProgress.setText("Loading installed apps...");
	        }

	        public ActivityAdapter doInBackground(Object... params) {
	            // Load the activities
	            Intent queryIntent = new Intent(Intent.ACTION_MAIN);
	            List<ResolveInfo> list = mPackageManager.queryIntentActivities(queryIntent, 0);

	            // Sort the list
	            Collections.sort(list, new ResolveInfo.DisplayNameComparator(mPackageManager));

	            // Make the wrappers
	            ArrayList<ResolveInfoWrapper> activities = new ArrayList<ResolveInfoWrapper>(list.size());
	            for(ResolveInfo item : list) {
	                activities.add(new ResolveInfoWrapper(item));
	            }
	            return new ActivityAdapter(InstalledAppActivity.this, activities);
	        }

	        public void onPostExecute(ActivityAdapter result) {
	            llProgress.setVisibility(View.GONE);
	        	setListAdapter(result);
	        }
	    }
	 
	 @Override
	    protected void onCreate(Bundle savedState) {
	        super.onCreate(savedState);

	        setContentView(R.layout.listapps);

	        getListView().setTextFilterEnabled(true);

	        mPackageManager = getPackageManager();
	        
	        llProgress = (LinearLayout) findViewById(R.id.llProgress);
	        txtProgress = (TextView) findViewById(R.id.txtProgress);

	        // Start loading the data
	        new LoadingTask().execute((Object[]) null);
	    }

	    @Override
	    protected void onListItemClick(ListView list, View view, int position, long id) {
	        ResolveInfoWrapper wrapper = (ResolveInfoWrapper) getListAdapter().getItem(position);
	        ResolveInfo info = wrapper.getInfo();

//	        // Build the intent for the chosen activity
//	        Intent intent = new Intent();
//	        intent.setComponent(new ComponentName(info.activityInfo.applicationInfo.packageName,
//	                info.activityInfo.name));
//	        Intent result = new Intent();
//	        result.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//
//	        // Set the name of the activity
//	        result.putExtra(Intent.EXTRA_SHORTCUT_NAME, info.loadLabel(mPackageManager));
//
//	        // Build the icon info for the activity
//	        Drawable drawable = info.loadIcon(mPackageManager);
//	        if (drawable instanceof BitmapDrawable) {
//	            BitmapDrawable bd = (BitmapDrawable) drawable;
//	            result.putExtra(Intent.EXTRA_SHORTCUT_ICON, bd.getBitmap());
//	        }
////	        ShortcutIconResource iconResource = new ShortcutIconResource();
////	        iconResource.packageName = info.activityInfo.packageName;
////	        iconResource.resourceName = getResources().getResourceEntryName(info.getIconResource());
////	        result.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
//
//	        // Set the result
//	        setResult(RESULT_OK, result);
	        
	        Log.d(TAG, "info: " + info.loadLabel(mPackageManager));
	        Log.d(TAG, "info: " + info.activityInfo.applicationInfo.packageName);
	        Log.d(TAG, "info: " + info.activityInfo.name);
	        
	        SharedPreferences prefs = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			
			editor.putString(STARTAPP, info.loadLabel(mPackageManager).toString());
			editor.putString(STARTAPPPACKAGE, info.activityInfo.applicationInfo.packageName);
			editor.putString(STARTAPPACTIVITY, info.activityInfo.name);
			editor.commit();
	        
			finish();
	    }
}
