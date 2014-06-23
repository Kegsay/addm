package kegsay.addm;

import java.util.Date;

import kegsay.addm.Addm.Callback;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/** 
 * Receiver class for when we next need to poke.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_POKE = "kegsay.addm.AlarmReceiver.ACTION_POKE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        // poke and re-schedule on boot and whenever we get poked.
        if (intent.getAction().equals(ACTION_POKE) || intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Addm addm = new Addm(context);
            addm.executeAsync(new Callback() {
    
                @Override
                public void onCompleted() {
                    // re-schedule the alarm
                    Config config = new Config(context);
                    long nextMins = config.getUpdateRate();
                    AlarmReceiver.scheduleIn(context, nextMins);
                }
                
            });
        }
    }
    
    public static void scheduleIn(Context context, long mins) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_POKE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        long rateMs = (mins * 60 * 1000);
        long atTimeMs = SystemClock.elapsedRealtime() + rateMs;
        Log.i("ADDM","Next poke in "+rateMs+" ms");
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, atTimeMs, alarmIntent);
    }

}
