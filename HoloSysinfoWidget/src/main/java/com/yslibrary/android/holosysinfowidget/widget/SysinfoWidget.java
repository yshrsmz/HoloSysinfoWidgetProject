package com.yslibrary.android.holosysinfowidget.widget;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import com.yslibrary.android.holosysinfowidget.Consts;
import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.dao.InternalStorage;
import com.yslibrary.android.holosysinfowidget.dao.Ram;

import java.util.Calendar;

/**
 * Created by yshrsmz on 2013/11/09.
 */
public class SysinfoWidget extends AppWidgetProvider {
    public static final String TAG = SysinfoWidget.class.getSimpleName();

    private static final int UPDATE_INTERVAL = 60 * 1000;

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "#onEnabled");

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);

        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), UPDATE_INTERVAL, createSysinfoUpdateIntent(context));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "#onUpdate");
        Log.d(TAG, "appWidgetIds.length: " + appWidgetIds.length);
        Log.d(TAG, "appWidgetIds[0]: " + appWidgetIds[0]);

        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        Ram ram = new Ram(am);

        InternalStorage is = new InternalStorage();

        for (int appWidgetId : appWidgetIds) {
//            Intent intent = new Intent(context, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sysinfo_widget);

            // set ram info
            remoteViews.setTextViewText(R.id.widget_var_available_ram, ram.getAvailableMemWithUnit());
            remoteViews.setTextViewText(R.id.widget_var_total_ram, ram.getTotalMemWithUnit());

            // set internal storage info
            remoteViews.setTextViewText(R.id.widget_var_available_storage, is.getAvailableMemWithUnit());
            remoteViews.setTextViewText(R.id.widget_var_total_storage, ram.getTotalMemWithUnit());

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            updateAppWidget(context, appWidgetManager, appWidgetId, ram, is);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(TAG, "#onReceive");

        if (Consts.SYSINFO_UPDATE.equals(intent.getAction())) {
            Log.d(TAG, "Sysinfo update");

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

            Ram ram = new Ram((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE));
            InternalStorage is = new InternalStorage();

            for (int appWidgetId : ids) {
                updateAppWidget(context, appWidgetManager, appWidgetId, ram, is);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "#onDeleted");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "#onDisabled");

        // cancel AlarmManager
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createSysinfoUpdateIntent(context));
    }

    private PendingIntent createSysinfoUpdateIntent(Context context) {
        Intent intent = new Intent(Consts.SYSINFO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Ram ram, InternalStorage is) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sysinfo_widget);

        int ramColor = Color.GREEN;
        int internalStorageColor = Color.GREEN;

        // set ram info
        remoteViews.setTextViewText(R.id.widget_var_available_ram, ram.getAvailableMemWithUnit());

        if (ram.isLowMemory()) {
            ramColor = Color.RED;
        }
        remoteViews.setTextColor(R.id.widget_var_available_ram, ramColor);

        remoteViews.setTextViewText(R.id.widget_var_total_ram, ram.getTotalMemWithUnit());

        // set internal storage info
        remoteViews.setTextViewText(R.id.widget_var_available_storage, is.getAvailableMemWithUnit());
        remoteViews.setTextViewText(R.id.widget_var_total_storage, is.getTotalMemWithUnit());

        if (is.isLowMemory()) {
            internalStorageColor = Color.RED;
        }
        remoteViews.setTextColor(R.id.widget_var_available_storage, internalStorageColor);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}
