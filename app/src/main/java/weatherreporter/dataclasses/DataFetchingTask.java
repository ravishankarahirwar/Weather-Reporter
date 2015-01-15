package weatherreporter.dataclasses;

/**
 * Created by acer on 1/12/2015.
 */


import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import weatherreporter.com.weatherreporter.HomeActivity;
import weatherreporter.com.weatherreporter.R;
import weatherreporter.managers.AlertManager;
import weatherreporter.managers.RequestManager;
import weatherreporter.util.JsonParser;

public class DataFetchingTask extends AsyncTask<Void, Integer, Integer> {

    JSONObject weatherJson;

    private JsonParser mJsonParser;
    private RequestManager.RequestListner requestListner;
    private RequestManager mRequestManager;
    private String weatherUrl;
    private String forecastUrl;
    private AlertManager mAlertManager;

    private HomeActivity contextActivity;
    private String greg = "UrlTask";


    public DataFetchingTask(HomeActivity contextActivity, String weatherUrl,
                            RequestManager.RequestListner requestListner) {
        MyLog.d(greg, "constructor ");

        mAlertManager=new AlertManager(contextActivity);
        this.weatherUrl = weatherUrl; // weather
        this.contextActivity = contextActivity;
        mJsonParser = new JsonParser(contextActivity);
        this.requestListner = requestListner;
        mRequestManager=new RequestManager();
    }

    @Override
    protected void onPreExecute() {
        contextActivity.loadAnimationStart();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... arg0) {
        if (isCancelled())
            return null;

        try {// weather
            MyLog.d(greg, weatherUrl);
            weatherJson = mRequestManager.connectToOpenWeatherServer(weatherUrl);
            mJsonParser.parseWeather(weatherJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } catch (ParseException e) {
            e.printStackTrace();
            return 3;
        }

        if (isCancelled())
            return null;
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            /** if data was not received */
            case 2:
                mAlertManager.showAlert(R.string.no_data_found);
                break;
            case 1:
                mAlertManager.showAlert(R.string.no_data_found);
                break;
            case 3:
                Toast.makeText(contextActivity,
                        "error",
                        Toast.LENGTH_LONG).show();
                break;

            /** if data has been successfully received */
            case 0:
                // save query string and title to refresh data next time

                Editor editor = contextActivity.mSettings.edit();
                editor.putString("selectedCity", HomeActivity.mAllData.mAllWeatherData.getCity());
                editor.putString("urlStrDay", weatherUrl);
                editor.putString("weatherJson", String.valueOf(weatherJson));
                editor.apply();
                requestListner.onResponse();

        }
        contextActivity.loadAnimationStop();
    }




}

