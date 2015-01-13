package weatherreporter.com.weatherreporter;

import weatherreporter.dataclasses.Data;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.dataclasses.DayWeather;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentNow extends Fragment {

	LinearLayout LL;
	TextView city;
	ImageView imWeatherIcon;
	TextView temperature;
	TextView descript;
	TextView windSpeed;
	TextView humidity;
	TextView pressure;
	ProgressBar ProgressBar;
	DayWeather dw;
	String greg = "FragmentNow";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		MyLog.d(greg, "FragmentNow start");
		final View v = inflater.inflate(R.layout.fragment_now, null);

		LL = (LinearLayout) v.findViewById(R.id.FNLinearL);
		LL.setVisibility(View.GONE);
		ProgressBar = (ProgressBar) v.findViewById(R.id.FNProgressBar);
		if (null!=FirstActivity.newData.getNowWeather()){
		
				MyLog.d(greg, "get data");
				dw = FirstActivity.newData.getNowWeather();

						MyLog.d(greg, "FragmentNow post.run setText");

						city = (TextView) v.findViewById(R.id.city);
						imWeatherIcon = (ImageView) v
								.findViewById(R.id.imWeatherIcon);
						temperature = (TextView) v
								.findViewById(R.id.temperature);
						descript = (TextView) v.findViewById(R.id.description);
						windSpeed = (TextView) v.findViewById(R.id.windSpeed);
						humidity = (TextView) v.findViewById(R.id.humidity);
						pressure = (TextView) v.findViewById(R.id.pressure);
						city.setText(dw.city);
						imWeatherIcon.setImageResource(dw.imageId);
						temperature.setText(dw.dayTemperature + "�C");
						descript.setText(dw.description);
						windSpeed.setText(dw.windSpeed);
						humidity.setText(dw.humidity);
						pressure.setText(dw.pressure);
						LL.setVisibility(View.VISIBLE);
						ProgressBar.setVisibility(View.GONE);

				}
		return v;
	}
}
