package weatherreporter.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weatherreporter.com.weatherreporter.HomeActivity;
import weatherreporter.com.weatherreporter.R;
import weatherreporter.dataclasses.DayWeather;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.dataclasses.Main;

/**
 * Created by Ravishankar on 1/12/2015.
 */
public class JsonParser {
    private static final String TAG="JsonParser";
    private Main mMain;
    Context mContext;
    public JsonParser(Context context) {

        this.mMain = mMain;
        this.mContext=context;
    }

    public void parsForecast(JSONObject weatherJson) throws JSONException {
        MyLog.d(TAG, "try pars jsonForecast: " + weatherJson);
        if (weatherJson.has("cnt")) {
            MyLog.d(TAG, "try pars forecast start");
            int d = weatherJson.getInt("cnt");
            DayWeather[] forecast = new DayWeather[d];
            JSONArray list = weatherJson.getJSONArray("list");
            for (int i = 0; i <= d - 1; i++) {
                JSONObject day = (JSONObject) list.get(i);
                MyLog.d("greg", "forecast for (i=" + i + ")");
                forecast[i] = new DayWeather();
                parsForecast(day);
            }
          //  forecastWeather = forecast;
            return;
        }
        throw new JSONException("server error");
    }

    public void parseWeather(JSONObject weatherJson) throws JSONException {
        MyLog.d(TAG, "try pars jsonDay: " + weatherJson);
        if (weatherJson.has("main")) {
            MyLog.d(TAG, "try pars nowweather start");
            DayWeather nowWeather = new DayWeather();
            parsingTodayWeather(weatherJson);
           // this.nowWeather = nowWeather;
            return;
        }
        throw new JSONException("server error");
    }

    public void parsingTodayWeather(JSONObject json) throws JSONException {
        MyLog.d(TAG , "start pars day weather " + json);

      //  cntxt = (Context) context;
        JSONObject main;

        main = (JSONObject) json.get("main");

        JSONArray weather = (JSONArray) json.get("weather");
        JSONObject weather2 = (JSONObject) weather.get(0);
        JSONObject wind = (JSONObject) json.get("wind");

      //  HomeActivity.mAllData.mAllWeatherData.mMain.city = (String) json.get("name");
        HomeActivity.mAllData.mAllWeatherData.mMain.setTemperature(temperatureEdit(main.getDouble("temp")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setPressure(String.valueOf(main.get("pressure")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setHumidity(String.valueOf(main.get("humidity")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setMinimumTemperature(String.valueOf(main.get("temp_min")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setMaximumTemperature(String.valueOf(main.get("temp_max")));


/*        imageId = ((Context) context).getResources().getIdentifier(
                "w_" + String.valueOf(weather2.get("icon")), "drawable",
                ((Context) context).getPackageName());

        description = context.getString(((Context) context).getResources().getIdentifier(
                "d" + String.valueOf(weather2.get("id")), "string",
                ((Context) context).getPackageName()));*/
    };

    public void parsForecast(JSONObject json, Context context)
            throws JSONException {
       /* MyLog.d(greg , "pars day forecast " + json);
        cntxt = (Context) context;

        JSONArray weather = (JSONArray) json.get("weather");
        JSONObject weather2 = (JSONObject) weather.get(0);
        JSONObject temp = (JSONObject) json.get("temp");

        date = new java.text.SimpleDateFormat("dd.MM.yyyy")
                .format(new java.util.Date(json.getLong("dt") * 1000));

        dayTemperature = temperatureEdit(temp.getDouble("day"));
        nightTemperature = temperatureEdit(temp.getDouble("night"));
        mornTemperature = temperatureEdit(temp.getDouble("morn"));
        eveTemperature = temperatureEdit(temp.getDouble("eve"));

        pressure = context.getString(R.string.pressure)
                + String.valueOf(json.get("pressure"))
                + context.getString(R.string.pressureUnit);
        humidity = context.getString(R.string.humidity)
                + String.valueOf(json.get("humidity"))
                + context.getString(R.string.percent);
        windSpeed = context.getString(R.string.wind) + " "
                + context.getString(windDir(json.getInt("deg"))) + " "
                + String.valueOf(json.getInt("speed")) + " "
                +  context.getString(R.string.speedUnit);
        imageId = ((Context) context).getResources().getIdentifier(
                "w_" + String.valueOf(weather2.get("icon")), "drawable",
                ((Context) context).getPackageName());

        description = context.getString(((Context) context).getResources().getIdentifier(
                "d" + String.valueOf(weather2.get("id")), "string",
                ((Context) context).getPackageName()));
*/
    };


    private String temperatureEdit(Double t) {
        int T = (int) Math.round(t - 273.15);
        String str = String.valueOf(T);
        if (T > 0)
            str = "+" + str;
        return str;
    };

    private int windDir(int i) {
        if (i <= 22 | i > 337)
            return (R.string.N);
        if (i > 22 && i <= 67)
            return (R.string.NE);
        if (i > 67 && i <= 112)
            return (R.string.E);
        if (i > 112 && i <= 157)
            return (R.string.SE);
        if (i > 157 && i <= 202)
            return (R.string.S);
        if (i > 202 && i <= 247)
            return (R.string.SW);
        if (i > 247 && i <= 292)
            return (R.string.W);
        if (i > 292 && i <= 337)
            return (R.string.NW);
        return (R.string.hello_world);
    }
}
