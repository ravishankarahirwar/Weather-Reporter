package weatherreporter.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weatherreporter.com.weatherreporter.HomeActivity;
import weatherreporter.com.weatherreporter.R;
import weatherreporter.dataclasses.Main;
import weatherreporter.dataclasses.MyLog;

/**
 * Created by Ravishankar on 1/12/2015.
 */
public class JsonParser {
    private static final String TAG = "JsonParser";
    Context mContext;

    public JsonParser(Context context) {
        this.mContext = context;
    }



    public void parseWeather(JSONObject weatherJson) throws JSONException {
        MyLog.d(TAG, "try pars jsonDay: " + weatherJson);
        if (weatherJson.has("main")) {
            MyLog.d(TAG, "try pars weather start");
            parsingTodayWeather(weatherJson);
            return;
        }
        throw new JSONException("server error");
    }

    public void parsingTodayWeather(JSONObject json) throws JSONException {
        MyLog.d(TAG, "start pars day weather " + json);

         JSONObject main = (JSONObject) json.get("main");

        JSONArray weather = (JSONArray) json.get("weather");
        JSONObject weather2 = (JSONObject) weather.get(0);
        JSONObject wind = (JSONObject) json.get("wind");
        JSONObject sys = (JSONObject) json.get("sys");

        if (String.valueOf(json.get("name")).length()>0){
            HomeActivity.mAllData.mAllWeatherData.setCity((String) json.get("name"));
        }else{
            HomeActivity.mAllData.mAllWeatherData.setCity(String.valueOf(sys.get("country")));
        }

        String strLastUpdateTime = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm")
                .format(new java.util.Date(json.getLong("dt") * 1000));

        HomeActivity.mAllData.mAllWeatherData.setLastUpdateTime(strLastUpdateTime);

        HomeActivity.mAllData.mAllWeatherData.mMain.setTemperature(temperatureEdit(main.getDouble("temp")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setPressure(String.valueOf(main.get("pressure")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setHumidity(String.valueOf(main.get("humidity")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setMinimumTemperature(temperatureEdit(main.getDouble("temp_min")));
        HomeActivity.mAllData.mAllWeatherData.mMain.setMaximumTemperature(temperatureEdit(main.getDouble("temp_max")));


        HomeActivity.mAllData.mAllWeatherData.mWind.setSpeed(String.valueOf(wind.get("speed")));
      //  HomeActivity.mAllData.mAllWeatherData.mWind.setGust(String.valueOf(wind.get("gust")));
        HomeActivity.mAllData.mAllWeatherData.mWind.setDeg(String.valueOf(wind.get("deg")));

        HomeActivity.mAllData.mAllWeatherData.mSys.setCountry(String.valueOf(sys.get("country")));
        HomeActivity.mAllData.mAllWeatherData.mSys.setMessage(String.valueOf(sys.get("message")));

        HomeActivity.mAllData.mAllWeatherData.mSys.setSunrise(convertMillesecondToTime(sys.getLong("sunrise")));
        HomeActivity.mAllData.mAllWeatherData.mSys.setSunset(convertMillesecondToTime(sys.getLong("sunset")));
        int imageId = ((Context) mContext).getResources().getIdentifier(
                "w_" + String.valueOf(weather2.get("icon")), "drawable",
                ((Context) mContext).getPackageName());
        HomeActivity.mAllData.mAllWeatherData.setIconId(imageId);


    }

    ;



    private String temperatureEdit(Double t) {
        int T = (int) Math.round(t - 273.15);
        String str = String.valueOf(T);
        if (T > 0)
            str = "+" + str;
        return str;
    }

    ;



    private String convertMillesecondToTime(long millisecond){
        String time = new java.text.SimpleDateFormat("HH:mm")
                .format(new java.util.Date(millisecond * 1000));
        return time;
    }
}
