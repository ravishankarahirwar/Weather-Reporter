/**
 * @author Ravishankar
 *
 *@date 04/09/2014
 */
package weatherreporter.errorhandling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.quopn.wallet.MainSplashScreen;
import com.quopn.wallet.R;

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
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
//		Log.v(TAG, "onBackPressed");
		reset();
		super.onBackPressed();
	}
	
	private void reset() {
		startActivity(new Intent(ShowExceptionActivity.this, MainSplashScreen.class));
//		Log.v(TAG, "Redirected to >> MainSplashScreen");
	}
}
