package com.test.lsm.bean.event;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/30
 */
public class SCAutoScanChgEvent {

    public SCAutoScanChgEvent(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
