/**
 * @author Ravishankar
 *
 *@date 15/01/2015
 */
package weatherreporter.errorhandling;

import weatherreporter.com.weatherreporter.HomeActivity;
import weatherreporter.com.weatherreporter.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class ShowExceptionActivity extends Activity {
	private static final String TAG="ShowExceptionActivity";
	private TextView mError;
	private Button mBtnDismiss;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.exception);

		mBtnDismiss = (Button) findViewById(R.id.btnDismiss);
		mBtnDismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();

			}
		});
	}
	
	@Override
	public void onBackPressed() {

		reset();
		super.onBackPressed();
	}
	
	private void reset() {
        /**Restarting project from HomeActivity*/
		startActivity(new Intent(ShowExceptionActivity.this, HomeActivity.class));
        finish();
	}
}
