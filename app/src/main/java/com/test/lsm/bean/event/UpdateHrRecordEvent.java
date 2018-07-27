package com.test.lsm.bean.event;

import java.util.Calendar;

/**
 * 功能：更新年、月、天
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/19
 */
public class UpdateHrRecordEvent {

    private String dateTime;
    private Calendar calendar;

    public UpdateHrRecordEvent(String dateTime, Calendar calendar) {
        this.dateTime = dateTime;
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
