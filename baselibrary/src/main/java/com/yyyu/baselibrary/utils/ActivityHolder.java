package com.yyyu.baselibrary.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：维护Activity
 *
 * @author yyyu
 * @date 2016/5/18
 */
public class ActivityHolder {

    public static List<Activity> activities = new ArrayList<>();

    /**
     * 添加activity
     * @param act
     */
    public static void addActivity(Activity act){
        activities.add(act);
    }

    /**
     * 移除activity
     *
     * @param act
     */
    public static void removeActivity(Activity act){
        activities.remove(act);
    }

    /**
     * 销毁某个activity
     *
     * @param cls 类名
     */
    public static void finishedActivity(Class<?> cls){
        for (Activity act: activities) {
            if (!act.isFinishing() && act.getClass().equals(cls)){
                act.finish();
            }
        }
    }

    /**
     * 销毁所有的activity
     */
    public static void finishedAll(){
        for (Activity act: activities) {
            if (!act.isFinishing()){
                act.finish();
            }
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){

        return activities.get(activities.size()-1);
    }


}