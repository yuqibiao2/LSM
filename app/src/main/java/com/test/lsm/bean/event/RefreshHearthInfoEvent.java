package com.test.lsm.bean.event;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/30
 */
public class RefreshHearthInfoEvent {

    private String msg;

    private List<Integer> rriList;

    public RefreshHearthInfoEvent(String msg, List<Integer> rriList) {
        this.msg = msg;
        this.rriList = rriList;
    }

    public List<Integer> getRriList() {
        return rriList;
    }

    public void setRriList(List<Integer> rriList) {
        this.rriList = rriList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
