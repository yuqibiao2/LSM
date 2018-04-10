package com.test.lsm;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;


/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class MyApplication extends Application{

    public Context aContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.aContext = getApplicationContext();
        /*百度地图配置*/
        SDKInitializer.initialize(aContext);
    }
}
