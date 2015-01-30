package weatherreporter.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weatherreporter.com.weatherreporter.HomeActivity;
import weatherreporter.com.weatherreporter.R;
import weatherreporter.dataclasses.ForecastData;
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

    public void parsForcast(JSONObject jsonObjectForecast) throws JSONException {
        if (jsonObjectForecast.has("cnt")) {
            MyLog.d(TAG, "try pars forecast start");
            int d = jsonObjectForecast.getInt("cnt");
            HomeActivity.mAllData.mAllForecastData.getList().clear();
            JSONArray list = jsonObjectForecast.getJSONArray("list");
            for (int i = 0; i <= d - 1; i++) {
                JSONObject dayForecast = (JSONObject) list.get(i);
                MyLog.d(TAG, "forecast for (i=" + i + ")");

                HomeActivity.mAllData.mAllForecastData.list.add(parsForecast(dayForecast,mContext));
            }

            return;
        }
        throw new JSONException("server error");
    }

    public ForecastData parsForecast(JSONObject json, Context context)
            throws JSONException {
        ForecastData tForecastData=new ForecastData();
        MyLog.d(TAG , "pars day forecast " + json);


        JSONArray weather = (JSONArray) json.get("weather");
        JSONObject weather2 = (JSONObject) weather.get(0);
        JSONObject temp = (JSONObject) json.get("temp");

        tForecastData.dt = new java.text.SimpleDateFormat("dd.MM.yy")
                .format(new java.util.Date(json.getLong("dt") * 1000));

        tForecastData.setMinimumTemperature(temperatureEdit(temp.getDouble("min")));
        tForecastData.setMaximumTemperature(temperatureEdit(temp.getDouble("max")));
        tForecastData.day = temperatureEdit(temp.getDouble("day"));
        tForecastData.night = temperatureEdit(temp.getDouble("night"));
        tForecastData.morn = temperatureEdit(temp.getDouble("morn"));
        tForecastData.eve = temperatureEdit(temp.getDouble("eve"));

        tForecastData.setPressure(context.getString(R.string.pressure)
                + String.valueOf(json.get("pressure"))
                + context.getString(R.string.pressureUnit));
        tForecastData.setHumidity(context.getString(R.string.humidity)
                + String.valueOf(json.get("humidity"))
                + context.getString(R.string.percent));
        tForecastData.windSpeed = context.getString(R.string.wind) + " "
                + context.getString(windDir(json.getInt("deg"))) + " "
                + String.valueOf(json.getInt("speed")) + " "
                +  context.getString(R.string.speedUnit);
        tForecastData.imageId = ((Context) context).getResources().getIdentifier(
                "w_" + String.valueOf(weather2.get("icon")), "drawable",
                ((Context) context).getPackageName());

       tForecastData.setDescription(String.valueOf(weather2.get("description")));

return tForecastData;
    }




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
        return (R.string.app_name);
    }
}
