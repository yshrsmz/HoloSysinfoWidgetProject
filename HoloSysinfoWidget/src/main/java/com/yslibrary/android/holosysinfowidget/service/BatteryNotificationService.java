package com.yslibrary.android.holosysinfowidget.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.ui.MainActivity;

/**
 * Created by yshrsmz on 13/11/18.
 */
public class BatteryNotificationService extends Service {

    private final static String TAG = BatteryNotificationService.class.getSimpleName();

    private static NotificationManager nm;

    private static final String batteryIconUri = "@drawable/ic_battery_";

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "#onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "#onStartCommand");

        // set battery info intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(null, intentFilter);
        registerReceiver(broadcastReceiver, intentFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        nm = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(R.string.app_name);

        unregisterReceiver(broadcastReceiver);

        Log.d(TAG, "#onDestroy");
    }

    /**
     * create notification
     * @param context
     * @param title
     * @param subText
     * @param percentage
     */
    private static void setNotification(Context context, String title, String subText, int percentage) {
        nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification noti = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(context.getResources().getIdentifier(batteryIconUri + String.format("%1$03d", percentage), null, context.getPackageName()))
                .setTicker("start HoloSysinfoWidget...")
                .setContentTitle(title)
                .setContentText(subText)
                .setWhen(System.currentTimeMillis())
                .build();

        // permanent notification
        noti.flags = Notification.FLAG_ONGOING_EVENT;


        nm.notify(R.string.app_name, noti);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

                String title = "";
                String subText = "Status: ";
                int percentage = 0;

                Log.d(TAG, "Battery Scale: " + String.valueOf(intent.getIntExtra("scale", 0)));
                Log.d(TAG, "Battery Level: " + String.valueOf(intent.getIntExtra("level", 0)));

                percentage = (int)((double)(intent.getIntExtra("level", 0)) / (double)(intent.getIntExtra("scale", 0)) * 100);

                Log.d(TAG, "Battery percentage: " + String.valueOf(percentage));

                title = String.valueOf(percentage) + "%";

                // charging
                switch (intent.getIntExtra("status", 0)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        Log.d(TAG, "Battery status: CHARGING");
                        title += " Charging";
                        break;

                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        Log.d(TAG, "Battery status: DISCHARGING");
                        title += " Discharging";
                        break;

                    case BatteryManager.BATTERY_STATUS_FULL:
                        Log.d(TAG, "Battery status: FULL");
                        title += " Full";
                        break;

                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        Log.d(TAG, "Battery status: NOT CHARGING");
                        title += " Not Charging";
                        break;

                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        Log.d(TAG, "Battery status: UNKNOWN");
                        title += " Unknown";
                        break;
                }

                // plugged
                switch (intent.getIntExtra("plugged", 0)) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        Log.d(TAG, "Battery Plugged: AC");
                        title += "(AC Plugged)";
                        break;

                    case BatteryManager.BATTERY_PLUGGED_USB:
                        Log.d(TAG, "Battery Plugged: USB");
                        title += "(USB Plugged)";
                        break;
                }

                // battery health
                switch (intent.getIntExtra("health", 0)) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        Log.d(TAG, "Battery health: UNKNOWN");
                        subText += "Unknown";
                        break;

                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        Log.d(TAG, "Battery health: DEAD");
                        subText += "Dead";
                        break;

                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        Log.d(TAG, "Battery health: GOOD");
                        subText += "Good";
                        break;

                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        Log.d(TAG, "Battery health: OVERHEAT");
                        subText += "Overheat";
                        break;

                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        Log.d(TAG, "Battery health: OVER VOLTAGE");
                        subText += "Over Voltage";
                        break;

                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        Log.d(TAG, "Battery health: UNSPECIFIED FAILURE");
                        subText += "Unspecified Failure";
                        break;
                }

                Log.d(TAG, "Battery Present: " + String.valueOf(intent.getBooleanExtra("present", false)));

                Log.d(TAG, "Battery Voltage: " + String.valueOf(intent.getIntExtra("voltage", 0)));

                Log.d(TAG, "Battery Temperature: " + String.valueOf((float)(intent.getIntExtra("temperature", 0)) / 10));
                subText += ", " + String.valueOf((float)(intent.getIntExtra("temperature", 0)) / 10) + "\u2103";

                setNotification(context, title, subText, percentage);
            }
        }
    };
}
