package com.yslibrary.android.holosysinfowidget.dao;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by yshrsmz on 2013/11/10.
 */
public class InternalStorage extends MemoryBase {
    private static final String TAG = InternalStorage.class.getSimpleName();

    private static final int INTERNAL_STORAGE_THRESHOLD_PERCENTAGE = 10;

    public InternalStorage() {

        setTotalMem(getTotalStorage(Environment.getDataDirectory()));
        setAvailableMem(getAvailableStorage(Environment.getDataDirectory()));
        setPercentage(getPercentage(getTotalMem(), getAvailableMem()));
        setUsedPercentage(getUsedPercentage(getTotalMem(), getAvailableMem()));

        setLowMemory((getPercentage() < INTERNAL_STORAGE_THRESHOLD_PERCENTAGE));
    }

    public long getTotalStorage(File item) {

        long size = 0;
        try {
            StatFs fs = new StatFs(item.getAbsolutePath());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                size = (long)(fs.getBlockSize() * fs.getBlockCount());
            } else {
                size = fs.getBlockSizeLong() * fs.getBlockCountLong();
            }

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "can't get size.", e);
        }
        return size;
    }

    public long getAvailableStorage(File item) {
        long size = 0;

        try {
            StatFs fs = new StatFs(item.getAbsolutePath());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                size = (long)fs.getBlockSize() * (long)fs.getAvailableBlocks();
            } else {
                size = fs.getBlockSizeLong() * fs.getAvailableBlocksLong();
            }

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "can't get size.", e);
        }
        return size;
    }
}
