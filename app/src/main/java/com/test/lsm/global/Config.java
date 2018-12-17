package com.test.lsm.global;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/12/12
 */
public class Config {

    //---蓝牙重连时间间隔
    public static  final  int BLE_RETRY_INTERVAL = 15*1000;//15s
    public static final int BLE_RETRY_TIME_UPPER = 10*4;//10分鐘
    public static  int bleRetryTime = BLE_RETRY_TIME_UPPER;
    //---当前心跳上传时间间隔
    public static  final int UPLOAD_CUR_HEALTH_INTERVAL = 10*1000;
    //---监控界面刷新时间间隔
    public static  final int MONITOR_REFRESH_INTERVAL = UPLOAD_CUR_HEALTH_INTERVAL;

}
