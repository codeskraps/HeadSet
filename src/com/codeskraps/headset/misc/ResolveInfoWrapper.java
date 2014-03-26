package com.codeskraps.headset.misc;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class ResolveInfoWrapper {
	private ResolveInfo info;
	private boolean checked = false;
	private PackageManager pm;

	public ResolveInfoWrapper(PackageManager pm, ResolveInfo info) {
		this.pm = pm;
		this.info = info;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Drawable getIcon() {
		return info.loadIcon(pm);
	}

	public String getPackageName() {
		return info.activityInfo.packageName;
	}

	public String getActivity() {
		return info.activityInfo.name;
	}

	@Override
	public String toString() {
		return info.loadLabel(pm).toString();
	}
}
