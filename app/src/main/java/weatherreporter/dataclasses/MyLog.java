package weatherreporter.dataclasses;

import android.util.Log;

import weatherreporter.com.weatherreporter.BuildConfig;

public class MyLog {
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

}
