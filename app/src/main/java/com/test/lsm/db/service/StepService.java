package com.test.lsm.db.service;

import com.test.lsm.MyApplication;
import com.test.lsm.db.StepDao;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.inter.IStepService;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/15
 */
public class StepService implements IStepService {

    private final StepDao stepDao;

    public StepService() {
        stepDao = MyApplication.getDaoInstant().getStepDao();
    }

    @Override
    public void addCurrentDayStep(Step step) {
        long date = step.getDate();
        int hour = step.getHour();
        List<Step> list = stepDao.queryBuilder()
                .where(StepDao.Properties.Date.eq(date))
                .where(StepDao.Properties.Hour.eq(hour))
                .list();
        List<Step> beforeStepByHour = getOtherStepByHour(hour);
        int beforeStepNum = 0;
        if (beforeStepByHour!=null){
            for (Step step1 : beforeStepByHour){
                beforeStepNum+=step1.getStepNum();
            }
        }
        int result = step.getStepNum() - beforeStepNum;
        if (result<0){
            result=0;
        }
        step.setStepNum(result);
        if (list != null && list.size() > 0) {//存在当天当小时
            Step step1 = list.get(0);
            step1.setStepNum(result);
            stepDao.update(step1);
        } else {
            stepDao.save(step);
        }
    }

    @Override
    public List<Step> getCurrentDateStep() {
        long currentDate = MyTimeUtils.getCurrentDate();
        List<Step> list = stepDao.queryBuilder()
                .where(StepDao.Properties.Date.eq(currentDate))
                .list();
        return list;
    }

    @Override
    public List<Step> getOtherStepByHour(int hour) {
        long currentDate = MyTimeUtils.getCurrentDate();
        List<Step> list = stepDao.queryBuilder()
                .where(StepDao.Properties.Date.eq(currentDate))
                .where(StepDao.Properties.Hour.notEq(hour)).list();//防止时间被修改了
        return list;
    }

    @Override
    public Step getStepByHourOnCurrentDay(int hour) {
        long currentDate = MyTimeUtils.getCurrentDate();
        List<Step> list = stepDao.queryBuilder()
                .where(StepDao.Properties.Date.eq(currentDate))
                .where(StepDao.Properties.Hour.eq(hour)).list();//防止时间被修改了
        return (list!=null&&list.size()>0)?list.get(0):null;
    }


}
