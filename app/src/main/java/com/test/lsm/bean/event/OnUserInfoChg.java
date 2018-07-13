package com.test.lsm.bean.event;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/12
 */
public class OnUserInfoChg {

    public OnUserInfoChg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
