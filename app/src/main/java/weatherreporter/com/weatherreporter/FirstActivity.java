package weatherreporter.com.weatherreporter;

import weatherreporter.com.weatherreporter.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;

import weatherreporter.dataclasses.Data;
import weatherreporter.dataclasses.UrlTask;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.dataclasses.DayWeather;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class FirstActivity extends ActionBarActivity {

	private static final String greg = "firstActivity";
	public static Data newData;

	MyTabListener nowListener = new MyTabListener(FirstActivity.this, newData, "now",
			FragmentNow.class);
	MyTabListener forecastListener = new MyTabListener(FirstActivity.this, newData,
			"forecast", FragmentForecast.class);

	JSONObject weatherJson;
	Menu menu;
	public ActionBar bar;
	public Tab now;
	Tab forecast;
	public ImageView imv;
	boolean refreshAnim = false;
	MenuItem refreshItem;
	UrlTask urlTask = null;
	public boolean visibleOnScreen = false;
	public boolean showNewData = false;
	public SharedPreferences mSettings;

	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyLog.d("gerg", "onCreate");
		setContentView(R.layout.first_activity);

		bar = getActionBar();
		//bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		now = bar.newTab();
		now.setText("������");
		now.setTabListener(nowListener);
		bar.addTab(now);

		forecast = bar.newTab();
		forecast.setText("�������");
		forecast.setTabListener(forecastListener);
		bar.addTab(forecast);

		mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);
		newData = new Data();
		if (mSettings.contains("title")) { 			// get last save data
			MyLog.d(greg, "get last save data");
			newData.title = mSettings.getString("title", null);
			newData.urlStrDay = mSettings.getString("urlStrDay", null);
			newData.urlStrForecast = mSettings
					.getString("urlStrForecast", null);

			DayWeather dw = new DayWeather();
			try {
				MyLog.d(greg, "try pars from last data");
				
				dw.parsDay(new JSONObject(mSettings.getString("joDay", null)),
						this);
				newData.setNowWeather(dw);

				JSONObject jo = new JSONObject(mSettings.getString(
						"joForecast", null));
				JSONArray list = jo.getJSONArray("list");
				int d = list.length();
				DayWeather[] forecast = new DayWeather[d];

				for (int i = 0; i <= d - 1; i++) {
					JSONObject day = (JSONObject) list.get(i);
					MyLog.d(greg, "forecast for (i=" + i + ")");
					forecast[i] = new DayWeather();
					forecast[i].parsForecast(day, this);
				}
				newData.setForecast(forecast);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			bar.setTitle(newData.title);
		} else {

			// ask user chose city or coordinate
			MyLog.d(greg, "startChangeActivity");
			startChangeActivity();
		}
	}


	
	@Override
	protected void onResume() {
		super.onResume();
		MyLog.d("gerg", "onResume");
		if (showNewData) {
			afterUrlTask();
			showNewData = false;
		}
		visibleOnScreen = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyLog.d("gerg", "onPause");
		visibleOnScreen = false;
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MyLog.d("anim", "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);

		if (null == refreshItem) {
			// first time create menu
			refreshItem = menu.findItem(R.id.refresh);
			return true;
		}

		if (!refreshAnim && null != refreshItem.getActionView()) {
			// stop animation
			MyLog.d("anim", "set action view null");
			refreshItem.getActionView().clearAnimation();
			return true;
		} else {
			// start animation
			refreshItem = menu.findItem(R.id.refresh);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imv = (ImageView) inflater.inflate(R.layout.imv_refresh, null);
			Animation an = AnimationUtils.loadAnimation(this,
					R.anim.loadingrotate);
			an.setRepeatCount(Animation.INFINITE);
			imv.startAnimation(an);

			MyLog.d("anim", "set action view imv");
			refreshItem.setActionView(imv);
			refreshItem.setIcon(null);
			return true;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editCity:
			startChangeActivity();
			break;
		case R.id.refresh:
			refresh();
			break;
		}
		return true;
	}

	private void refresh() {
		// enough data to query
		if (null == newData.urlStrForecast || null == newData.urlStrDay
				|| null == newData.title) {

			Toast.makeText(this, "�������� �����", Toast.LENGTH_LONG).show();

			return;
		}
		stopQuery();
		// start new query with last data
//		urlTask = (UrlTask) new UrlTask(this, newData, newData.urlStrDay,
//				newData.urlStrForecast, newData.title).execute();
	}

	private void stopQuery() {// stop another query
		if (null != urlTask
				&& (!urlTask.isCancelled() || AsyncTask.Status.FINISHED != urlTask
						.getStatus()))
			urlTask.cancel(false);
	}

	public void startChangeActivity() {
		MyLog.d(greg, "startChangeActivity");
		Intent intent = new Intent(this, LocationActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		MyLog.d(greg, "onActivityResult");
		switch (resultCode) {
		case 1:
			// start new query with "city"
//			String city = intent.getStringExtra("city");
//			MyLog.d(greg, "city " + city);
//			stopQuery();
//			MyLog.d(greg, "start newURL request code 1");
//			urlTask = (UrlTask) new UrlTask(this, newData, newData.strWeather
//					+ "q=" + city, newData.strForecast + "q=" + city
//					+ "&cnt=14", city).execute();
//
//			break;
//		case 2:
//			// start new query with coordinate
//			String lat = intent.getStringExtra("lat");
//			String lon = intent.getStringExtra("lon");
//			MyLog.d(greg, "request code 2");
//			stopQuery();
//			urlTask = (UrlTask) new UrlTask(this, newData, newData.strWeather
//					+ "lat=" + lat + "&lon=" + lon, newData.strForecast
//					+ "lat=" + lat + "&lon=" + lon + "&cnt=14", lat + " " + lon)
//					.execute();
//			break;
		}
	}

	public void loadAnimationStart() {
		// start loading animation
		if (!refreshAnim) {
			MyLog.d("anim", "animation successful start ");
			refreshAnim = true;
			invalidateOptionsMenu();
		}
	}

	public void loadAnimationStop() {
		// stop loading animation
		if (refreshAnim) {
			MyLog.d("anim", "animation successful stop");
			refreshAnim = false;
			invalidateOptionsMenu();
		}
	}

	public void afterUrlTask() {
		if (now != null) {
			now.select();
			bar.setTitle(newData.title);
		}
	}
}
