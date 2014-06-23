package kegsay.addm;

import org.json.JSONObject;

import android.content.Context;

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
    /** Value: Enum of {@link NetworkType}. */
    private static final String KEY_NETWORK_TYPE = "net_type";
    /** Value: String */
    private static final String KEY_NETWORK_SSID = "net_ssid";
    
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
    
    // SD Card info
    /** Value: Enum of {@link SdCardState}. */
    private static final String KEY_EXT_SD_CARD_STATE = "ext_sd_card_state";
    
    // Time info
    /** Value: String */
    private static final String KEY_TIMEZONE = "timezone";
    /** Value: String */
    private static final String KEY_WALL_CLOCK = "wall_clock_time";
    /** Value: Long */
    private static final String KEY_UPTIME = "uptime";
    
    
    /** Values for {@link DeviceInfo#KEY_NETWORK_TYPE} */
    public enum NetworkType {
        UNKNOWN,
        WIFI,
        MOBILE
    }
    
    /** Values for {@link DeviceInfo#KEY_SIM_STATE} */
    public enum SimState {
        UNKNOWN
    }
    
    /** Values for {@link DeviceInfo#KEY_BATTERY_STATE} */
    public enum BatteryState {
        UNKNOWN
    }
    
    /** Values for {@link DeviceInfo#KEY_EXT_SD_CARD_STATE} */
    public enum SdCardState {
        UNKNOWN
    }

    private Context mContext;
    private JSONObject mJson;
    
    public DeviceInfo(Context context) {
        mContext = context;
        resetInfo();
    }
    
    /**
     * Clears any previous device info state.
     */
    public void resetInfo() {
        mJson = new JSONObject();
    }
    
    /**
     * Collects information on the device on a best-effort basis.
     */
    public void collectInfo() {
        // since it's best effort, we discard any failures when collecting, making this pretty simple.
        addDeviceBuildInfo(mJson);
        addNetworkInfo(mJson);
        addSimInfo(mJson);
        addBatteryInfo(mJson);
        addSdCardInfo(mJson);
        addTimeInfo(mJson);
    }
    
    public JSONObject getInfo() {
        return mJson;
    }
    
    /**
     * Adds device build model/manufacturer and OS version to the provided JSON.
     * @param json The JSON object to insert into. Clobbers existing keys.
     */
    private void addDeviceBuildInfo(JSONObject json) {
        
    }
    
    /**
     * Adds wifi network information to the provided JSON.
     * @param json The JSON object to insert into. Clobbers existing keys.
     */
    private void addNetworkInfo(JSONObject json) {
        
    }
    
    /**
     * Adds SIM card info to the provided JSON.
     * @param json The JSON object to insert into. Clobbers existing keys.
     */
    private void addSimInfo(JSONObject json) {
        
    }
    
    /**
     * Adds battery information to the provided JSON.
     * @param json The JSON object to insert into. Clobbers existing keys.
     */
    private void addBatteryInfo(JSONObject json) {
        
    }
    
    /**
     * Adds external SD card information to the provided JSON.
     * @param json The JSON object to insert into. Clobbers existing keys.
     */
    private void addSdCardInfo(JSONObject json) {
        
    }
    
    /**
     * Adds time information (e.g. wall clock time, time zone) to the provided JSON.
     * @param json The JSON object to insert into. Clobbers existing keys.
     */
    private void addTimeInfo(JSONObject json) {
        
    }
}
