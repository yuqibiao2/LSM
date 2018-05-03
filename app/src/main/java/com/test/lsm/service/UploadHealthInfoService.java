package com.test.lsm.service;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.test.lsm.MyApplication;
import com.test.lsm.bean.form.UserHealthInfo;
import com.test.lsm.bean.json.SaveUserHealthInfoReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

/**
 * 功能：上传用户信息的service
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/19
 */
public class UploadHealthInfoService extends Service{

    private static final String TAG = "UploadHealthInfoService";

    boolean isUpload = true;
    private APIMethodManager apiMethodManager;
    private MyApplication application;
    private int user_id;
    private String phone;

    @Override
    public void onCreate() {
        apiMethodManager = APIMethodManager.getInstance();
        application = (MyApplication) getApplication();
        UserLoginReturn.PdBean user = application.getUser();
        user_id = user.getUSER_ID();
        phone = user.getPHONE();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //---TODO 判断设备是否连接、连接时才上传数据
        String tel = phone.trim();
        if ("0937999127".equals(tel)
                ||"0930583683".equals(tel)
                ||"0911568010".equals(tel)
                ||"0980999941".equals(tel)){//判断给定的四个人
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (isUpload){
                                if (isRightTime()){
                                    int heartNum = application.getHeartNum();
                                    int stepNum = application.getStepNum();
                                    double calorieValue = application.getCalorieValue();
                                    UserHealthInfo userHealthInfo = new UserHealthInfo();
                                    userHealthInfo.setUserId(user_id);
                                    userHealthInfo.setHeartNum(heartNum);
                                    userHealthInfo.setStepNum(stepNum);
                                    userHealthInfo.setCalorieValue(calorieValue);
                                    userHealthInfo.setRawData(Constant.sbHeartData.toString());
                                    Constant.sbHeartData = new StringBuffer();
                                    //TODO 提交数据
                                    apiMethodManager.saveUserHealthInfo(userHealthInfo, new IRequestCallback<SaveUserHealthInfoReturn>() {
                                        @Override
                                        public void onSuccess(SaveUserHealthInfoReturn result) {
                                            MyLog.e(TAG , "==userHealthInfo=提交数据成功");
                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {
                                            MyLog.e(TAG , "==userHealthInfo=提交数据失败"+throwable.getMessage());
                                        }
                                    });
                                    Thread.sleep(2*1000);
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 5.2、5.3、5.4、5.5 期间
     *
     * @return
     */
    private boolean isRightTime(){
        String currentDateTime = MyTimeUtils.getCurrentDateTime();
        //MyLog.d(TAG , "currentDateTime："+currentDateTime);
       /* return MyTimeUtils.timeCompare(currentDateTime,"2018-05-01 23:59:59")>0
                && MyTimeUtils.timeCompare(currentDateTime,"2018-05-05 23:59:59")<0;*/
       return true;
    }

    @Override
    public void onDestroy() {
        isUpload = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
