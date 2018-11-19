package com.test.lsm.db.service.inter;

import com.test.lsm.db.bean.AFibExpRecord;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/14
 */
public interface IAFibExpRecordService {

    /**
     * 添加
     *
     * @param expRecord
     */
    void add(AFibExpRecord expRecord);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 得到所有
     *
     * @return
     */
    List<AFibExpRecord> getALL();

}
