package com.test.lsm.bean.event;

/**
 * 功能：更新年、月、天
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/19
 */
public class UpdateHrRecordEvent {

    private String dateTime;

    public UpdateHrRecordEvent(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
