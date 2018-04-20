package com.yyyu.lsmalgorithm;

import android.util.Log;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/12
 */
public class MyLib {

    static {
        //加载so 文件
        System.loadLibrary("native-lib");
    }


    private final static short SAMPLE_RATE = 250;
    private final static short ONE_MINUTE = 60;

    private  static double[] signal = new double[SAMPLE_RATE];
    private static int bufferIndex = 0;
    private static CircularFifoQueue<Integer> queue = new CircularFifoQueue<>(ONE_MINUTE);


    public static int countHeartRate(short[] ecg) {
        // to storage signal to buffer, and the buffer storage a second size signal.
        for (int i = 0; i < ecg.length; i++) {
            signal[i + bufferIndex * ecg.length] = ecg[i];
        }
        bufferIndex++;

        // Loading one second Signal.
        if (SAMPLE_RATE == bufferIndex * ecg.length) {
            bufferIndex = 0;
            int currentRPeaks =countRPeaks(signal, signal.length);
            queue.add(currentRPeaks);

            float sum = 0;
            for (int i = 0; i < queue.size(); i++) {
                sum += queue.get(i);
            }
            sum = sum * 60;
            Log.d("queue", queue.toString());
            int hr = (int) sum / queue.size();
            return hr < 40 ? 0 : hr > 250 ? 0 : hr;
        }
        return -1;
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static native int countRPeaks(double[] ecg, int length);

    public static native int countHeartRate(double data);

    public static native int countStep(double gyroX, double gyroY, double gyroZ, double accX, double accY, double accZ, double magnX, double magnY, double magnZ);

}
