package com.test.lsm.bean.form;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/30
 */
public class UserCourseTimeVo {

    private Integer UC_ID;
    private String START_TIME;
    private String END_TIME;

    public Integer getUC_ID() {
        return UC_ID;
    }

    public void setUC_ID(Integer UC_ID) {
        this.UC_ID = UC_ID;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }
}
