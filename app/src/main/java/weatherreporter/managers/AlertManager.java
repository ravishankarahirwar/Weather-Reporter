package weatherreporter.managers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ravi on 1/11/2015.
 */
public class AlertManager {
    private Context mContext;
    public AlertManager(Context context) {
        this.mContext=context;
    }

    public void showAlert(String alertMessage){
        Toast.makeText(mContext,alertMessage,Toast.LENGTH_SHORT).show();
    }
    public void showAlert(int alertMessageId){
        Toast.makeText(mContext,mContext.getString(alertMessageId),Toast.LENGTH_SHORT).show();
    }
}
