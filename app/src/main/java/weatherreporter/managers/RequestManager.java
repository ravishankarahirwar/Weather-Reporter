package weatherreporter.managers;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Ravi on 1/11/2015.
 */
public class RequestManager {

    public interface RequestListner {
        public void onResponse();
    }

    public JSONObject connectToOpenWeatherServer(String apiUrl) throws ParseException,
            ClientProtocolException, JSONException, IOException {
        HttpGet tempHttpGet=new HttpGet(apiUrl.replace(" ", "%20"));
        tempHttpGet.addHeader("x-api-key","4d3f0bf8d671ac54c3bcbdbe61e98ce5");
        JSONObject jsonObject = new JSONObject(
                EntityUtils.toString(new DefaultHttpClient().execute(
                        tempHttpGet).getEntity()));
        return jsonObject;
    }

}
