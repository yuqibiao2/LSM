package com.test.lsm.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
@Entity
public class Step {

    @Id(autoincrement = true)
    private Long id;
    private int stepNum;
    private int hour;
    private long date;
    @Generated(hash = 460216993)
    public Step(Long id, int stepNum, int hour, long date) {
        this.id = id;
        this.stepNum = stepNum;
        this.hour = hour;
        this.date = date;
    }
    @Generated(hash = 561308863)
    public Step() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getHour() {
        return this.hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public int getStepNum() {
        return this.stepNum;
    }
    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

}
