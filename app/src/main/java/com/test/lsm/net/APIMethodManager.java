package com.test.lsm.net;

import com.test.lsm.bean.form.QueryRunInfoVo;
import com.test.lsm.bean.form.RunRecord;
import com.test.lsm.bean.form.UserHealthInfo;
import com.test.lsm.bean.form.UserRegVo;
import com.test.lsm.bean.json.GetHealthInfoDtlReturn;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.test.lsm.bean.json.SaveRunRecordReturn;
import com.test.lsm.bean.json.SaveUserHealthInfoReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.json.UserRegReturn;
import com.test.lsm.net.api.LsmApi;

import java.util.HashMap;
import java.util.Map;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class APIMethodManager {

    private LsmApi lsmApi;

    private APIMethodManager() {
        APIFactory apiFactory = APIFactory.getInstance();
        lsmApi = apiFactory.createLsmApi();
    }

    private static class SingletonHolder {
        private static final APIMethodManager INSTANCE = new APIMethodManager();
    }

    public static APIMethodManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 得到跑步记录详情
     *
     * @param id
     * @param callback
     * @return
     */
    public Subscription getHealthInfoDtl(String id, final IRequestCallback<GetHealthInfoDtlReturn> callback) {
        Subscription subscribe = lsmApi.getHealthInfoDtl(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetHealthInfoDtlReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetHealthInfoDtlReturn getHealthInfoDtlReturn) {
                        callback.onSuccess(getHealthInfoDtlReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 得到用户的跑步记录
     *
     * @param queryRunInfoVo
     * @param callback
     * @return
     */
    public Subscription queryUserRunInfo(QueryRunInfoVo queryRunInfoVo, final IRequestCallback<QueryUserRunInfoReturn> callback) {
        Map<String, String> paras = new HashMap<>();
        paras.put("userId", "" + queryRunInfoVo.getUserId());
        paras.put("page", "" + queryRunInfoVo.getPage());
        paras.put("pageSize", "" + queryRunInfoVo.getPageSize());
        final Subscription subscribe = lsmApi.queryUserRunInfo(paras)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QueryUserRunInfoReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(QueryUserRunInfoReturn queryUserRunInfoReturn) {
                        callback.onSuccess(queryUserRunInfoReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 保存跑步记录
     *
     * @param runRecord
     * @param callback
     * @return
     */
    public Subscription saveRunRecord(RunRecord runRecord, final IRequestCallback<SaveRunRecordReturn> callback) {
        Map<String, String> paras = new HashMap<>();
        paras.put("userId", "" + runRecord.getUserId());
        paras.put("startTime", "" + runRecord.getStartTime());
        paras.put("stopTime", "" + runRecord.getStopTime());
        paras.put("coordinateInfo", "" + runRecord.getCoordinateInfo());
        paras.put("distance", "" + runRecord.getDistance());
        paras.put("runTime", "" + runRecord.getRunTime());
        final Subscription subscribe = lsmApi.saveRunRecord(paras)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveRunRecordReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(SaveRunRecordReturn saveUserHealthInfoReturn) {
                        callback.onSuccess(saveUserHealthInfoReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 保存心跳、卡路里、步数信息
     *
     * @param userHealthInfo
     * @param callback
     * @return
     */
    public Subscription saveUserHealthInfo(UserHealthInfo userHealthInfo, final IRequestCallback<SaveUserHealthInfoReturn> callback) {
        Map<String, String> paras = new HashMap<>();
        paras.put("userId", "" + userHealthInfo.getUserId());
        paras.put("heartNum", "" + userHealthInfo.getHeartNum());
        paras.put("calorieValue", "" + userHealthInfo.getCalorieValue());
        paras.put("stepNum", "" + userHealthInfo.getStepNum());
        final Subscription subscribe = lsmApi.saveUserHealthInfo(paras)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SaveUserHealthInfoReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(SaveUserHealthInfoReturn saveUserHealthInfoReturn) {
                        callback.onSuccess(saveUserHealthInfoReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 用户注册
     *
     * @param userRegVo
     * @param callback
     * @return
     */
    public Subscription register(UserRegVo userRegVo, final IRequestCallback<UserRegReturn> callback) {

        Map<String, String> paras = new HashMap<>();
        paras.put("USERNAME", userRegVo.getUSERNAME());
        paras.put("PASSWORD", userRegVo.getPASSWORD());
        paras.put("PHONE", userRegVo.getPHONE());
        paras.put("USER_WEIGHT", userRegVo.getUSER_WEIGHT());
        paras.put("USER_HEIGHT", userRegVo.getUSER_HEIGHT());
        paras.put("USER_SEX", userRegVo.getUSER_SEX());
        paras.put("BIRTHDAY", userRegVo.getBIRTHDAY());
        paras.put("URGENT_USER", userRegVo.getURGENT_USER());
        paras.put("URGENT_PHONE", userRegVo.getURGENT_PHONE());

        Subscription subscribe = lsmApi.register(paras)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserRegReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(UserRegReturn userRegReturn) {
                        callback.onSuccess(userRegReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param callback
     * @return
     */
    public Subscription login(String username, String password, final IRequestCallback<UserLoginReturn> callback) {

        Subscription subscribe = lsmApi.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserLoginReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(UserLoginReturn userLoginReturn) {
                        callback.onSuccess(userLoginReturn);
                    }
                });

        return subscribe;
    }


}
