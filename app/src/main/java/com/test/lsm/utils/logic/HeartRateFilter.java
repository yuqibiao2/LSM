package com.test.lsm.utils.logic;

/**
 * 功能：心率过滤
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class HeartRateFilter {

    private static int startCount = 0;
    private static int displayHR = -1;
    private static int zeroCount = 0;

    public static int  doFilter(int heartRate) {

        startCount++;
        if (startCount > 6) {
            if (heartRate == 0) {
                zeroCount++;
            } else {
                zeroCount = 0;
            }
            if (zeroCount > 3) {
                displayHR = 0;
                zeroCount = 0;
            } else {
                if ((heartRate >= 30) && (heartRate <= 250)) {
                    if (displayHR == -1) {
                        displayHR = heartRate;
                    } else {
                        if (Math.abs(heartRate - displayHR) > 8) {
                            if ((heartRate - displayHR) > 0) {
                                displayHR = displayHR + 5;
                            } else {
                                displayHR = displayHR - 5;
                            }
                        } else {
                            displayHR = heartRate;
                        }
                    }
                }
            }
        }

        return displayHR;
    }

}
