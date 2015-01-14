package weatherreporter.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;


/**
 * Created by Ravi on 1/14/2015.
 */
public class Validater {

    /**
     * @param context : Context of activity.
     * @Description : This method is use for checking internet connectivity of android device.
     * */
    public static boolean isInternetAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            //connection is avlilable
            return true;
        }else{
            //no connection
            return false;
//
        }

    }
    pubic boolean isPlriaceNameValid(String placeName){
        if(TextUtils.isEmpty(placeName)  ){

            return false;
        }else if(!placeName.matches(Constants.CITY_NAME_PATTERN)){

            return false;
        }
        else if( (placeName.startsWith(" ") || placeName.endsWith(" "))  ){
            mEditName.setText(name.trim());
            return;
        }
    }

}
