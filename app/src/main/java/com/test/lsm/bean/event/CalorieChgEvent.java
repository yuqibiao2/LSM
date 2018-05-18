package com.test.lsm.bean.event;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
public class CalorieChgEvent {

    private float calorieNum;
    private String msg;

    public CalorieChgEvent(float calorieNum, String msg) {
        this.calorieNum = calorieNum;
        this.msg = msg;
    }

    public float getCalorieNum() {
        return calorieNum;
    }

    public void setCalorieNum(float calorieNum) {
        this.calorieNum = calorieNum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
