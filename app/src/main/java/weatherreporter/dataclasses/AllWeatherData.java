package weatherreporter.dataclasses;

/**
 * Created by acer on 1/12/2015.
 */
public class AllWeatherData {
    public Main mMain;
    public Sys mSys;
    public Weather mWeather;
    public Wind mWind;
    public AllWeatherData(){
        mMain=new Main();
        mSys=new Sys();
        mWeather=new Weather();
        mWind=new Wind();
    }
}