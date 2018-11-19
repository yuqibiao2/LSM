package com.test.lsm.bean;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/14
 */
public class LsmBleData {

    private int hrValue;
    private double rriValue;

    public LsmBleData() {
    }

    public LsmBleData(int hrValue, double rriValue) {
        this.hrValue = hrValue;
        this.rriValue = rriValue;
    }

    public int getHrValue() {
        return hrValue;
    }

    public void setHrValue(int hrValue) {
        this.hrValue = hrValue;
    }

    public double getRriValue() {
        return rriValue;
    }

    public void setRriValue(double rriValue) {
        this.rriValue = rriValue;
    }
}
