package com.swm.core.temp;

/**
 * Created by yangzhenyu on 2017/5/8.
 */

public class RemoveBaselineWander implements SwmFilter {

    private static final double[] B_250 = {0.019011975760990, 0};
    private static final double[] A_250 = {1, -0.980988024239010};
    private static final double SI_250=0.980988024239010;

    private static final double[] B_360 = {0.013241579947798,0};
    private static final double[] A_360 = {1,-0.98675842005220};
    private static final double SI_360=0.986758420052202;

    private final double[] b;
    private double[] a;

    private double[] x;

    public RemoveBaselineWander(int sampleRate) {
        b = sampleRate == 360 ? B_360 : B_250;
        a = sampleRate == 360 ? A_360 : A_250;
    }

    @Override
    public void filter(EcgData ecgData) {
        int ndata = ecgData.samples.length;

        // At last 5 points to calculate baseline
        if (ndata < 5)
            return;

        int n = 0;

        double[] baseline = new double[ndata + 6];

        double xN = ecgData.samples[ndata-1];
        double xNMinus1 = ecgData.samples[ndata-2];
        double xNMinus2 = ecgData.samples[ndata-3];
        double xNMinus3 = ecgData.samples[ndata-4];

        if (x == null) {
            x = new double[4];
            for (int i = 0; i < 4; i++)
                x[i] = ecgData.samples[i];
        } else {
            x[3] = ecgData.samples[0];
        }

        for (int i = 0; i < 3; i++)
           baseline[i] = 2 * x[0] - x[3-i];

        for (int i = 0; i < ndata; i++)
            baseline[i+3] = ecgData.samples[i];

        baseline[ndata+5] = 2 * xN - xNMinus1;
        baseline[ndata+4] = 2 * xN - xNMinus2;
        baseline[ndata+3] = 2 * xN - xNMinus3;

        for (n = 0; n < ndata + 6; n++) {
            double y0 = baseline[n];
            double yNMinus1 = n > 0 ? baseline[n-1] : 0;

            baseline[n] = b[0] * y0 + b[1] * yNMinus1 - a[1] * yNMinus1;
        }

        n = ndata + 5;

        do {
            double yN = baseline[n];
            double yNPlus1 = n == ndata + 5 ? 0 : baseline[n+1];

            baseline[n] = b[0] * yN + b[1] * yNPlus1 - a[1] * yNPlus1;

            if(n >= 3 && n <= (ndata+2)) {
                double y = ecgData.samples[n-3] - baseline[n];
                ecgData.samples[n-3] = y;
            }

            n--;
        } while(n > 0);

        for (int i = 0; i < 3; i++)
            x[i] = x[i+1];
    }

    @Override
    public void reset() {
        x = null;
    }
}
