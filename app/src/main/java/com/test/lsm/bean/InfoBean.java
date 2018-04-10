package com.test.lsm.bean;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/10
 */
public class InfoBean {

    private int type;
    private String info;

    public InfoBean(int type, String info) {
        this.type = type;
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
