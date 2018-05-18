package com.swm.core;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/14
 */
public class HeartRateService {

    static {
        System.loadLibrary("swm_ecg_algo");
    }

    public  static native int CalculateHeartRate(int[] i32ECGRawBuffer);
    public static native void GetRtoRIntervalData(double[] rriAry, double[] timeAry);
    public static native float GetSdnn();
    public  static native float GetRmssd();
    public static native int InitialForModeChange(int mode);

    public static native double[] GetRtoRIntervalData2();

    public static native void getHistoryRRI(short[] historyRRI);

}
