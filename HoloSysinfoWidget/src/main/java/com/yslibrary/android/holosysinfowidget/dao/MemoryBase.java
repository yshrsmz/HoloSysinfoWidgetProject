package com.yslibrary.android.holosysinfowidget.dao;

import java.math.BigDecimal;

/**
 * Created by yshrsmz on 2013/11/10.
 */
public class MemoryBase {

    public static String TAG = MemoryBase.class.getSimpleName();

    private long totalMem;

    private long availableMem;

    private int percentage;

    private int usedPercentage;

    private static final int BYTE_SIZE = 1024;

    private static final String UNIT_B = "B";
    private static final String UNIT_KB = "KB";
    private static final String UNIT_MB = "MB";
    private static final String UNIT_GB = "GB";

    public String getBestMatchSize(long memorySize) {
        String result = "";
        double dResult = ((double)memorySize) / BYTE_SIZE;
        String resultUnit = UNIT_B;

        if (dResult > 1) {
            // KB
            resultUnit = UNIT_KB;

            if ((dResult / BYTE_SIZE) > 1) {
                // MB
                dResult = dResult / BYTE_SIZE;
                resultUnit = UNIT_MB;

                if ((dResult / BYTE_SIZE) > 1) {
                    // GB
                    dResult = dResult / BYTE_SIZE;
                    resultUnit = UNIT_GB;
                }
            }

        } else {
            // B
            dResult = memorySize;
        }

        // round result
        BigDecimal bigDecimal = new BigDecimal(dResult);
        result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + resultUnit;

        return result;
    }

    protected int getPercentage(long total, long current) {
        double result;
        double dTotal = (double)total;
        double dCurrent = (double)current;

        result = dCurrent / dTotal * 100;

        return (int)result;
    }

    protected int getUsedPercentage(long total, long current) {
        int freePercentage = getPercentage(total, current);

        return 100 - freePercentage;
    }

    public long getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(long totalMem) {
        this.totalMem = totalMem;
    }

    public String getTotalMemWithUnit() {
        return getBestMatchSize(this.totalMem);
    }

    public long getAvailableMem() {
        return availableMem;
    }

    public void setAvailableMem(long availableMem) {
        this.availableMem = availableMem;
    }

    public String getAvailableMemWithUnit() {
        return getBestMatchSize(this.availableMem);
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(int usedPercentage) {
        this.usedPercentage = usedPercentage;
    }
}
