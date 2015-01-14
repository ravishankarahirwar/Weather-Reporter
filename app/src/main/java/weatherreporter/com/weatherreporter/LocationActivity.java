package weatherreporter.com.weatherreporter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import weatherreporter.dataclasses.MyLog;

public class LocationActivity extends Activity implements OnClickListener {
    String greg = "locationActivity";

    Activity locationActivity = this;
    private AutoCompleteTextView citySelecter;
    public void onCreate(Bundle savedInstanceState) {
        MyLog.d(greg, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        citySelecter = (AutoCompleteTextView) findViewById(R.id.autoCompleteCity);
        String[] cities = getResources().getStringArray(R.array.city);

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
                String str = citySelecter.getText().toString();
                MyLog.d(greg, "Selected City" + str);
                intent.putExtra("selectedCity", str);
                setResult(1, intent);
                break;

        }
        finish();
    }
}
