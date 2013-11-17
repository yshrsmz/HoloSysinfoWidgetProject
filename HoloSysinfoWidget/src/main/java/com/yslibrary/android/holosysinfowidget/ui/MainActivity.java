package com.yslibrary.android.holosysinfowidget.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yslibrary.android.holosysinfowidget.Consts;
import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.dao.InternalStorage;
import com.yslibrary.android.holosysinfowidget.dao.Ram;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static NotificationManager nm;

    private static final String batteryIconUri = "@drawable/ic_battery_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendBroadcast(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Log.v(TAG, "onCreate");
    }

    @Override
    protected void onRestart() {
        super.onResume();

        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.v(TAG, "onResume");

        updateMemoryInfo();
        updateStorageInfo();

        Log.d(TAG, "send intent to update widget");
        Intent widgetUpdate = new Intent(Consts.SYSINFO_UPDATE);
        sendBroadcast(widgetUpdate);

        // set battery info intent
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(null, intentFilter);
        registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    private void updateMemoryInfo() {
        // get memory info
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        Ram ram = new Ram(activityManager);

        int availRamColor = Color.GREEN;

        long totalRam = ram.getTotalMem();
        long availableRam = ram.getAvailableMem();

        int iRamPercentage = ram.getUsedPercentage();

        // total memory
        Log.v(TAG, "memoryInfo.totalMem[MB]:" + (int)(totalRam / 1024 / 1024));
        TextView tvTotalRam = (TextView)findViewById(R.id.var_total_ram);
        tvTotalRam.setText(ram.getTotalMemWithUnit());

        // available memory
        Log.v(TAG, "memoryInfo.availMem[MB]: " + (int)(availableRam / 1024 / 1024));
        TextView tvAvailableRam = (TextView)findViewById(R.id.var_available_ram);
        tvAvailableRam.setText(ram.getAvailableMemWithUnit());

        if (ram.isLowMemory()) {
            availRamColor = Color.RED;
        }
        tvAvailableRam.setTextColor(availRamColor);

        // progress bar for ram
        ProgressBar pbRam = (ProgressBar)findViewById(R.id.var_progress_ram);
        pbRam.setProgress(iRamPercentage);

        // percentage for ram
        TextView tvRamPercentage = (TextView)findViewById(R.id.var_ram_percentage);
        tvRamPercentage.setText(Integer.toString(iRamPercentage) + "%");

        // low memory
        Log.v(TAG, "memoryInfo.threshold[MB]: " + (int)(ram.getThreshold() / 1024 / 1024));

        // is low memory
        Log.v(TAG, "memoryInfo.lowMemory: " + ram.isLowMemory());
    }

    private void updateStorageInfo() {
        InternalStorage internalStorage = new InternalStorage();
        int availStorageColor = Color.GREEN;

        long totalStorage = internalStorage.getTotalMem();
        long availableStorage = internalStorage.getAvailableMem();

        int iStoragePercentage = internalStorage.getUsedPercentage();

        // total storage
        Log.v(TAG, "internalStorage.totalMem[MB]:" + (int)(totalStorage / 1024 / 1024));
        TextView tvTotalStorage = (TextView)findViewById(R.id.var_total_storage);
        tvTotalStorage.setText(internalStorage.getTotalMemWithUnit());

        // available storage
        Log.v(TAG, "internalStorage.availMem[MB]:" + (int)(availableStorage / 1024 / 1024));
        TextView tvAvailableStorage = (TextView)findViewById(R.id.var_available_storage);
        tvAvailableStorage.setText(internalStorage.getAvailableMemWithUnit());

        if (internalStorage.isLowMemory()) {
            availStorageColor = Color.RED;
        }

        tvAvailableStorage.setTextColor(availStorageColor);

        // progress bar for internalStorage
        ProgressBar pbInternalStorage = (ProgressBar)findViewById(R.id.var_progress_storage);
        pbInternalStorage.setProgress(iStoragePercentage);

        // percentage for internalStorage
        TextView tvStoragePercentage = (TextView)findViewById(R.id.var_storage_percentage);
        tvStoragePercentage.setText(Integer.toString(iStoragePercentage) + "%");
    }

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

                setNotification(context, title, subText, percentage);
            }
        }
    };

}
