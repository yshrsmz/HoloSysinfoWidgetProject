package com.yslibrary.android.holosysinfowidget.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yslibrary.android.holosysinfowidget.R;
import com.yslibrary.android.holosysinfowidget.Util;
import com.yslibrary.android.holosysinfowidget.dao.InternalStorage;
import com.yslibrary.android.holosysinfowidget.dao.Ram;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private void updateMemoryInfo() {
        // get memory info
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        Ram ram = new Ram(activityManager);

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

        // progress bar for internalStorage
        ProgressBar pbInternalStorage = (ProgressBar)findViewById(R.id.var_progress_storage);
        pbInternalStorage.setProgress(iStoragePercentage);

        // percentage for internalStorage
        TextView tvStoragePercentage = (TextView)findViewById(R.id.var_storage_percentage);
        tvStoragePercentage.setText(Integer.toString(iStoragePercentage) + "%");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
