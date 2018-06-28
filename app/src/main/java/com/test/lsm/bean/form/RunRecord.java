package com.test.lsm.bean.form;

import android.content.Intent;

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
    private String avgHeart;
    private String maxHeart;
    private String calorieValue;

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

    public String getAvgHeart() {
        return avgHeart;
    }

    public void setAvgHeart(String avgHeart) {
        this.avgHeart = avgHeart;
    }

    public String getMaxHeart() {
        return maxHeart;
    }

    public void setMaxHeart(String maxHeart) {
        this.maxHeart = maxHeart;
    }

    public String getCalorieValue() {
        return calorieValue;
    }

    public void setCalorieValue(String calorieValue) {
        this.calorieValue = calorieValue;
    }
}
