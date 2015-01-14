package weatherreporter.com.weatherreporter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import weatherreporter.util.JsonParser;
import weatherreporter.dataclasses.AllData;
import weatherreporter.dataclasses.DataFetchingTask;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.managers.RequestManager;
import weatherreporter.util.WeatherApi;


public class HomeActivity extends ActionBarActivity implements RequestManager.RequestListner {
    public static final String TAG = "HomeActivity";
    public static AllData mAllData;
    public SharedPreferences mSettings;
    public boolean visibleOnScreen = false;
    public boolean showNewData = false;
    boolean refreshAnim = false;
    private MenuItem refreshItem;
    private Typeface mHeaderFont, mLabelValueFont;
    private DataFetchingTask mDataFetchingTask = null;

    private ImageView iconWindAndPresser, iconSun,iconDetail,imv;
    private JsonParser mJsonParser;
    private TextView cityName,lastUpdateTime, titleNow, tempratureNow, tempratureMinimumNow,
            tempratureMaximumNow, titleDetail, labelHumidity, valueHumidity,
            labelPresser,valuePresser,
            titleWind, labelWindSpeed, speedValue, labelWindDegree, valueWindDegree,
            titleSun, valueSunRise, valueSunSet, titleForecast, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);

        mAllData = new AllData();
        if (mSettings.contains("selectedCity")) {
            try{
            mJsonParser=new JsonParser(this);
            JSONObject weatherJsonObject = new JSONObject(mSettings.getString( "weatherJson", null));
            mJsonParser.parseWeather(weatherJsonObject);
            updateUserInterface();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        } else {
            // ask user chose city
            MyLog.d(TAG, "startChangeActivity");
            startChangeActivity();
        }

    }

    private void init() {

        iconWindAndPresser = (ImageView) findViewById(R.id.iconWindAndPresser);
        iconSun = (ImageView) findViewById(R.id.iconSun);
        iconDetail = (ImageView) findViewById(R.id.iconDetail);

        mHeaderFont = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        mLabelValueFont = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");

        cityName = (TextView) findViewById(R.id.cityName);
        lastUpdateTime = (TextView) findViewById(R.id.lastUpdateTime);
        titleNow = (TextView) findViewById(R.id.titleNow);
        tempratureNow = (TextView) findViewById(R.id.tempratureNow);
        tempratureMinimumNow = (TextView) findViewById(R.id.tempratureMinimumNow);
        tempratureMaximumNow = (TextView) findViewById(R.id.tempratureMaximumNow);
        titleDetail = (TextView) findViewById(R.id.titleDetail);
        labelHumidity = (TextView) findViewById(R.id.labelHumidity);
        valueHumidity = (TextView) findViewById(R.id.valueHumidity);
        labelPresser= (TextView) findViewById(R.id.labelPresser);
        valuePresser= (TextView) findViewById(R.id.valuePresser);
        titleWind = (TextView) findViewById(R.id.titleWind);
        labelWindSpeed = (TextView) findViewById(R.id.labelSpeed);
        speedValue = (TextView) findViewById(R.id.speedValue);
        labelWindDegree = (TextView) findViewById(R.id.labelWindDegree);
        valueWindDegree = (TextView) findViewById(R.id.valueWindDegree);
        titleSun = (TextView) findViewById(R.id.titleSun);
        valueSunRise = (TextView) findViewById(R.id.valueSunRise);
        valueSunSet = (TextView) findViewById(R.id.valueSunSet);
        titleForecast = (TextView) findViewById(R.id.titleForecast);
        date = (TextView) findViewById(R.id.date);

        cityName.setTypeface(mHeaderFont);
        lastUpdateTime.setTypeface(mLabelValueFont);
        titleNow.setTypeface(mHeaderFont);
        tempratureNow.setTypeface(mLabelValueFont);
        tempratureMinimumNow.setTypeface(mLabelValueFont);
        tempratureMaximumNow.setTypeface(mLabelValueFont);
        titleDetail.setTypeface(mHeaderFont);

        labelHumidity.setTypeface(mLabelValueFont);
        valueHumidity.setTypeface(mLabelValueFont);

        labelPresser.setTypeface(mLabelValueFont);
        valuePresser.setTypeface(mLabelValueFont);

        titleWind.setTypeface(mHeaderFont);
        labelWindSpeed.setTypeface(mLabelValueFont);
        speedValue.setTypeface(mLabelValueFont);
        labelWindDegree.setTypeface(mLabelValueFont);
        valueWindDegree.setTypeface(mLabelValueFont);
        titleSun.setTypeface(mHeaderFont);
        valueSunRise.setTypeface(mLabelValueFont);
        valueSunSet.setTypeface(mLabelValueFont);
        titleForecast.setTypeface(mHeaderFont);
        date.setTypeface(mLabelValueFont);

        cityName.setText(R.string.city_what);
        titleNow.setText(R.string.now);
        titleDetail.setText(R.string.detail);
        labelHumidity.setText(R.string.humidity);
        titleWind.setText(R.string.wind);
        titleSun.setText(R.string.sun);
        titleForecast.setText(R.string.forecast);
        labelWindSpeed.setText(R.string.speed);
        labelWindDegree.setText(R.string.degree);
        date.setText(R.string.date);
        labelPresser.setText(R.string.pressure);

        iconWindAndPresser.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wind_flow));
        iconSun.startAnimation(AnimationUtils.loadAnimation(this, R.anim.sun_rise_set));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MyLog.d("anim", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_home, menu);

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


    @Override
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        MyLog.d(TAG, "onActivityResult");
        switch (resultCode) {
            case 1:
                // start new query with "city"
                String selectedCity = intent.getStringExtra("selectedCity");
                MyLog.d(TAG, "city " + selectedCity);
                stopQuery();
                MyLog.d(TAG, "start newURL request code 1");
                mDataFetchingTask = (DataFetchingTask) new DataFetchingTask(HomeActivity.this, WeatherApi.WEATHER
                        + "q=" + selectedCity,this);
                mDataFetchingTask.execute();

                break;

        }
    }

    public void startChangeActivity() {
        MyLog.d(TAG, "startChangeActivity");
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, 1);
    }

    private void stopQuery() {// stop another query
        if (null != mDataFetchingTask
                && (!mDataFetchingTask.isCancelled() || AsyncTask.Status.FINISHED != mDataFetchingTask
                .getStatus()))
            mDataFetchingTask.cancel(false);
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



    @Override
    public void onResponse() {
        updateUserInterface();
    }

    private void updateUserInterface() {
        tempratureNow.setText(mAllData.mAllWeatherData.mMain.getTemperature() + (char) 0x00B0 + "C");
        tempratureMinimumNow.setText(mAllData.mAllWeatherData.mMain.getMinimumTemperature() + (char) 0x00B0 + "C");
        tempratureMaximumNow.setText(mAllData.mAllWeatherData.mMain.getMaximumTemperature() + (char) 0x00B0 + "C");
        cityName.setText(mAllData.mAllWeatherData.getCity());
        speedValue.setText(mAllData.mAllWeatherData.mWind.getSpeed()+" mps");
        valueWindDegree.setText(mAllData.mAllWeatherData.mWind.getDeg());

        valueSunRise.setText(mAllData.mAllWeatherData.mSys.getSunrise());
        valueSunSet.setText(mAllData.mAllWeatherData.mSys.getSunset());

        valueHumidity.setText(mAllData.mAllWeatherData.mMain.getHumidity()+getString(R.string.percent));
        valuePresser.setText(mAllData.mAllWeatherData.mMain.getPressure());
        lastUpdateTime.setText("Last update : "+mAllData.mAllWeatherData.getLastUpdateTime());
        iconDetail.setImageResource(mAllData.mAllWeatherData.getIconId());
    }

    private void refresh() {
        if (mSettings.contains("selectedCity")) {            // get last save data
            MyLog.d(TAG, "get last save data");
            stopQuery();
            String selectedCity = mSettings.getString("selectedCity", null);
            mDataFetchingTask = (DataFetchingTask) new DataFetchingTask(HomeActivity.this, WeatherApi.WEATHER
                    + "q=" + selectedCity,this);
            mDataFetchingTask.execute();
        } else {
            Toast.makeText(this, "Please Select City First", Toast.LENGTH_LONG).show();
            return;
        }

    }

}
