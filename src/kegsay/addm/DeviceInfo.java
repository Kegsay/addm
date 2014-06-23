package kegsay.addm;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * This class does most of the legwork to get information about the device and converts it
 * into JSON.
 */
public class DeviceInfo {
    
    // Device build info
    /** Value: String. */
    private static final String KEY_OS_VERSION = "os_version";
    /** Value: String. */
    private static final String KEY_MODEL = "build_model";
    /** Value: String. */
    private static final String KEY_MANUFACTURER = "build_manufacturer";
    
    // Network info
    /** Value: String. */
    private static final String KEY_NETWORK_TYPE = "net_type";
    /** Value: String. */
    private static final String KEY_NETWORK_SSID = "net_ssid";
    /** Value: String. */
    private static final String KEY_NETWORK_SUBTYPE = "net_subtype";
    
    // SIM info
    /** Value: Enum of {@link SimState}. */
    private static final String KEY_SIM_STATE = "sim_state";
    /** Value: String */
    private static final String KEY_SIM_NUMBER = "sim_number";
    
    // Battery info
    /** Value: Integer. */
    private static final String KEY_BATTERY_PERCENT = "battery_percent";
    /** Value: Enum of {@link BatteryState}. */
    private static final String KEY_BATTERY_STATE = "battery_state";
    /** Value: 'AC' or 'USB' */
    private static final String KEY_BATTERY_CHARGE_SOURCE = "battery_charge_source";
    
    // SD Card info
    /** Value: Enum of {@link SdCardState}. */
    private static final String KEY_EXT_SD_CARD_STATE = "ext_sd_card_state";
    
    // Time info
    /** Value: String */
    private static final String KEY_TIMEZONE = "timezone";
    /** Value: Long */
    private static final String KEY_WALL_CLOCK = "wall_clock_time";
    /** Value: Long */
    private static final String KEY_UPTIME = "uptime";
    
    /** Values for {@link DeviceInfo#KEY_SIM_STATE} */
    public enum SimState {
        UNKNOWN(TelephonyManager.SIM_STATE_UNKNOWN),
        ABSENT(TelephonyManager.SIM_STATE_ABSENT),
        NETWORK_LOCKED(TelephonyManager.SIM_STATE_NETWORK_LOCKED),
        PIN_REQUIRED(TelephonyManager.SIM_STATE_PIN_REQUIRED),
        PUK_REQUIRED(TelephonyManager.SIM_STATE_PUK_REQUIRED),
        READY(TelephonyManager.SIM_STATE_READY);
        
        private int mState;
        
        SimState(int simState) {
            mState = simState;
        }
        
        public int getAndroidSimState() {
            return mState;
        }
    }
    
    /** Values for {@link DeviceInfo#KEY_BATTERY_STATE} */
    public enum BatteryState {
        UNKNOWN(BatteryManager.BATTERY_STATUS_UNKNOWN),
        CHARGING(BatteryManager.BATTERY_STATUS_CHARGING),
        DISCHARGING(BatteryManager.BATTERY_STATUS_DISCHARGING),
        FULL(BatteryManager.BATTERY_STATUS_FULL),
        NOT_CHARGING(BatteryManager.BATTERY_STATUS_NOT_CHARGING);
        
        private int mState;
        
        BatteryState(int state) {
            mState = state;
        }
        
        public int getAndroidBatteryState() {
            return mState;
        }
    }
    
    /** Values for {@link DeviceInfo#KEY_EXT_SD_CARD_STATE} */
    public enum SdCardState {
        UNKNOWN
    }

    private Context mContext;
    private Map<String, Object> mMap;
    
    public DeviceInfo(Context context) {
        mContext = context;
        resetInfo();
    }
    
    /**
     * Clears any previous device info state.
     */
    public void resetInfo() {
        mMap = new HashMap<String, Object>();
    }
    
    /**
     * Collects information on the device on a best-effort basis.
     */
    public void collectInfo() {
        // since it's best effort, we discard any failures when collecting, making this pretty simple.
        addDeviceBuildInfo(mMap);
        addNetworkInfo(mMap);
        addSimInfo(mMap);
        addBatteryInfo(mMap);
        addSdCardInfo(mMap);
        addTimeInfo(mMap);
    }
    
    public JSONObject getInfo() {
        return new JSONObject(mMap);
    }
    
    /**
     * Adds device build model/manufacturer and OS version to the provided JSON.
     * @param map The map to insert into. Clobbers existing keys.
     */
    private void addDeviceBuildInfo(Map<String, Object> map) {
        map.put(KEY_MANUFACTURER, android.os.Build.MANUFACTURER);
        map.put(KEY_MODEL, android.os.Build.MODEL);
        map.put(KEY_OS_VERSION, android.os.Build.VERSION.RELEASE);
    }
    
    /**
     * Adds wifi network information to the provided JSON.
     * @param map The map to insert into. Clobbers existing keys.
     */
    private void addNetworkInfo(Map<String, Object> map) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            String ssid = wifiInfo.getSSID();
            if (ssid != null) {
                map.put(KEY_NETWORK_SSID, ssid);
            }
        }
        
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            map.put(KEY_NETWORK_TYPE, activeNetwork.getTypeName());
            map.put(KEY_NETWORK_SUBTYPE, activeNetwork.getSubtypeName());
        }
        
    }
    
    /**
     * Adds SIM card info to the provided JSON.
     * @param map The map to insert into. Clobbers existing keys.
     */
    private void addSimInfo(Map<String, Object> map) {
        TelephonyManager telephony = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        
        int androidSimState = telephony.getSimState();
        for (SimState simState : SimState.values()) {
            if (simState.getAndroidSimState() == androidSimState) {
                map.put(KEY_SIM_STATE, simState.name());
                break;
            }
        }
        
        String number = telephony.getLine1Number();
        if (number != null) {
            map.put(KEY_SIM_NUMBER, number);
        }
        
    }
    
    /**
     * Adds battery information to the provided JSON.
     * @param map The map to insert into. Clobbers existing keys.
     */
    private void addBatteryInfo(Map<String, Object> map) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);
        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            for (BatteryState state : BatteryState.values()) {
                if (state.getAndroidBatteryState() == status) {
                    map.put(KEY_BATTERY_STATE, state.name());
                    break;
                }
            }
            
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            switch (chargePlug) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                map.put(KEY_BATTERY_CHARGE_SOURCE, "AC");
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                map.put(KEY_BATTERY_CHARGE_SOURCE, "USB");
                break;
            }
            
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = 100 * (level / (float)scale);
            if (batteryPct >= 0.0f && batteryPct <= 100.0f) {
                map.put(KEY_BATTERY_PERCENT, (int)batteryPct);
            }
        }
    }
    
    /**
     * Adds external SD card information to the provided JSON.
     * @param map The map to insert into. Clobbers existing keys.
     */
    private void addSdCardInfo(Map<String, Object> map) {
        
    }
    
    /**
     * Adds time information (e.g. wall clock time, time zone) to the provided JSON.
     * @param map The map to insert into. Clobbers existing keys.
     */
    private void addTimeInfo(Map<String, Object> map) {
        map.put(KEY_UPTIME, SystemClock.elapsedRealtime());
        map.put(KEY_WALL_CLOCK, System.currentTimeMillis());
    }
}
