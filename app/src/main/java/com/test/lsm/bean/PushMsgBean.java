package com.test.lsm.bean;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/10
 */
public class PushMsgBean {

    private String imgUrl;
    private int imgId;
    private String title;
    private String datetime;

    public PushMsgBean(String imgUrl, String title, String datetime) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.datetime = datetime;
    }

    public PushMsgBean(int imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
