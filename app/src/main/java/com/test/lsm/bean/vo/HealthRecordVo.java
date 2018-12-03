package com.test.lsm.bean.vo;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/27
 */
public class HealthRecordVo {

    private int userId;
    private String recordInfo;
    private String infoSavePath;
    private int irecordNterval;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRecordInfo() {
        return recordInfo;
    }

    public void setRecordInfo(String recordInfo) {
        this.recordInfo = recordInfo;
    }

    public String getInfoSavePath() {
        return infoSavePath;
    }

    public void setInfoSavePath(String infoSavePath) {
        this.infoSavePath = infoSavePath;
    }

    public int getIrecordNterval() {
        return irecordNterval;
    }

    public void setIrecordNterval(int irecordNterval) {
        this.irecordNterval = irecordNterval;
    }
}
