package com.test.lsm.bean.form;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/16
 */
public class QueryHRVInfo {

    private String serverId = "1";
    private String deviceMacAddress = "2";
    private String gender="M";
    private int age = 45;
    private int weight = 78;
    private int height = 175;
    private String rrInterval ;
    private String resIndex="1,2,4,6,9,10";
    private String measureTime = "2017-08-05T22:30:00";
    private String account ="singularwins";
    private String token = "jesse@SW131";

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getRrInterval() {
        return rrInterval;
    }

    public void setRrInterval(String rrInterval) {
        this.rrInterval = rrInterval;
    }

    public String getResIndex() {
        return resIndex;
    }

    public void setResIndex(String resIndex) {
        this.resIndex = resIndex;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
