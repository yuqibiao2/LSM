package com.test.lsm.db.service.inter;

import com.test.lsm.db.bean.Step;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
public interface IStepService {

    void addCurrentDayStep(Step step);

    List<Step> getCurrentDateStep();

    /**
     * 得到当天除了某个小时的其他信息
     *
     * @return
     */
    List<Step> getOtherStepByHour(int hour);

    Step getStepByHourOnCurrentDay(int hour);

}
