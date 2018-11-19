package com.test.lsm.db.service;

import com.test.lsm.MyApplication;
import com.test.lsm.db.AFibExpRecordDao;
import com.test.lsm.db.bean.AFibExpRecord;
import com.test.lsm.db.service.inter.IAFibExpRecordService;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/14
 */
public class AFibExpRecordService implements IAFibExpRecordService {

    private final AFibExpRecordDao expRecordDao;

    public AFibExpRecordService() {
        expRecordDao = MyApplication.getDaoInstant().getAFibExpRecordDao();
    }

    @Override
    public void add(AFibExpRecord expRecord) {
        expRecordDao.insert(expRecord);
    }

    @Override
    public void delete(Long id) {
        expRecordDao.deleteByKey(id);
    }

    @Override
    public List<AFibExpRecord> getALL() {
        return expRecordDao
                .queryBuilder()
                .orderDesc(AFibExpRecordDao.Properties.CreateTime)
                .list();
    }
}
