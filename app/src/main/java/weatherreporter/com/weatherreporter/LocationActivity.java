package weatherreporter.com.weatherreporter;

import weatherreporter.dataclasses.Data;
import weatherreporter.dataclasses.MyLog;
import weatherreporter.dataclasses.DayWeather;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationActivity extends Activity implements OnClickListener {
	String greg = "locationActivity";

	Activity locationActivity = this;
	private AutoCompleteTextView tvEnterSity;
	private EditText placeLatitude;
	private EditText placeLongitude;

	public void onCreate(Bundle savedInstanceState) {
		MyLog.d(greg, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		tvEnterSity = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);
		String[] cities = getResources().getStringArray(R.array.city);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, cities);
		tvEnterSity.setAdapter(adapter);

		Button btnSearchByCity = (Button) findViewById(R.id.btnSearchByCity);
		btnSearchByCity.setOnClickListener(this);

        placeLatitude = (EditText) findViewById(R.id.placeLatitude);
        placeLongitude = (EditText) findViewById(R.id.placeLongitude);



		Button btnSearchByCrd = (Button) findViewById(R.id.btnSearchByCrd);
		btnSearchByCrd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LocationActivity.this,HomeActivity.class);
		switch (v.getId()) {
		case R.id.btnSearchByCity:
			String str = tvEnterSity.getText().toString();
			MyLog.d(greg, "read city " + str);
			intent.putExtra("selectedCity", str);
			setResult(1, intent);
			break;
		case R.id.btnSearchByCrd:
			String lat = this.placeLatitude.getText().toString();
			String lon = this.placeLongitude.getText().toString();
			MyLog.d(greg, "read lat " + lat + "lon " + lon);
			intent.putExtra("lat", lat);
			intent.putExtra("lon", lon);
			setResult(2, intent);
			break;
		}
		finish();
	}
}
