package com.yslibrary.android.holosysinfowidget.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.service.BatteryNotificationService;

/**
 * Created by A12897 on 13/11/18.
 */
public class SettingActivity extends PreferenceActivity {

    private final static String TAG = SettingActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new settingFragment())
                    .commit();
        }
    }


    public static class settingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preference);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if (key.equals(getString(R.string.pref_key_activate_notification))) {
                // activate/deactivate BatteryNotificationService
                boolean value = sharedPreferences.getBoolean(key, true);

                Log.d(TAG, "pref - key: " + key + ", value: " + value);

                if (value) {
                    getActivity().startService(new Intent(getActivity().getBaseContext(), BatteryNotificationService.class));
                } else {
                    getActivity().stopService(new Intent(getActivity().getBaseContext(), BatteryNotificationService.class));
                }
            }
        }
    }
}
