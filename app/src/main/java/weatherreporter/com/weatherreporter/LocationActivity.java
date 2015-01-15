package weatherreporter.com.weatherreporter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import weatherreporter.dataclasses.MyLog;
import weatherreporter.errorhandling.ExceptionHandler;
import weatherreporter.util.Constants;

public class LocationActivity extends Activity implements OnClickListener {
    private static final String TAG="LocationActivity";

    public SharedPreferences mSettings;
    private AutoCompleteTextView citySelecter;

    public void onCreate(Bundle savedInstanceState) {
        MyLog.d(TAG, "onCreate");
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mSettings = getSharedPreferences(HomeActivity.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        citySelecter = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);
        String[] cities = getResources().getStringArray(R.array.city);

        /** Location is mandatory if user came first time */
        if (!mSettings.contains("selectedCity")) {
            citySelecter.setHint(Html.fromHtml("<font color = #006064>" + getString(R.string.hint_city) + "</font><font color = #ff0000>" + "*" + "</font>"));
        }else{
            /** User want to change location */
            setTitle(R.string.changeCity);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, cities);
        citySelecter.setAdapter(adapter);

        Button btnSearchByCity = (Button) findViewById(R.id.btnSearchByCity);
        btnSearchByCity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
        switch (v.getId()) {
            case R.id.btnSearchByCity:
                String location = citySelecter.getText().toString();
                /** Validation checking of entered location */
                if(TextUtils.isEmpty(location)  ){
                    citySelecter.setError(getString(R.string.error_location_cant_empty));
                }else if(!location.matches(Constants.CITY_NAME_PATTERN)){
                    citySelecter.setError(getString(R.string.error_valid_location));
                }else {
                    /** Every thing is fine about entered location */
                    MyLog.d(TAG, "Selected City" + location);
                    intent.putExtra("selectedCity", location);
                    setResult(1, intent);
                    finish();
                }
                break;

        }

    }
}
