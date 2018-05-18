package com.swm.core.temp;

import android.util.Log;


/**
 * Created by yangzhenyu on 2017/5/8.
 */

public class FirFilter implements SwmFilter {
    private static final double[] BN_250 = {0.0194041444581080
                ,0.0450147864686318
                ,0.0815814643972139
                ,0.119825032244859
                ,0.149014391144678
                ,0.159968759587586
                ,0.149014391144678
                ,0.119825032244859
                ,0.0815814643972139
                ,0.0450147864686318
                ,0.0194041444581080};

    private static final double[] BN_360 = {0.00895567203488443
                ,0.0167898345946794
                ,0.0295617071851600
                ,0.0454324200454105
                ,0.0629696314718780
                ,0.0801389042614653
                ,0.0946360340878712
                ,0.104339082954544
                ,0.107753910440413
                ,0.104339082954544
                ,0.0946360340878712
                ,0.0801389042614653
                ,0.0629696314718780
                ,0.0454324200454105
                ,0.0295617071851600
                ,0.0167898345946794
                ,0.00895567203488443};

    private final double[] bn;
    private double xn;
    private double xnMinus1;
    private double xnMinus2;
    private double xnMinus3;
    private double xnMinus4;
    private double xnMinus5;
    private double xnMinus6;
    private double xnMinus7;
    private double xnMinus8;
    private double xnMinus9;
    private double xnMinus10;

    public FirFilter(int sampleRate) {
        bn = (sampleRate == 360 ? BN_360 : BN_250);
    }

    @Override
    public void filter(EcgData ecgData) {
        double tmp = 0;
        int limit = ecgData.samples.length > bn.length ? bn.length : ecgData.samples.length;
        Log.d("Shower", "Limit: " + limit);
        int n = limit - 1;

        for (int i = 0; i < limit; i++) {
            xn = ecgData.samples[i];

            tmp = bn[0] * xn
                    + bn[1] * xnMinus1
                    + bn[2] * xnMinus2
                    + bn[3] * xnMinus3
                    + bn[4] * xnMinus4
                    + bn[5] * xnMinus5
                    + bn[6] * xnMinus6
                    + bn[7] * xnMinus7
                    + bn[8] * xnMinus8
                    + bn[9] * xnMinus9
                    + bn[10] * xnMinus10;

            ecgData.samples[i] = tmp;

            xnMinus10 = xnMinus9;
            xnMinus9 = xnMinus8;
            xnMinus8 = xnMinus7;
            xnMinus7 = xnMinus6;
            xnMinus6 = xnMinus5;
            xnMinus5 = xnMinus4;
            xnMinus4 = xnMinus3;
            xnMinus2 = xnMinus1;
            xnMinus1 = xn;

        }
    }

    @Override
    public void reset() {
        xn = 0;
        xnMinus1 = 0;
        xnMinus2 = 0;
        xnMinus3 = 0;
        xnMinus4 = 0;
        xnMinus5 = 0;
        xnMinus6 = 0;
        xnMinus7 = 0;
        xnMinus8 = 0;
        xnMinus9 = 0;
        xnMinus10 = 0;
    }
}
