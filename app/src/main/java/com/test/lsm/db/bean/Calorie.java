package com.test.lsm.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/16
 */
@Entity
public class Calorie {

    @Id(autoincrement = true)
    private Long id;
    private float calorieValue;
    private int hour;
    private long date;
    @Generated(hash = 718553577)
    public Calorie(Long id, float calorieValue, int hour, long date) {
        this.id = id;
        this.calorieValue = calorieValue;
        this.hour = hour;
        this.date = date;
    }
    @Generated(hash = 1732156137)
    public Calorie() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public float getCalorieValue() {
        return this.calorieValue;
    }
    public void setCalorieValue(float calorieValue) {
        this.calorieValue = calorieValue;
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

}
