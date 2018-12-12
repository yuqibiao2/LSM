package com.test.lsm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.crashlytics.android.Crashlytics;
import com.test.lsm.bean.LsmBleData;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.db.DaoMaster;
import com.test.lsm.db.DaoSession;
import com.today.step.lib.TodayStepManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.fabric.sdk.android.Fabric;


/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class MyApplication extends MultiDexApplication {

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
        /*crash统计平台接入*/
        Fabric.with(this, new Crashlytics());
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
        BleManager bleManager = BleManager.getInstance();
        bleManager.init(this);
        bleManager
                .enableLog(true)
                .setOperateTimeout(5000);
        //开始蓝牙
        if (bleManager.isSupportBle()){
          if (!bleManager.isBlueEnable()){
              bleManager.enableBluetooth();//开启蓝牙
          }
        }
    }

    public boolean isBleConnected(){
        if (currentBleDevice!=null && BleManager.getInstance().isConnected(currentBleDevice)){
            return true;
        }else{
            return  false;
        }
    }

    //---Hr讯号异常回调
    public OnHrAbnormalListener mOnHrAbnormalListener;

    public  void  setOnHrAbnormalListener(OnHrAbnormalListener onHrAbnormalListener){
        this.mOnHrAbnormalListener = onHrAbnormalListener;
    }

    public interface OnHrAbnormalListener{
        void onExp(String tip);
    }

    //---得到心跳值得回调
    public List<OnGetHrValueListener> mOnGetHrValueListenerHolder = new ArrayList<>();

    public void setOnGetHrValueListener(OnGetHrValueListener onGetHrValueListener){
        mOnGetHrValueListenerHolder.add(onGetHrValueListener);
    }

    public interface OnGetHrValueListener{
        void onGet(int hrValue);
    }

    //---得到RRI值回调
    public OnGetRriValueListener mOnGetRriValueListener;

    public void setOnGetRriValueListener(OnGetRriValueListener onGetRrriValueListener){
        this.mOnGetRriValueListener = onGetRrriValueListener;
    }

    public interface OnGetRriValueListener{
        void onGet(int rriValue);
    }

    //--得到心跳值、RRI得回调
    public OnGetBleDataValueListener mOnGetBleDataValueListener;

    public void setOnGetBleDataValueListener( OnGetBleDataValueListener onGetBleDataValueListener){
        this.mOnGetBleDataValueListener = onGetBleDataValueListener;
    }

    public interface OnGetBleDataValueListener {
        void onGet(LsmBleData lsmBleData);
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
