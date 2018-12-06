package com.test.lsm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.test.lsm.MyApplication;
import com.test.lsm.bean.json.EmptyDataReturn;
import com.test.lsm.bean.vo.QueryHRVInfo;
import com.test.lsm.bean.vo.SaveCurrentHealthVo;
import com.test.lsm.bean.vo.SaveHeartByMinVo;
import com.test.lsm.bean.vo.SaveUserHRVVo;
import com.test.lsm.bean.vo.UserHealthInfo;
import com.test.lsm.bean.json.GetActiveUser;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.bean.json.SaveHeartByMin;
import com.test.lsm.bean.json.SaveUserHRV;
import com.test.lsm.bean.json.SaveUserHealthInfoReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Date;
import java.util.List;

/**
 * 功能：上传用户信息的service
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/19
 */
public class UploadHealthInfoService extends Service {

    private static final String TAG = "UploadHealthInfoService";

    boolean isUpload = true;
    private APIMethodManager apiMethodManager;
    private MyApplication application;
    private int user_id;
    private String phone;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.e(TAG , "onCreate=======================");

        apiMethodManager = APIMethodManager.getInstance();
        application = (MyApplication) getApplication();
        UserLoginReturn.PdBean user = application.getUser();
        if (user!=null){
            user_id = user.getUSER_ID();
            phone = user.getPHONE().trim();
        }else{
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        toUploadSpecified(phone);
        toUploadUsual();
        toUploadUserHRV();
        toUploadCurrentHealthInfo();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 上传当前心跳值（10s钟上传一次）
     *
     */
    private void toUploadCurrentHealthInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                  while (isUpload){
                      Thread.sleep(10*1000);
                      SaveCurrentHealthVo healthVo = new SaveCurrentHealthVo();
                      healthVo.setUserId(user_id);
                      healthVo.setHeartNum( application.getHeartNum());
                      healthVo.setCalorieValue(application.getCalorieValue());
                      healthVo.setStepNum(application.getStepNum());
                      apiMethodManager.saveCurrentHealthInfo(healthVo, new IRequestCallback<EmptyDataReturn>() {
                          @Override
                          public void onSuccess(EmptyDataReturn result) {

                          }

                          @Override
                          public void onFailure(Throwable throwable) {

                          }
                      });
                  }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 上传用户HRV（每十分钟上传一次）
     *
     */
    private void toUploadUserHRV(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(3*60*1000);

                        //rriBufferCircularFifoQueue<Long> rriBuffer = Constant.rriBuffer;
                        List<Integer> lastedUsefulRriList = Constant.lastedUsefulRriList;
                        if (!application.isBleConnected()|| lastedUsefulRriList.size()<200) {
                            continue;
                        }
                        StringBuffer rrlIntervalSb = new StringBuffer();
                        for (int i=0 ; i<lastedUsefulRriList.size() ; i++){
                            Integer rriValue = lastedUsefulRriList.get(i);
                            if (i==lastedUsefulRriList.size()-1){
                                rrlIntervalSb.append(rriValue+"");
                            }else{
                                rrlIntervalSb.append(rriValue+",");
                            }
                        }
                        QueryHRVInfo hrvInfo = new QueryHRVInfo();
                        hrvInfo.setRrInterval(rrlIntervalSb.toString());
                        apiMethodManager.getHRVInfo(hrvInfo, new IRequestCallback<GetHRVInfoReturn>() {
                            @Override
                            public void onSuccess(GetHRVInfoReturn result) {
                                List<GetHRVInfoReturn.HRVIndexBean> hrvIndex = result.getHRVIndex();
                                if (hrvIndex != null && hrvIndex.size() > 0) {
                                    GetHRVInfoReturn.HRVIndexBean hrvIndexBean = hrvIndex.get(0);
                                    //---体力状态
                                    Integer bodyFitness = Integer.parseInt(hrvIndexBean.getBodyFitness());
                                    //---身体疲劳
                                    Integer bodyFatigue = Integer.parseInt(hrvIndexBean.getBodyFatigue());
                                    //---压力紧张
                                    Integer stressTension = Integer.parseInt(hrvIndexBean.getStressTension());
                                    //---心情稳定
                                    Integer moodStability = Integer.parseInt(hrvIndexBean.getMoodStability());
                                    SaveUserHRVVo saveUserHRVVo = new SaveUserHRVVo();
                                    saveUserHRVVo.setUserId(user_id);
                                    saveUserHRVVo.setBODYFITNESS(""+hrvIndexBean.getBodyFitness());
                                    saveUserHRVVo.setBODYFATIGUE(""+hrvIndexBean.getBodyFatigue());
                                    saveUserHRVVo.setSTRESSTENSION(""+hrvIndexBean.getStressTension());
                                    saveUserHRVVo.setMOODSTABILITY(""+hrvIndexBean.getMoodStability());
                                    saveUserHRVVo.setMINDFITNESS(""+hrvIndexBean.getMindFitness());
                                    saveUserHRVVo.setMINDFATIGUE(""+hrvIndexBean.getBodyFatigue());

                                    apiMethodManager.saveUserHRV(saveUserHRVVo, new IRequestCallback<SaveUserHRV>() {
                                        @Override
                                        public void onSuccess(SaveUserHRV result) {
                                            String code = result.getResult();
                                            MyLog.e(TAG , "==code="+code);
                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {
                                            MyLog.e(TAG , ""+throwable.getMessage());
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                MyLog.e(TAG, "getHRVInfo异常：" + throwable.getMessage());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * 每个用户都上传（一分钟上传一次）
     */
    private void toUploadUsual() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isUpload) {
                        Thread.sleep(60 * 1000);
                      /*  if (!application.isBleConnected()) {
                            continue;
                        }*/
                        //计算平均值
                        CircularFifoQueue<Integer> oneMinHeart = Constant.oneMinHeart;
                        int total = 0;
                        for (Integer heartNum : oneMinHeart) {
                            total += heartNum;
                        }
                        int size = oneMinHeart.size();
                        int avgHearNum =size>0? total /size : 0 ;
                        SaveHeartByMinVo saveHeartByMinVo = new SaveHeartByMinVo();
                        saveHeartByMinVo.setUserId(user_id);
                        saveHeartByMinVo.setHeartNum(avgHearNum);
                        double calorieValue = application.getCalorieValue();
                        saveHeartByMinVo.setCalorieValue(calorieValue);
                        int stepNum = application.getStepNum();
                        saveHeartByMinVo.setStepNum(stepNum);
                        String currentDateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", new Date(System.currentTimeMillis()));
                        saveHeartByMinVo.setCurrentTime(currentDateTime);

                        apiMethodManager.saveHeartByMin(saveHeartByMinVo, new IRequestCallback<SaveHeartByMin>() {
                            @Override
                            public void onSuccess(SaveHeartByMin result) {
                                //MyLog.e(TAG, "上传成功==saveHeartByMin="+result.getResult());
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                MyLog.e(TAG, "saveHeartByMin：" + throwable.getMessage());
                            }
                        });

                        Constant.oneMinHeart = new CircularFifoQueue<>(60);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 上传指定人数的信息
     *
     * @param tel :当前用户的电话号码
     */
    private void toUploadSpecified(final String tel) {

        apiMethodManager.getActiveUserList(new IRequestCallback<GetActiveUser>() {
            @Override
            public void onSuccess(GetActiveUser result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    List<GetActiveUser.PdBean> pd = result.getPd();
                    for (GetActiveUser.PdBean bean : pd) {
                        if (tel.equals(bean.getTel().trim())) {//符合条件
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        while (isUpload) {

                                            Thread.sleep(2 * 1000);

                                            if (!application.isBleConnected()) {
                                                continue;
                                            }
                                            if (isRightTime()) {
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
                                                // 提交数据
                                                apiMethodManager.saveUserHealthInfo(userHealthInfo, new IRequestCallback<SaveUserHealthInfoReturn>() {
                                                    @Override
                                                    public void onSuccess(SaveUserHealthInfoReturn result) {
                                                        MyLog.e(TAG, "==userHealthInfo=提交数据成功");
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable throwable) {
                                                        MyLog.e(TAG, "==userHealthInfo=提交数据失败" + throwable.getMessage());
                                                    }
                                                });
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                            break;
                        }
                    }
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    /**
     * 5.2、5.3、5.4、5.5 期间
     *
     * @return
     */
    private boolean isRightTime() {
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
