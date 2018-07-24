package com.test.lsm.bean.event;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/30
 */
public class RunStopEvent {
    private String msg;
    private List<LatLng> points;

    public RunStopEvent(List<LatLng> points) {
        this.points = points;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
