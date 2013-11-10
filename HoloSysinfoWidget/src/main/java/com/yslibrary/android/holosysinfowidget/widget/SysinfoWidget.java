package com.yslibrary.android.holosysinfowidget.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.ui.MainActivity;

/**
 * Created by yshrsmz on 2013/11/09.
 */
public class SysinfoWidget extends AppWidgetProvider {
    public static final String TAG = SysinfoWidget.class.getSimpleName();

    public static final String EXTRA_APP_WIDGET_ID = "SysinfoWidgetId";

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "#onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "#onUpdate");
        Log.d(TAG, "appWidgetIds.length: " + appWidgetIds.length);
        Log.d(TAG, "appWidgetIds[0]: " + appWidgetIds[0]);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sysinfo_widget);
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);


            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        Intent intent = new Intent(context, WidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "#onDeleted");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "#onDisabled");
    }


    public static class WidgetService extends Service {
        @Override
        public void onStart(Intent intent, int si) {

        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
