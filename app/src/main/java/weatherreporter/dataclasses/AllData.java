package weatherreporter.dataclasses;

/**
 * Created by Ravi on 1/12/2015.
 */
public class AllData {
    public AllWeatherData mAllWeatherData;
    public AllForecastData mAllForecastData;

    public AllData() {
        mAllWeatherData = new AllWeatherData();
        mAllForecastData = new AllForecastData();
    }
}
