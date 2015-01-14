package weatherreporter.dataclasses;

import java.util.ArrayList;

/**
 * Created by Ravi on 1/12/2015.
 */
public class AllForecastData {
    public String dt;
    public ArrayList<AllWeatherData> list;

    public AllForecastData() {
        list = new ArrayList<AllWeatherData>();
    }

    /**
     * @param date :
     */

    public AllForecastData(String date, ArrayList<AllWeatherData> list) {
        this.dt = dt;
        this.list = list;
    }

    public String getDt() {
        return dt;
    }

    public void setDate(String date) {
        this.dt = dt;
    }

    public ArrayList<AllWeatherData> getList() {
        return list;
    }

    public void setList(ArrayList<AllWeatherData> list) {
        this.list = list;
    }
}
