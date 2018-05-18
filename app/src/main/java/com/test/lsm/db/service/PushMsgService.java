package com.test.lsm.db.service;

import com.test.lsm.MyApplication;
import com.test.lsm.db.PushMsgDao;
import com.test.lsm.db.bean.PushMsg;
import com.test.lsm.db.service.inter.IPushMsgService;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/16
 */
public class PushMsgService implements IPushMsgService {

    private final PushMsgDao pushMsgDao;

    public PushMsgService(){
        pushMsgDao = MyApplication.getDaoInstant().getPushMsgDao();
    }

    @Override
    public boolean isMsgExists(int imgId) {
        List<PushMsg> pushMsgList =  pushMsgDao.queryBuilder().where(PushMsgDao.Properties.ImgId.eq(imgId)).list();
        if (pushMsgList!=null && pushMsgList.size()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void insertMsg(PushMsg msg) {
        pushMsgDao.insert(msg);
    }

    @Override
    public PushMsg getLastMsgByImgId(int imgId) {
        List<PushMsg> list = pushMsgDao.queryBuilder()
                .where(PushMsgDao.Properties.ImgId.eq(imgId))
                .orderDesc(PushMsgDao.Properties.Datetime)
                .list();
        return (list!=null&&list.size()>0)?list.get(0):null;
    }

    @Override
    public List<PushMsg> getAllMsg() {
        return pushMsgDao
                .queryBuilder()
                .orderDesc(PushMsgDao.Properties.Datetime)
                .list();
    }
}
