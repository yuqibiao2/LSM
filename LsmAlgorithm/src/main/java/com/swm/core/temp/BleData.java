package com.swm.core.temp;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

/**
 * Created by yangzhenyu on 2016/9/25.
 */
public class BleData {

    public byte[] rawData;

    public BleData(byte[] rawData) {
        this.rawData = rawData;
    }

}
