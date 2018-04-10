package com.test.lsm.bean;

import com.clj.fastble.data.BleDevice;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/9
 */
public class BleConnectMessage {

    private int status;
    private BleDevice bleDevice;

    public BleConnectMessage(int status, BleDevice bleDevice) {
        this.status = status;
        this.bleDevice = bleDevice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }
}
