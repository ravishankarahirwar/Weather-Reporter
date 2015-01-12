package weatherreporter.dataclasses;

/**
 * Created by acer on 1/12/2015.
 */


        import java.io.IOException;

        import org.apache.http.ParseException;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.util.EntityUtils;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import android.content.SharedPreferences.Editor;
        import android.os.AsyncTask;
        import android.widget.Toast;

        import weatherreporter.com.weatherreporter.HomeActivity;

public class DataFetchingTask extends AsyncTask<Void, Integer, Integer> {
    private String weatherUrl;
    private String forecastUrl;
    private String title;
    private Data data;
    AllData allData;
    private HomeActivity contextActivity;
    private String greg = "UrlTask";
    private DayWeather nowWeather;
    private DayWeather[] forecastWeather;
    JSONObject weatherJson;
    JSONObject forecastJson;

    public DataFetchingTask(HomeActivity contextActivity, AllData allData, String weatherUrl,
                   String forecastUrl, String title) {
        MyLog.d(greg, "constructor ");
        this.allData = allData;
        this.title = title;
        this.weatherUrl = weatherUrl; // weather
        this.forecastUrl = forecastUrl; // forecast
        this.contextActivity = contextActivity;

    }

    @Override
    protected void onPreExecute() {
        //firstActivity.loadAnimationStart();
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... arg0) {
        if (isCancelled())
            return null;

        try {// day
            MyLog.d(greg, weatherUrl);
            weatherJson = connectToOpenWeatherServer(weatherUrl);
            parseWeather(weatherJson);
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
            MyLog.d(greg, forecastUrl);
            forecastJson = connectToOpenWeatherServer(forecastUrl);
            parsForecast(forecastJson);
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
            // if data was not received
            case 2:
                Toast.makeText(contextActivity,"No data found",
                        Toast.LENGTH_LONG).show();
                break;
            case 1:
                Toast.makeText(contextActivity,
                        "No data found",
                        Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(contextActivity,
                        "error",
                        Toast.LENGTH_LONG).show();
                break;

            // if data has been successfully received
            case 0:
                // save query string and title to refresh data next time
                data.urlStrDay = weatherUrl;
                data.urlStrForecast = forecastUrl;
                data.title = title;
                data.setForecast(forecastWeather);
                data.setNowWeather(nowWeather);

                Editor editor = contextActivity.mSettings.edit();
                editor.putString("title", title);
                editor.putString("urlStrDay", weatherUrl);
                editor.putString("urlStrForecast", forecastUrl);
                editor.putString("joDay", String.valueOf(weatherJson));
                editor.putString("joForecast", String.valueOf(forecastJson));
                editor.apply();

                // refresh visible activity
                if (contextActivity.visibleOnScreen)
                    contextActivity.afterUrlTask();
                else
                    contextActivity.showNewData = true;
                break;
        }
        //firstActivity.loadAnimationStop();
    }



    JSONObject connectToOpenWeatherServer(String apiUrl) throws ParseException,
            ClientProtocolException, JSONException, IOException {
        JSONObject jsonObject = new JSONObject(
                EntityUtils.toString(new DefaultHttpClient().execute(
                        new HttpGet(apiUrl)).getEntity()));
        return jsonObject;
    }



}

