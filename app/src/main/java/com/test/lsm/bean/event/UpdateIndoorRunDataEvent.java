package com.test.lsm.bean.event;

import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/20
 */
public class UpdateIndoorRunDataEvent {

    private List<BarEntry> mValues;

    public UpdateIndoorRunDataEvent(List<BarEntry> mValues) {
        this.mValues = mValues;
    }

    public List<BarEntry> getmValues() {
        return mValues;
    }

    public void setmValues(List<BarEntry> mValues) {
        this.mValues = mValues;
    }
}
