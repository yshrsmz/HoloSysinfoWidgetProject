package com.yslibrary.android.holosysinfowidget.dao;

import android.app.ActivityManager;

import com.yslibrary.android.holosysinfowidget.Util;

/**
 * Created by yshrsmz on 2013/11/10.
 */
public class Ram extends MemoryBase {

    private long threshold;
    private boolean lowMemory;

    public Ram(ActivityManager am) {
        ActivityManager activityManager = am;
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        setTotalMem(memoryInfo.totalMem);
        setAvailableMem(memoryInfo.availMem);

        setPercentage(getPercentage(getTotalMem(), getAvailableMem()));
        setUsedPercentage(getUsedPercentage(getTotalMem(), getAvailableMem()));

        this.threshold = memoryInfo.threshold;
        this.lowMemory = memoryInfo.lowMemory;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public boolean isLowMemory() {
        return lowMemory;
    }

    public void setLowMemory(boolean lowMemory) {
        this.lowMemory = lowMemory;
    }
}
