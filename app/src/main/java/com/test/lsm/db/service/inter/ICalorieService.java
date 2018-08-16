package com.test.lsm.db.service.inter;

import com.test.lsm.db.bean.Calorie;
import com.test.lsm.db.bean.Step;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/16
 */
public interface ICalorieService {


    /**
     * 得到当前的所有卡路里值
     *
     * @return
     */
    float getCurrentDateTotalCalorie();

    /**
     * 记录卡路里
     * 当前天 当前小时 下只有一条记录 存在则更新
     *
     * @param calorie
     */
    void addCurrentDayCalorie(Calorie calorie);

    /**
     * 得到当前天的步数(所有小时的)
     */
    List<Calorie> getCurrentDateCalorie();

    /**
     * 得到当天除了某个小时的其他信息
     *
     * @return
     */
    List<Calorie> getOtherCalorieByHour(int hour);

    /**
     * 得到当前天某一小时的步数
     *
     * @param hour
     * @return
     */
    Calorie getCalorieByHourOnCurrentDay(int hour);

}
