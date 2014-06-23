package kegsay.addm;

import org.json.JSONObject;

import android.content.Context;

/**
 * Manages the collection of info and the submission to the chosen url.
 * Brings everything together.
 */
public class Addm {
    
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
        HttpPoker poker = new HttpPoker(config.getUrl());
        poker.doPut(json);
        
        config.setLastPokeTime(System.currentTimeMillis());
    }
    
    
}
