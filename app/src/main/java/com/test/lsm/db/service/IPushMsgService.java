package com.test.lsm.db.service;

import com.test.lsm.db.bean.PushMsg;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/16
 */
public interface IPushMsgService {

    boolean isMsgExists(int imgId);

    void insertMsg(PushMsg msg);

    PushMsg getLastMsgByImgId(int imgId);

    List<PushMsg> getAllMsg ();

}
