package weatherreporter.com.weatherreporter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import weatherreporter.dataclasses.DayWeather;
import weatherreporter.dataclasses.MyLog;

public class FragmentForecast extends Fragment {

    public View v;
    String greg = "FragmentForecast";
    ProgressBar ProgressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLog.d(greg, "start");
        v = inflater.inflate(R.layout.fragment_forecast, null);
        ProgressBar = (ProgressBar) v.findViewById(R.id.ProgressBarFF);

        if (null != FirstActivity.newData.getForecast()) {
            MyLog.d(greg, "get data");
            DayWeather[] dw = FirstActivity.newData.getForecast();
            ForecastAdapter adapter = new ForecastAdapter(getActivity(), dw);
            ExpandableListView elvDay = (ExpandableListView) v
                    .findViewById(R.id.ELday);
            elvDay.setAdapter(adapter);
            ProgressBar.setVisibility(View.GONE);
        }

        return v;
    }

}
