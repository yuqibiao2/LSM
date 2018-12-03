package com.test.lsm.bean.vo;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/17
 */
public class UserUpdateVo {

    private  Integer USER_ID;
    private String USERNAME;
    private String PASSWORD;
    private String PHONE;
    private String USER_HEIGHT;
    private String USER_WEIGHT;
    private String USER_SEX;
    private String BIRTHDAY;
    private String URGENT_USER;
    private String URGENT_PHONE;
    private String USER_IMAGE="";
    private String HEALTH_PARAM;


    public Integer getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(Integer USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getHEALTH_PARAM() {
        return HEALTH_PARAM;
    }

    public void setHEALTH_PARAM(String HEALTH_PARAM) {
        this.HEALTH_PARAM = HEALTH_PARAM;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getUSER_HEIGHT() {
        return USER_HEIGHT;
    }

    public void setUSER_HEIGHT(String USER_HEIGHT) {
        this.USER_HEIGHT = USER_HEIGHT;
    }

    public String getUSER_WEIGHT() {
        return USER_WEIGHT;
    }

    public void setUSER_WEIGHT(String USER_WEIGHT) {
        this.USER_WEIGHT = USER_WEIGHT;
    }

    public String getUSER_SEX() {
        return USER_SEX;
    }

    public void setUSER_SEX(String USER_SEX) {
        this.USER_SEX = USER_SEX;
    }

    public String getBIRTHDAY() {
        return BIRTHDAY;
    }

    public void setBIRTHDAY(String BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }

    public String getURGENT_USER() {
        return URGENT_USER;
    }

    public void setURGENT_USER(String URGENT_USER) {
        this.URGENT_USER = URGENT_USER;
    }

    public String getURGENT_PHONE() {
        return URGENT_PHONE;
    }

    public void setURGENT_PHONE(String URGENT_PHONE) {
        this.URGENT_PHONE = URGENT_PHONE;
    }

    public String getUSER_IMAGE() {
        return USER_IMAGE;
    }

    public void setUSER_IMAGE(String USER_IMAGE) {
        this.USER_IMAGE = USER_IMAGE;
    }
}
