package com.codeskraps.headset.misc;

public class AppWrapper {

	private String app = null;
	private String packageName = null;
	private String activity = null;

	public AppWrapper(String app, String packageName, String activity) {
		this.app = app;
		this.packageName = packageName;
		this.activity = activity;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}
