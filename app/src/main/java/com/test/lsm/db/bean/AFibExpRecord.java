package com.test.lsm.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/14
 */
@Entity
public class AFibExpRecord {
    @Id(autoincrement = true)
    private Long id;
    private String tip;
    private long createTime;
    @Generated(hash = 1553767478)
    public AFibExpRecord(Long id, String tip, long createTime) {
        this.id = id;
        this.tip = tip;
        this.createTime = createTime;
    }
    @Generated(hash = 1909847667)
    public AFibExpRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTip() {
        return this.tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

}
