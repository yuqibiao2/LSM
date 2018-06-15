package com.test.lsm.bean.event;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/6/11
 */
public class ECGChgEvent {

    private short[] ecgValue;

    private String msg;

    public ECGChgEvent() {
    }

    public ECGChgEvent(short[] ecgValue, String msg) {
        this.ecgValue = ecgValue;
        this.msg = msg;
    }

    public short[] getEcgValue() {
        return ecgValue;
    }

    public void setEcgValue(short[] ecgValue) {
        this.ecgValue = ecgValue;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
