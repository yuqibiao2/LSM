package com.test.lsm.bean.event;

import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/12/5
 */
public class UpdateMemData {

    GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo;

    public GetMonitorGroupDetailReturn.DataBean.MemInfoListBean getMemInfo() {
        return memInfo;
    }

    public void setMemInfo(GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo) {
        this.memInfo = memInfo;
    }

}
