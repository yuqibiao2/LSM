package com.test.lsm.service;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.test.lsm.MyApplication;
import com.yyyu.baselibrary.utils.MyLog;

import de.greenrobot.event.EventBus;

/**
 * 功能：检测蓝牙设备是否连接的service
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/26
 */
public class CheckBleIsConnectService extends Service {

    private static final String TAG = "CheckBleIsConnectServic";

    private final static int TIME_SPACE = 5 * 1000;
    private MyApplication application;
    private BleDevice currentBleDevice;
    private BleManager bleManager;
    public static boolean isNotify = false;
    private boolean isCheck = true;
    public final static String BLE_DISCONNECT = "BLE_DISCONNECT";

    @Override
    public void onCreate() {
        super.onCreate();
        application = (MyApplication) getApplication();
        currentBleDevice = application.getCurrentBleDevice();
        bleManager = BleManager.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

/*        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isCheck) {
                    try {
                        if (currentBleDevice != null) {//连接过了才检测
                            if (!bleManager.isConnected(currentBleDevice)) {//未连接
                                if (!isNotify) {
                                    MyLog.e(TAG, "==currentBleDevice===未连接");
                                    EventBus.getDefault().post(BLE_DISCONNECT);
                                    isNotify = true;
                                }
                            }else{
                                isNotify = false;
                            }
                        }
                        MyLog.e(TAG, "=====service");
                        Thread.sleep(TIME_SPACE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        return super.onStartCommand(intent, flags, startId);
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        isCheck = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
