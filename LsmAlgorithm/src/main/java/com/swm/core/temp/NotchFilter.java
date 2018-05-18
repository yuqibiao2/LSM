package com.swm.core.temp;

/**
 * Created by yangzhenyu on 2017/5/8.
 */

public class NotchFilter implements SwmFilter {

    private static final double[] NOTCH_B_250 = {0.645263428365958,-0.0810328518007290,0.645263428365958};
    private static final double[] NOTCH_A_250 = {1,-0.0810328518007290,0.290526856731916};
    private static final double[] NOTCH_B_360 = {0.733153829077499,-0.733153829077499,0.733153829077499};
    private static final double[] NOTCH_A_360 = {1,-0.733153829077499,0.466307658154999};

    private final double[] b;
    private final double[] a;

    private double x1 = 0;
    private double x0 = 0;

    private double y1 = 0;
    private double y0 = 0;

    public NotchFilter(int sampleRate) {
        b = sampleRate == 360 ? NOTCH_B_360 : NOTCH_B_250;
        a = sampleRate == 360 ? NOTCH_A_360 : NOTCH_A_250;
    }

    @Override
    public void filter(EcgData ecgData) {

        int length = ecgData.samples.length;

        for (int n = 0; n < length; n++) {

            double x2 = ecgData.samples[n];   // n

            double y2 = x2 * b[0] + b[1] * x1 + b[2] * x0 - a[1] * y1 - a[2] * y0;

            x0 = x1;
            x1 = x2;

            y0 = y1;    // n - 2 = n - 1
            y1 = y2;    // n - 1 = n

            ecgData.samples[n] = y2;

        }
    }

    @Override
    public void reset() {
        x0 = 0;
        x1 = 0;

        y0 = 0;
        y1 = 0;
    }
}
