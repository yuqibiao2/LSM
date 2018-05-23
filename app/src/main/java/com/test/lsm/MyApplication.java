package com.test.lsm;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.test.lsm.bean.UserBean;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.db.DaoMaster;
import com.test.lsm.db.DaoSession;
import com.today.step.lib.TodayStepManager;

import cn.jpush.android.api.JPushInterface;


/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class MyApplication extends Application{

    public Context aContext;

    private static DaoSession daoSession;

    private UserLoginReturn.PdBean user;
    private int stepNum;
    private int heartNum;
    private double calorieValue;
    private double stepDistance;

    BleDevice currentBleDevice;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.aContext = getApplicationContext();
        setupDateBase();
        initBleBT();
        /*百度地图配置*/
        SDKInitializer.initialize(aContext);
        /*初始化计步库*/
        TodayStepManager.init(this);
        /*初始化Jpush推送*/
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }

    private void setupDateBase() {
        //创建数据库shop.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lsm.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * 初始化Ble
     */
    private void initBleBT() {
        BleManager.getInstance().init(this);
        BleManager.getInstance()
                .enableLog(true)
                .setMaxConnectCount(7)
                .setOperateTimeout(5000);
    }

    public boolean isBleConnected(){
        if (currentBleDevice!=null && BleManager.getInstance().isConnected(currentBleDevice)){
            return true;
        }else{
            return  false;
        }
    }

    public UserLoginReturn.PdBean getUser() {
        return user;
    }

    public void setUser(UserLoginReturn.PdBean user) {
        this.user = user;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public int getHeartNum() {
        return heartNum;
    }

    public void setHeartNum(int heartNum) {
        this.heartNum = heartNum;
    }

    public double getStepDistance() {
        return stepDistance;
    }

    public double getCalorieValue() {
        return calorieValue;
    }

    public void setCalorieValue(double calorieValue) {
        this.calorieValue = calorieValue;
    }

    public void setStepDistance(double stepDistance) {
        this.stepDistance = stepDistance;
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    public BleDevice getCurrentBleDevice() {
        return currentBleDevice;
    }

    public void setCurrentBleDevice(BleDevice currentBleDevice) {
        this.currentBleDevice = currentBleDevice;
    }

}
