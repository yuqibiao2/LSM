package com.test.lsm.db.service;

import com.test.lsm.MyApplication;
import com.test.lsm.db.CalorieDao;
import com.test.lsm.db.bean.Calorie;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.inter.ICalorieService;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/16
 */
public class CalorieService implements ICalorieService {

    private CalorieDao calorieDao;

    public CalorieService() {
        calorieDao = MyApplication.getDaoInstant().getCalorieDao();
    }

    @Override
    public float getCurrentDateTotalCalorie() {

        List<Calorie> currentDateCalorie = getCurrentDateCalorie();
        float total = 0;
        for (Calorie calorie : currentDateCalorie) {
            total += calorie.getCalorieValue();
        }

        return total;
    }

    @Override
    public void addCurrentDayCalorie(Calorie calorie) {

        long date = calorie.getDate();
        int hour = calorie.getHour();
        List<Calorie> list = calorieDao.queryBuilder()
                .where(CalorieDao.Properties.Date.eq(date))
                .where(CalorieDao.Properties.Hour.eq(hour))
                .list();
        List<Calorie> otherCalorieByHour = getOtherCalorieByHour(hour);
        float otherTotalCalorieValue = 0;
        if (otherCalorieByHour != null) {
            for (Calorie calorie1 : otherCalorieByHour) {
                otherTotalCalorieValue += calorie1.getCalorieValue();
            }
        }
        float result = calorie.getCalorieValue() - otherTotalCalorieValue;
        if (result < 0) {
            result = 0;
        }
        calorie.setCalorieValue(result);
        if (list != null && list.size() > 0) {//存在当天 当小时
            Calorie calorie1 = list.get(0);
            calorie1.setCalorieValue(result);
            calorieDao.update(calorie1);
        } else {
            calorieDao.save(calorie);
        }
    }

    @Override
    public List<Calorie> getCurrentDateCalorie() {

        long currentDate = MyTimeUtils.getCurrentDate();
        List<Calorie> list = calorieDao.queryBuilder()
                .where(CalorieDao.Properties.Date.eq(currentDate))
                .list();
        return list;
    }

    @Override
    public List<Calorie> getOtherCalorieByHour(int hour) {

        long currentDate = MyTimeUtils.getCurrentDate();
        List<Calorie> list = calorieDao.queryBuilder()
                .where(CalorieDao.Properties.Date.eq(currentDate))
                .where(CalorieDao.Properties.Hour.notEq(hour))
                .list();

        return list;
    }

    @Override
    public Calorie getCalorieByHourOnCurrentDay(int hour) {

        long currentDate = MyTimeUtils.getCurrentDate();
        List<Calorie> list = calorieDao.queryBuilder()
                .where(CalorieDao.Properties.Date.eq(currentDate))
                .where(CalorieDao.Properties.Hour.eq(hour))
                .list();

        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

}
