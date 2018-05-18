package com.swm.core.temp;

/**
 * Created by yangzhenyu on 2017/5/8.
 */

public class IirFilter implements SwmFilter {
    private double i32PrevSample;
    private double i32PrevDCSample = 0;
    private final double fIIRFilterCoeff;

    public IirFilter(double fIIRFilterCoeff) {
        this.fIIRFilterCoeff = fIIRFilterCoeff;
    }

    @Override
    public void filter(EcgData ecgData) {

        int length = ecgData.samples.length;

        for(int i = 0; i < length; i++) {
            i32PrevDCSample = (int) (ecgData.samples[i] - i32PrevSample + fIIRFilterCoeff * i32PrevDCSample);
            i32PrevSample = ecgData.samples[i];
            ecgData.samples[i] = i32PrevDCSample;
        }
    }

    @Override
    public void reset() {
        i32PrevDCSample = 0;
    }
}
