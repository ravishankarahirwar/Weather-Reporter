package weatherreporter.com.weatherreporter;

import android.content.Context;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weatherreporter.errorhandling.ExceptionHandler;
import weatherreporter.managers.AlertManager;
import weatherreporter.util.Constants;
import weatherreporter.util.ExpandableListAdapter;
import weatherreporter.util.JsonParser;
import weatherreporter.dataclasses.AllData;
import weatherreporter.dataclasses.DataFetchingTask;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.managers.RequestManager;
import weatherreporter.util.Validater;
import weatherreporter.util.WeatherApi;

public class HomeActivity extends ActionBarActivity implements RequestManager.RequestListner ,ExpandableListView.OnGroupExpandListener,ExpandableListView.OnGroupCollapseListener {
    private static final String TAG = "HomeActivity";
    public static final String SHAREDPREFERENCE_NAME="LAST_DATA";
    public static AllData mAllData;
    public SharedPreferences mSettings;
    private AlertManager mAlertManager;
    boolean refreshAnim = false;
    private MenuItem refreshItem;
    private Typeface mHeaderFont, mLabelValueFont;
    private DataFetchingTask mDataFetchingTask = null;
    private LinearLayout detailForecast;
    private LayoutInflater mInflater;
    private View detailForecastRowView;
    private ImageView iconWindAndPresser, iconSun,iconDetail, actionIconRefresh;
    private JsonParser mJsonParser;
    private TextView cityName,lastUpdateTime, titleNow, tempratureNow, tempratureMinimumNow,
            tempratureMaximumNow, titleDetail, labelHumidity, valueHumidity,
            labelPresser,valuePresser,
            titleWind, labelWindSpeed, speedValue, labelWindDegree, valueWindDegree,
            titleSun, valueSunRise, valueSunSet, titleForecast, date;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ScrollView scrollView;


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


        // get the listview
       expListView = (ExpandableListView) findViewById(R.id.lvExp);

       /// preparing list data
        prepareListData();




        mInflater = LayoutInflater.from(this);
        detailForecast = (LinearLayout) findViewById(R.id.detailForecast);

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
            tempratureMinimumNow.setText(mAllData.mAllWeatherData.mMain.getMinimumTemperature() + getString(R.string.unit_temperature));
            tempratureMaximumNow.setText(mAllData.mAllWeatherData.mMain.getMaximumTemperature() + getString(R.string.unit_temperature));
            cityName.setText(mAllData.mAllWeatherData.getCity());
            speedValue.setText(mAllData.mAllWeatherData.mWind.getSpeed() + getString(R.string.unit_wind));
            valueWindDegree.setText(mAllData.mAllWeatherData.mWind.getDeg());

            valueSunRise.setText(mAllData.mAllWeatherData.mSys.getSunrise());
            valueSunSet.setText(mAllData.mAllWeatherData.mSys.getSunset());

            valueHumidity.setText(mAllData.mAllWeatherData.mMain.getHumidity() + getString(R.string.percent));
            valuePresser.setText(mAllData.mAllWeatherData.mMain.getPressure());
            lastUpdateTime.setText("Last update : " + mAllData.mAllWeatherData.getLastUpdateTime());
            iconDetail.setImageResource(mAllData.mAllWeatherData.getIconId());

           /* List<String> nowShowing = new ArrayList<String>();
            nowShowing.add("The Conjuring");
            nowShowing.add("Despicable Me 2");
            nowShowing.add("Turbo");
            nowShowing.add("Grown Ups 2");
            nowShowing.add("Red 2");
            nowShowing.add("The Wolverine");*/

           /* for (int i = 0; i < mAllData.mAllForecastData.getList().size(); i++) {
                listDataHeader.add(mAllData.mAllForecastData.list.get(i).dt);
                listDataChild.put(listDataHeader.get(i), nowShowing);

            }*/
            ((ExpandableListAdapter)expListView.getExpandableListAdapter()).notifyDataSetChanged();
            tempratureMinimumNow.setText(mAllData.mAllForecastData.list.get(0).getMinimumTemperature());
            tempratureMaximumNow.setText(mAllData.mAllForecastData.list.get(0).getMaximumTemperature());

           // detailForecast.removeAllViews();
           /* if(!mAllData.mAllForecastData.getList().isEmpty()) {
                tempratureMinimumNow.setText(mAllData.mAllForecastData.list.get(0).getMinimumTemperature());
                tempratureMaximumNow.setText(mAllData.mAllForecastData.list.get(0).getMaximumTemperature());
                for (int i = 0; i < mAllData.mAllForecastData.getList().size(); i++) {
                    detailForecastRowView = mInflater.inflate(R.layout.detail_forecast_row, null);
                    ((TextView) detailForecastRowView.findViewById(R.id.date)).setTypeface(mLabelValueFont);
                    ((TextView) detailForecastRowView.findViewById(R.id.minTemp)).setTypeface(mLabelValueFont);
                    ((TextView) detailForecastRowView.findViewById(R.id.maxTemp)).setTypeface(mLabelValueFont);

                    ((TextView) detailForecastRowView.findViewById(R.id.date)).setTag("day" + i);
                    ((TextView) detailForecastRowView.findViewById(R.id.minTemp)).setTag("dayMinTemp" + i);
                    ((TextView) detailForecastRowView.findViewById(R.id.maxTemp)).setTag("daymaxTemp" + i);

                    ((TextView) detailForecastRowView.findViewById(R.id.date)).setText(mAllData.mAllForecastData.list.get(i).dt);
                    //((TextView) detailForecastRowView.findViewById(R.id.date)).setText("Day"+1);
                    ((TextView) detailForecastRowView.findViewById(R.id.minTemp)).setText(mAllData.mAllForecastData.list.get(i).getMinimumTemperature());
                    ((TextView) detailForecastRowView.findViewById(R.id.maxTemp)).setText(mAllData.mAllForecastData.list.get(i).getMaximumTemperature());
                    detailForecast.addView(detailForecastRowView);
                }
           }
*/
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
                mAlertManager.showAlert(R.string.please_connect_to_internet);
            }
        } else {
            mAlertManager.showAlert(R.string.location_not_found);
            return;
        }

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

      // Adding child data
      /*  listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }
    @Override
    public void onGroupExpand(int groupPosition) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) expListView.getLayoutParams();
        param.height = (3 * expListView.getHeight());
        expListView.setLayoutParams(param);
        expListView.refreshDrawableState();
        scrollView.refreshDrawableState();
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) expListView.getLayoutParams();
        param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        expListView.setLayoutParams(param);
        expListView.refreshDrawableState();
        scrollView.refreshDrawableState();
    }

}
