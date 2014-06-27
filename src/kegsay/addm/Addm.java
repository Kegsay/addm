package kegsay.addm;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.content.Context;

/**
 * Manages the collection of info and the submission to the chosen url.
 * Brings everything together.
 */
public class Addm {
    public static final String ADDM_PATH = "/addm/device/";
    public static final long ERROR_SERVER_HIT_FAILED = -2L;
    
    public interface Callback {
        public void onCompleted();
    }

    private Context mContext;
    
    public Addm(Context context) {
        mContext = context;
    }
    
    public void executeAsync() {
        executeAsync(null);
    }
    
    public void executeAsync(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                execute();
                if (callback != null) {
                    callback.onCompleted();
                }
            }
        }).start();
    }
    
    public void execute() {
        DeviceInfo info = new DeviceInfo(mContext);
        info.collectInfo();
        JSONObject json = info.getInfo();
        
        Config config = new Config(mContext);
        HttpPoker poker = new HttpPoker(config.getUrl() + ADDM_PATH + info.getDeviceId());
        HttpResponse response = poker.doPut(json);
        boolean success = response != null && response.getStatusLine().getStatusCode() == 200;
        config.setLastPokeTime(success ? System.currentTimeMillis() : ERROR_SERVER_HIT_FAILED);
    }
    
    
}
