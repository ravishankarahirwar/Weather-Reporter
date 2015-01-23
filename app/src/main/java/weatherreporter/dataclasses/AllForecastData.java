package weatherreporter.dataclasses;

import java.util.ArrayList;

/**
 * Created by Ravi on 1/12/2015.
 */
public class AllForecastData {

    public ArrayList<ForecastData> list;

    public AllForecastData() {
        list = new ArrayList<ForecastData>();
    }



    public ArrayList<ForecastData> getList() {
        return list;
    }

    public void setList(ArrayList<ForecastData> list) {
        this.list = list;
    }
}
