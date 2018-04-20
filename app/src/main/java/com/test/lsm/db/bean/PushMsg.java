package com.test.lsm.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/16
 */
@Entity
public class PushMsg {

    @Id(autoincrement = true)
    private Long id;
    private Integer imgId;
    private String imgUrl;
    private String title;
    private String datetime;

    public PushMsg(Long id, Integer imgId, String imgUrl, String title) {
        this.id = id;
        this.imgId = imgId;
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public PushMsg() {
    }

    @Generated(hash = 1512588120)
    public PushMsg(Long id, Integer imgId, String imgUrl, String title,
            String datetime) {
        this.id = id;
        this.imgId = imgId;
        this.imgUrl = imgUrl;
        this.title = title;
        this.datetime = datetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
