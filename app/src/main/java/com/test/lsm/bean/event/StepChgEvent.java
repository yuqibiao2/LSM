package com.test.lsm.bean.event;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
public class StepChgEvent {

    private int step;
    private String msg;

    public StepChgEvent(int step, String msg) {
        this.step = step;
        this.msg = msg;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
