package weatherreporter.dataclasses;

/**
 * Created by Ravi on 1/12/2015.
 */


import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import weatherreporter.com.weatherreporter.HomeActivity;
import weatherreporter.com.weatherreporter.R;
import weatherreporter.managers.AlertManager;
import weatherreporter.managers.RequestManager;
import weatherreporter.util.JsonParser;

public class DataFetchingTask extends AsyncTask<Void, Integer, Integer> {
    private static final String TAG = "DataFetchingTask";
    JSONObject jsonObjectWeather,jsonObjectForecast;

    private JsonParser mJsonParser;
    private RequestManager.RequestListner requestListner;
    private RequestManager mRequestManager;
    private String mUrlWeather,mUrlForcast;
    private AlertManager mAlertManager;

    private HomeActivity contextActivity;



    public DataFetchingTask(HomeActivity contextActivity, String urlWeather,String urlForcast,
                            RequestManager.RequestListner requestListner) {
        MyLog.d(TAG, "constructor ");

        mAlertManager=new AlertManager(contextActivity);
        this.mUrlWeather = urlWeather; // weather
        this.mUrlForcast = urlForcast; // weather
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
            MyLog.d(TAG, mUrlWeather);
            jsonObjectWeather = mRequestManager.connectToOpenWeatherServer(mUrlWeather);
            mJsonParser.parseWeather(jsonObjectWeather);
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
        try {// forecast
            MyLog.d(TAG, mUrlForcast);
            jsonObjectForecast = mRequestManager.connectToOpenWeatherServer(mUrlForcast);
            mJsonParser.parsForcast(jsonObjectForecast);
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
                editor.putString("urlWeather", mUrlWeather);
                editor.putString("jsonObjectWeather", String.valueOf(jsonObjectWeather));
                editor.putString("urlForecast", mUrlWeather);
                editor.putString("jsonObjectForecast", String.valueOf(jsonObjectForecast));
                editor.apply();
                requestListner.onResponse();

        }
        contextActivity.loadAnimationStop();
    }




}

