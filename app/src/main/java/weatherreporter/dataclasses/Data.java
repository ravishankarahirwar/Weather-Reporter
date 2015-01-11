package weatherreporter.dataclasses;

public class Data {
	String greg = "dataClass";

	private DayWeather nowWeather;
	private DayWeather[] forecast;


	public final String strWeather = "http://api.openweathermap.org/data/2.5/weather?";
	public final String strForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?";
	public String urlStrDay;
	public String urlStrForecast;
	public String title;

	public DayWeather getNowWeather() {
		return nowWeather;
	}

	public void setNowWeather(DayWeather nowWeather) {
		MyLog.d(greg, "setWeather");
		this.nowWeather = nowWeather;
	}

	public DayWeather[] getForecast() {
		return forecast;
	}

	public void setForecast(DayWeather forecast[]) {
		MyLog.d(greg, "setForecast");
		this.forecast = forecast;
	}
}
