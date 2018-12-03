package com.test.lsm.bean.vo;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/6/4
 */
public class SaveUserHRVVo {

    private Integer userId;
    private String MINDFITNESS;
    private String BODYFITNESS;
    private String MOODSTABILITY;
    private String STRESSTENSION;
    private String MINDFATIGUE;
    private String BODYFATIGUE;
    private String currentTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMINDFITNESS() {
        return MINDFITNESS;
    }

    public void setMINDFITNESS(String MINDFITNESS) {
        this.MINDFITNESS = MINDFITNESS;
    }

    public String getBODYFITNESS() {
        return BODYFITNESS;
    }

    public void setBODYFITNESS(String BODYFITNESS) {
        this.BODYFITNESS = BODYFITNESS;
    }

    public String getMOODSTABILITY() {
        return MOODSTABILITY;
    }

    public void setMOODSTABILITY(String MOODSTABILITY) {
        this.MOODSTABILITY = MOODSTABILITY;
    }

    public String getSTRESSTENSION() {
        return STRESSTENSION;
    }

    public void setSTRESSTENSION(String STRESSTENSION) {
        this.STRESSTENSION = STRESSTENSION;
    }

    public String getMINDFATIGUE() {
        return MINDFATIGUE;
    }

    public void setMINDFATIGUE(String MINDFATIGUE) {
        this.MINDFATIGUE = MINDFATIGUE;
    }

    public String getBODYFATIGUE() {
        return BODYFATIGUE;
    }

    public void setBODYFATIGUE(String BODYFATIGUE) {
        this.BODYFATIGUE = BODYFATIGUE;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
