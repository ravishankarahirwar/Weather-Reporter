package weatherreporter.dataclasses;

/**
 * Created by acer on 1/12/2015.
 */
public class AllWeatherData {
    public Main mMain;
    public Sys mSys;
    public Weather mWeather;
    public Wind mWind;
    private String city;
    private String lastUpdateTime;
    private int iconId;

    public AllWeatherData() {
        mMain = new Main();
        mSys = new Sys();
        mWeather = new Weather();
        mWind = new Wind();
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
