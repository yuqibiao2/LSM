package com.test.lsm.bean.vo;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/24
 */
public class SaveHeartByMinVo {

    private Integer userId;
    private Integer heartNum;
    private  Double calorieValue;
    private  Integer stepNum;
    private String currentTime;//2018-05-24 10:55

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHeartNum() {
        return heartNum;
    }

    public void setHeartNum(Integer heartNum) {
        this.heartNum = heartNum;
    }

    public Double getCalorieValue() {
        return calorieValue;
    }

    public void setCalorieValue(Double calorieValue) {
        this.calorieValue = calorieValue;
    }

    public Integer getStepNum() {
        return stepNum;
    }

    public void setStepNum(Integer stepNum) {
        this.stepNum = stepNum;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
