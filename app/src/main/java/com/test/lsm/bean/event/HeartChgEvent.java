package com.test.lsm.bean.event;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
public class HeartChgEvent {

    private int heartNUm;
    private String msg;

    public HeartChgEvent() {
    }

    public HeartChgEvent(int heartNUm, String msg) {
        this.heartNUm = heartNUm;
        this.msg = msg;
    }

    public int getHeartNUm() {
        return heartNUm;
    }

    public void setHeartNUm(int heartNUm) {
        this.heartNUm = heartNUm;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
