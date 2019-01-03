package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/14
 */
public class GetUserInfoReturn {

    private String result;
    //private PdBean pd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

  /*  public PdBean getPd() {
        return pd;
    }

    public void setPd(PdBean pd) {
        this.pd = pd;
    }*/

    public static class PdBean {

        private String USER_IMAGE;
        private String RIGHTS;
        private String USER_SEX;
        private String IP;
        private String PHONE;
        private String URGENT_USER;
        private int USER_ID;
        private String USER_WEIGHT;
        private String URGENT_PHONE;
        private String LAST_LOGIN;
        private String STATUS;
        private String PASSWORD;
        private String USER_HEIGHT;
        private String USERNAME;
        private int HEALTH_PARAM;
        private String BIRTHDAY;

        public String getUSER_IMAGE() {
            return USER_IMAGE;
        }

        public void setUSER_IMAGE(String USER_IMAGE) {
            this.USER_IMAGE = USER_IMAGE;
        }

        public String getRIGHTS() {
            return RIGHTS;
        }

        public void setRIGHTS(String RIGHTS) {
            this.RIGHTS = RIGHTS;
        }

        public String getUSER_SEX() {
            return USER_SEX;
        }

        public void setUSER_SEX(String USER_SEX) {
            this.USER_SEX = USER_SEX;
        }

        public String getIP() {
            return IP;
        }

        public void setIP(String IP) {
            this.IP = IP;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public String getURGENT_USER() {
            return URGENT_USER;
        }

        public void setURGENT_USER(String URGENT_USER) {
            this.URGENT_USER = URGENT_USER;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getUSER_WEIGHT() {
            return USER_WEIGHT;
        }

        public void setUSER_WEIGHT(String USER_WEIGHT) {
            this.USER_WEIGHT = USER_WEIGHT;
        }

        public String getURGENT_PHONE() {
            return URGENT_PHONE;
        }

        public void setURGENT_PHONE(String URGENT_PHONE) {
            this.URGENT_PHONE = URGENT_PHONE;
        }

        public String getLAST_LOGIN() {
            return LAST_LOGIN;
        }

        public void setLAST_LOGIN(String LAST_LOGIN) {
            this.LAST_LOGIN = LAST_LOGIN;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getPASSWORD() {
            return PASSWORD;
        }

        public void setPASSWORD(String PASSWORD) {
            this.PASSWORD = PASSWORD;
        }

        public String getUSER_HEIGHT() {
            return USER_HEIGHT;
        }

        public void setUSER_HEIGHT(String USER_HEIGHT) {
            this.USER_HEIGHT = USER_HEIGHT;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public int getHEALTH_PARAM() {
            return HEALTH_PARAM;
        }

        public void setHEALTH_PARAM(int HEALTH_PARAM) {
            this.HEALTH_PARAM = HEALTH_PARAM;
        }

        public String getBIRTHDAY() {
            return BIRTHDAY;
        }

        public void setBIRTHDAY(String BIRTHDAY) {
            this.BIRTHDAY = BIRTHDAY;
        }
    }
}
