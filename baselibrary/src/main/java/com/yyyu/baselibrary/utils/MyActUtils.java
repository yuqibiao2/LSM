package com.yyyu.baselibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

/**
 * 功能：activity相关工具类
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/1
 */
public class MyActUtils {

    /**
     * 判断Activity是否为最上面显示的Activity
     *
     * @param act
     * @return
     */
    public static boolean isTopAct(Activity act) {
        return getTopActivity(act).equals(act.getClass().getName());
    }

    /**
     * 判断当前界面显示的是哪个Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }

}
