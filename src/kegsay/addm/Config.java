package kegsay.addm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Basic config class to persist settings done in the app.
 */
public class Config {
    public static final long DEFAULT_UPDATE_RATE_MINS = 60 * 12; // every 12 hours
    public static final String DEFAULT_URL = "http://localhost:8080/addm";
    
    private static final String CONFIG_PREFS = "config_prefs";
    private static final String KEY_UPDATE_RATE_MINS = "kegsay.addm.Config.KEY_UPDATE_RATE_MINS";
    private static final String KEY_URL = "kegsay.addm.Config.KEY_URL";
    private static final String KEY_LAST_POKE_TIME = "kegsay.addm.Config.KEY_LAST_POKE_TIME";
    
    private Context mContext;
    private SharedPreferences mPrefs;

    public Config(Context appContext) {
        mContext = appContext.getApplicationContext(); // just in case an activity is passed, so we don't leak
        mPrefs = mContext.getSharedPreferences(CONFIG_PREFS, Context.MODE_PRIVATE);
    }
    
    /**
     * Set the update frequency for this device.
     * @param mins The update rate in mins
     */
    public void setUpdateRate(long mins) {
        mPrefs.edit().putLong(KEY_UPDATE_RATE_MINS, mins).commit();
    }
    
    /**
     * Get the update frequency for this device.
     * @return The update rate in mins.
     */
    public long getUpdateRate() {
        return mPrefs.getLong(KEY_UPDATE_RATE_MINS, Config.DEFAULT_UPDATE_RATE_MINS);
    }
    
    /**
     * Set the url to poke when updating.
     * @param url The url in http[s] form.
     */
    public void setUrl(String url) {
        mPrefs.edit().putString(KEY_URL, url).commit();
    }
    
    /**
     * Get the http[s] url to poke when updating.
     * @return The url to poke.
     */
    public String getUrl() {
        return mPrefs.getString(KEY_URL, Config.DEFAULT_URL);
    }
    
    public long getLastPokeTime() {
        return mPrefs.getLong(KEY_LAST_POKE_TIME, -1L);
    }
    
    public void setLastPokeTime(long time) {
        mPrefs.edit().putLong(KEY_LAST_POKE_TIME, time).commit();
    }
}
