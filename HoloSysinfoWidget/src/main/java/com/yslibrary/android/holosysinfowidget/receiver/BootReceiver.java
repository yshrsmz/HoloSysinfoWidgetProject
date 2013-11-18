package com.yslibrary.android.holosysinfowidget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.service.BatteryNotificationService;

/**
 * Created by A12897 on 13/11/18.
 */
public class BootReceiver extends BroadcastReceiver {
    private final static String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "#onReceive");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean shouldServiceActivate = pref.getBoolean(context.getResources().getString(R.string.pref_key_activate_notification), true);

        if (shouldServiceActivate) {
            // start battery notification service
            context.startService(new Intent(context, BatteryNotificationService.class));
        }
    }
}
