package com.codeskraps.headset.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeskraps.headset.R;
import com.codeskraps.headset.misc.AppWrapper;
import com.codeskraps.headset.misc.Cons;
import com.codeskraps.headset.misc.HeadSetApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GhostActivity extends Activity {
	private static final String TAG = GhostActivity.class.getSimpleName();

	private LaunchAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences prefs = getSharedPreferences(Cons.SHAREDPREFS, MODE_PRIVATE);

		if (prefs.getBoolean(Cons.WAKEUP, false)) {
			((HeadSetApplication) getApplication()).WakeLock_aquire();

			final Window win = getWindow();
			win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		}

		String json = prefs.getString(Cons.STARTUPAPPS, new String());
		ArrayList<AppWrapper> apps = new ArrayList<AppWrapper>();

		if (json.length() > 1) {
			apps = new Gson().fromJson(json, new TypeToken<ArrayList<AppWrapper>>() {}.getType());
			Log.v(TAG, "app:" + apps.size());
		}

		adapter = new LaunchAdapter(this, apps);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (adapter.getCount() > 1) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog,
					(ViewGroup) findViewById(R.id.dialog_root));
			GridView gridview = (GridView) layout.findViewById(R.id.gridview);
			gridview.setAdapter(adapter);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_name);
			builder.setView(layout);
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							GhostActivity.this.finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();

		} else if (adapter.getCount() == 1) {
			launchApp((AppWrapper) adapter.getItem(0));
		}
	}

	private void launchApp(AppWrapper wrapper) {
		Log.v(TAG, "Launching:" + wrapper.getPackageName());
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.setComponent(new ComponentName(wrapper.getPackageName(), wrapper.getActivity()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(i);
		} catch (Exception e) {}
		finish();
	}

	private class LaunchAdapter extends BaseAdapter implements OnClickListener {
		private LayoutInflater inflater;
		private ArrayList<AppWrapper> apps = null;

		public LaunchAdapter(Context context, ArrayList<AppWrapper> apps) {
			this.apps = apps;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (apps != null) return apps.size();
			return -1;
		}

		@Override
		public Object getItem(int position) {
			return apps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vHolder = null;

			if (convertView != null) vHolder = (ViewHolder) convertView.getTag();
			else {
				convertView = (View) inflater.inflate(R.layout.dialog_item, null);

				vHolder = new ViewHolder();
				vHolder.imageView = ((ImageView) convertView.findViewById(R.id.listImage));
				vHolder.textView = ((TextView) convertView.findViewById(R.id.lstText));
				vHolder.imageView.setOnClickListener(this);
				vHolder.textView.setOnClickListener(this);

				convertView.setTag(vHolder);
			}

			vHolder.imageView.setId(position);
			vHolder.textView.setId(position);
			AppWrapper launch = apps.get(position);
			vHolder.textView.setText(launch.getApp());
			try {
				String pkg = launch.getPackageName();
				Drawable icon = getPackageManager().getApplicationIcon(pkg);
				vHolder.imageView.setImageDrawable(icon);
			} catch (PackageManager.NameNotFoundException e) {
				Log.i(TAG, "Handled: " + e.getMessage(), e);
				vHolder.imageView.setImageBitmap(null);
				vHolder.textView.setText("App Removed");
				updateJson(e.getMessage());
			}

			return convertView;
		}

		@Override
		public void onClick(View v) {
			AppWrapper clicked = apps.get(v.getId());
			Log.d(TAG, "onClick" + clicked.getPackageName());

			launchApp(clicked);
		}
	}

	private void updateJson(String packageName) {

	}

	private class ViewHolder {
		ImageView imageView;
		TextView textView;
	}
}
