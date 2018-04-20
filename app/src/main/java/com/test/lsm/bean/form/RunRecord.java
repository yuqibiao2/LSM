package com.test.lsm.bean.form;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/19
 */
public class RunRecord {

    private int userId;
    private String startTime;
    private String stopTime;
    private String coordinateInfo;
    private double distance;
    private String runTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getCoordinateInfo() {
        return coordinateInfo;
    }

    public void setCoordinateInfo(String coordinateInfo) {
        this.coordinateInfo = coordinateInfo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }
}
