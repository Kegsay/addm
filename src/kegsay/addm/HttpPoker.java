package kegsay.addm;

import java.net.URI;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.util.Log;

/**
 * Pokes the given url with the given JSON data.
 */
public class HttpPoker {
    private static final String TAG = "HttpPoker";

    private String mUrl;
    
    public HttpPoker(String url) {
        mUrl = url;
    }
    
    public HttpResponse doPut(JSONObject json) {
        return doPut(json.toString());
    }
    
    public HttpResponse doPut(String json) {
        Log.d(TAG, mUrl+" : "+json);
        try{
            URI url = new URI(mUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut put= new HttpPut(url);
            put.setEntity(new StringEntity(json,"UTF-8"));
            put.setHeader("Content-type", "application/json");
            put.setHeader("charset", "utf-8");
            put.addHeader("Accept-Language", Locale.getDefault().toString().replace("_", "-"));
            httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "ADDM v0.1 "+android.os.Build.MODEL);
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 60 * 1000); // 1min
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5 * 60 * 1000); // 5min 
            HttpResponse response = httpClient.execute(put);
            return response;
        }
        catch(Exception e) {
            Log.e(TAG, "Failed to PUT: "+e);
        }
        return null;
    }
}
