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
            // this.nowWeather = nowWeather;
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

        HomeActivity.mAllData.mAllWeatherData.setCity((String) json.get("name"));
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

      /*  description = context.getString(((Context) context).getResources().getIdentifier(
                "d" + String.valueOf(weather2.get("id")), "string",
                ((Context) context).getPackageName()));*/
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

    private String convertMillesecondToTime(long millisecond){
        String time = new java.text.SimpleDateFormat("HH:mm")
                .format(new java.util.Date(millisecond * 1000));
        return time;
    }
}
