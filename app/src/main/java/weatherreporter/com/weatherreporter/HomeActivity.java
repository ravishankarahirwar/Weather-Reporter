package weatherreporter.com.weatherreporter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import weatherreporter.dataclasses.AllData;
import weatherreporter.dataclasses.Data;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.dataclasses.UrlTask;


public class HomeActivity extends ActionBarActivity {
    public static final String TAG="HomeActivity";
    public SharedPreferences mSettings;
    private UrlTask urlTask = null;
    public static Data newData;
    public static AllData mAllData;

    boolean refreshAnim = false;
    MenuItem refreshItem;

    public boolean visibleOnScreen = false;
    public boolean showNewData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSettings = getSharedPreferences("LAST_DATA", Context.MODE_PRIVATE);
        newData = new Data();
        mAllData=new AllData();
        if (mSettings.contains("title")) {

        } else {
           // ask user chose city or coordinate
            MyLog.d(TAG, "startChangeActivity");
            startChangeActivity();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        MyLog.d(TAG, "onActivityResult");
        switch (resultCode) {
            case 1:
                // start new query with "city"
                String city = intent.getStringExtra("city");
                MyLog.d(TAG, "city " + city);
                stopQuery();
                MyLog.d(TAG, "start newURL request code 1");
                urlTask =(UrlTask) new UrlTask(HomeActivity.this, mAllData, newData.strWeather
                        + "q=" + city, newData.strForecast + "q=" + city
                        + "&cnt=14", city);
                urlTask.execute();

                break;
            case 2:
                // start new query with coordinate
                String lat = intent.getStringExtra("lat");
                String lon = intent.getStringExtra("lon");
                MyLog.d(TAG, "request code 2");
                stopQuery();
                urlTask = (UrlTask) new UrlTask(HomeActivity.this, newData, newData.strWeather
                        + "lat=" + lat + "&lon=" + lon, newData.strForecast
                        + "lat=" + lat + "&lon=" + lon + "&cnt=14", lat + " " + lon)
                        .execute();
                break;
        }
    }

    public void startChangeActivity() {
        MyLog.d(TAG, "startChangeActivity");
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, 1);
    }
    private void stopQuery() {// stop another query
        if (null != urlTask
                && (!urlTask.isCancelled() || AsyncTask.Status.FINISHED != urlTask
                .getStatus()))
            urlTask.cancel(false);
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
//        if (now != null) {
//            now.select();
//            bar.setTitle(newData.title);
//        }
    }
}
