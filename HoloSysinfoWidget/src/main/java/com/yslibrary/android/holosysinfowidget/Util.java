package com.yslibrary.android.holosysinfowidget;

import java.math.BigDecimal;

/**
 * Created by yshrsmz on 2013/11/09.
 */
public class Util {

    private static int BYTE_SIZE = 1024;

    private static String UNIT_B = "B";
    private static String UNIT_KB = "KB";
    private static String UNIT_MB = "MB";
    private static String UNIT_GB = "GB";

    public static String getBestMatchSize(long memorySize) {
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

    public static int getPercentage(long total, long current) {
        double result;
        double dTotal = (double)total;
        double dCurrent = (double)current;

        result = dCurrent / dTotal * 100;

        return (int)result;
    }
}
