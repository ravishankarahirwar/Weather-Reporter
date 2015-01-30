package weatherreporter.com.weatherreporter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.ExpandableListView;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weatherreporter.errorhandling.ExceptionHandler;
import weatherreporter.managers.AlertManager;
import weatherreporter.util.AppMessage;
import weatherreporter.util.Constants;
import weatherreporter.util.ExpandableListAdapter;
import weatherreporter.util.JsonParser;
import weatherreporter.dataclasses.AllData;
import weatherreporter.dataclasses.DataFetchingTask;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.managers.RequestManager;
import weatherreporter.util.Validater;
import weatherreporter.util.WeatherApi;

public class HomeActivity extends ActionBarActivity implements RequestManager.RequestListner  {
    private static final String TAG = "HomeActivity";
    public static final String SHAREDPREFERENCE_NAME="LAST_DATA";
    public static AllData mAllData;
    public SharedPreferences mSettings;
    private AlertManager mAlertManager;
    boolean refreshAnim = false;
    private MenuItem refreshItem;
    private Typeface mHeaderFont, mLabelValueFont;
    private DataFetchingTask mDataFetchingTask = null;

    private ImageView iconWindAndPresser, iconSun,iconDetail, actionIconRefresh;
    private JsonParser mJsonParser;
    private TextView cityName,lastUpdateTime, titleNow, tempratureNow, tempratureMinimumNow,
            tempratureMaximumNow, titleDetail, labelHumidity, valueHumidity,
            labelPresser,valuePresser,
            titleWind, labelWindSpeed, speedValue, labelWindDegree, valueWindDegree,
            titleSun, valueSunRise, valueSunSet, titleForecast, date;


    private AdView mAdView;
    private AdRequest adRequestBaner;
    private InterstitialAd interstitial;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_home);
        init();

        /**Checking last data*/
        if (mSettings.contains("selectedCity")) {
            try{
            mJsonParser=new JsonParser(this);
            JSONObject weatherJsonObject = new JSONObject(mSettings.getString( "jsonObjectWeather", null));
            mJsonParser.parseWeather(weatherJsonObject);
                JSONObject jsonObjectForecast = new JSONObject(mSettings.getString( "jsonObjectForecast", null));
                mJsonParser.parsForcast(jsonObjectForecast);
            updateUserInterface();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        } else {
            /** ask user chose location*/
            MyLog.d(TAG, "start:LocationActivity");
            startChangeActivity();
        }

    }
    /** Initializing all variable*/
    private void init() {




        // Create the Admob Banner.
        mAdView = (AdView)findViewById(R.id.adView);
        adRequestBaner= new AdRequest.Builder().build();
        mAdView.loadAd(adRequestBaner);
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.ad_interstitial));

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);




        // get the listview
       expListView = (ExpandableListView) findViewById(R.id.lvExp);

       /// preparing list data
        prepareListData();


        mSettings = getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        mAllData = new AllData();
        mAlertManager=new AlertManager(this);
        listAdapter = new ExpandableListAdapter(this, mAllData.mAllForecastData.getList());
        // setting list adapter
        expListView.setAdapter(listAdapter);


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
        lastUpdateTime.setTypeface(mHeaderFont);
        titleNow.setTypeface(mHeaderFont);
        tempratureNow.setTypeface(mHeaderFont);
        tempratureMinimumNow.setTypeface(mHeaderFont);
        tempratureMaximumNow.setTypeface(mHeaderFont);
        titleDetail.setTypeface(mHeaderFont);

        labelHumidity.setTypeface(mHeaderFont);
        valueHumidity.setTypeface(mHeaderFont);

        labelPresser.setTypeface(mHeaderFont);
        valuePresser.setTypeface(mHeaderFont);

        titleWind.setTypeface(mHeaderFont);
        labelWindSpeed.setTypeface(mHeaderFont);
        speedValue.setTypeface(mHeaderFont);
        labelWindDegree.setTypeface(mHeaderFont);
        valueWindDegree.setTypeface(mHeaderFont);
        titleSun.setTypeface(mHeaderFont);
        valueSunRise.setTypeface(mHeaderFont);
        valueSunSet.setTypeface(mHeaderFont);
        titleForecast.setTypeface(mHeaderFont);
        date.setTypeface(mHeaderFont);

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

        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!refreshAnim) {
                    displayInterstitial();
                    startChangeActivity();
                }else{
                    mAlertManager.showAlert(R.string.wait);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MyLog.d("anim", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_home, menu);

        if (null == refreshItem) {
            /** first time create menu */
            refreshItem = menu.findItem(R.id.refresh);
            return true;
        }

        if (!refreshAnim && null != refreshItem.getActionView()) {
            /** stop animation */
            MyLog.d("anim", "set action view null");
            refreshItem.getActionView().clearAnimation();
            return true;
        } else {
            /** start animation */
            refreshItem = menu.findItem(R.id.refresh);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            actionIconRefresh = (ImageView) inflater.inflate(R.layout.imv_refresh, null);
            Animation an = AnimationUtils.loadAnimation(this,
                    R.anim.loadingrotate);
            an.setRepeatCount(Animation.INFINITE);
            actionIconRefresh.startAnimation(an);

            MyLog.d("anim", "set action view ");
            refreshItem.setActionView(actionIconRefresh);
            refreshItem.setIcon(null);
            return true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.editCity:
                if (!refreshAnim) {
                    displayInterstitial();
                    startChangeActivity();
                }else{
                    mAlertManager.showAlert(R.string.wait);
                }

                break;
            case R.id.refresh:
                refresh();
                break;
            case R.id.rateapp:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.URL_GOOGLE_PLAY_WEATHER_REPORTER));
                startActivity(intent);
                break;
            case R.id.shareapp:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try It Once!!");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.URL_GOOGLE_PLAY_WEATHER_REPORTER);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

                break;
            case R.id.moreapp:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.URL_GOOGLE_PLAY_ALL_APP));
                startActivity(intent);
                break;
            case R.id.facebookpage:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.URL_FACEBOOK_PAGE));
                startActivity(intent);
                break;
            case R.id.exit:
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
                /** start new query with "selectedLocation" */
                String selectedCity = intent.getStringExtra("selectedCity");
                MyLog.d(TAG, "selectedLocation " + selectedCity);
                stopQuery();
                MyLog.d(TAG, "start newURL request code 1");
                if(Validater.isInternetAvailable(this)) {
                    mDataFetchingTask = (DataFetchingTask) new DataFetchingTask(HomeActivity.this, WeatherApi.WEATHER
                            + "q=" + selectedCity, WeatherApi.FORECAST
                            + "q=" + selectedCity+ "&cnt=14",this);
                    mDataFetchingTask.execute();
                }else{
                    mAlertManager.showAlert(R.string.please_connect_to_internet);
                }
                break;

        }
    }

    public void startChangeActivity() {
        MyLog.d(TAG, "startChangeActivity");
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, 1);
    }

    /** stop another query */
    private void stopQuery() {
        if (null != mDataFetchingTask
                && (!mDataFetchingTask.isCancelled() || AsyncTask.Status.FINISHED != mDataFetchingTask
                .getStatus()))
            mDataFetchingTask.cancel(false);
    }

    public void loadAnimationStart() {
        /**  start loading animation */
        if (!refreshAnim) {
            MyLog.d("anim", "animation successful start ");
            refreshAnim = true;
            invalidateOptionsMenu();
        }
    }

    public void loadAnimationStop() {
        /**  stop loading animation */
        if (refreshAnim) {
            MyLog.d("anim", "animation successful stop");
            refreshAnim = false;
            invalidateOptionsMenu();
        }
    }



    @Override
    public void onResponse() {
        /** Updating user interface after getting response form server */
         updateUserInterface();
    }

    private void updateUserInterface() {
        if(mAllData!=null) {
            tempratureNow.setText(mAllData.mAllWeatherData.mMain.getTemperature() + getString(R.string.unit_temperature));
//            tempratureMinimumNow.setText(mAllData.mAllWeatherData.mMain.getMinimumTemperature() + getString(R.string.unit_temperature));
//            tempratureMaximumNow.setText(mAllData.mAllWeatherData.mMain.getMaximumTemperature() + getString(R.string.unit_temperature));
            cityName.setText(mAllData.mAllWeatherData.getCity());
            speedValue.setText(mAllData.mAllWeatherData.mWind.getSpeed() + getString(R.string.unit_wind));
            valueWindDegree.setText(mAllData.mAllWeatherData.mWind.getDeg());

            valueSunRise.setText(mAllData.mAllWeatherData.mSys.getSunrise());
            valueSunSet.setText(mAllData.mAllWeatherData.mSys.getSunset());

            valueHumidity.setText(mAllData.mAllWeatherData.mMain.getHumidity() + getString(R.string.percent));
            valuePresser.setText(mAllData.mAllWeatherData.mMain.getPressure());
            lastUpdateTime.setText("Last update : " + mAllData.mAllWeatherData.getLastUpdateTime());
            iconDetail.setImageResource(mAllData.mAllWeatherData.getIconId());


            if(!mAllData.mAllForecastData.getList().isEmpty()) {
                ((ExpandableListAdapter) expListView.getExpandableListAdapter()).notifyDataSetChanged();
                tempratureMinimumNow.setText(mAllData.mAllForecastData.list.get(0).getMinimumTemperature()+ getString(R.string.unit_temperature));
                tempratureMaximumNow.setText(mAllData.mAllForecastData.list.get(0).getMaximumTemperature()+ getString(R.string.unit_temperature));
            }


        }else{
            mAlertManager.showAlert(R.string.no_data_found);
        }
    }

    private void refresh() {
        if (mSettings.contains("selectedCity")) {            // get last save data
            MyLog.d(TAG, "get last save data");
            stopQuery();
            String selectedCity = mSettings.getString("selectedCity", null);

            if(Validater.isInternetAvailable(this)) {
                mDataFetchingTask = (DataFetchingTask) new DataFetchingTask(HomeActivity.this, WeatherApi.WEATHER
                        + "q=" + selectedCity, WeatherApi.FORECAST
                        + "q=" + selectedCity+ "&cnt=14",this);
                mDataFetchingTask.execute();
            }else{
                AppMessage.makeText(this, getString(R.string.please_connect_to_internet)).show();
              //  mAlertManager.showAlert(R.string.please_connect_to_internet);
            }
        } else {
            mAlertManager.showAlert(R.string.location_not_found);
            return;
        }

    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public void onBackPressed() {
        try {


            if (!mSettings.getBoolean("exit_dialog_nevershow_again", false)) {
                View checkBoxView = View.inflate(this, R.layout.dialogcheckbox, null);
                CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putBoolean("exit_dialog_nevershow_again", isChecked);
                        editor.commit();
                    }
                });

                checkBox.setText(getString(R.string.never_show_again));

                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle(getString(R.string.exit_alert_title));
                alertDialog.setIcon(R.drawable.apptheme_rate_star_big_on_holo_light);
                alertDialog.setMessage(getString(R.string.rate_us_message));
                alertDialog.setView(checkBoxView);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        displayInterstitial();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.rate_us), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {


                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=weatherreporter.com.weatherreporter"));
                        startActivity(intent);


                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.exit), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        displayInterstitial();

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            } else {
                displayInterstitial();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } catch (Exception e) {e.printStackTrace();
        }
    }
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


    }

}
