package com.test.lsm.net;

import com.test.lsm.bean.form.QueryHRVInfo;
import com.test.lsm.bean.form.QueryRunInfoVo;
import com.test.lsm.bean.form.RunRecord;
import com.test.lsm.bean.form.SaveHeartByMinVo;
import com.test.lsm.bean.form.SaveUserHRVVo;
import com.test.lsm.bean.form.UserHealthInfo;
import com.test.lsm.bean.form.UserRegVo;
import com.test.lsm.bean.json.GetActiveUser;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.bean.json.GetHealthInfoDtlReturn;
import com.test.lsm.bean.json.GetMsgDetail;
import com.test.lsm.bean.json.GetMsgListReturn;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.test.lsm.bean.json.SaveHeartByMin;
import com.test.lsm.bean.json.SaveRunRecordReturn;
import com.test.lsm.bean.json.SaveUserHRV;
import com.test.lsm.bean.json.SaveUserHealthInfoReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.json.UserRegReturn;
import com.test.lsm.net.api.LsmApi;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.Subscribe;
import retrofit2.http.Field;
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
    private final LsmApi lsmApiWithoutBaseURL;

    private APIMethodManager() {
        APIFactory apiFactory = APIFactory.getInstance();
        lsmApi = apiFactory.createLsmApi();
        lsmApiWithoutBaseURL = apiFactory.createLsmApiWithoutBaseURL();
    }

    private static class SingletonHolder {
        private static final APIMethodManager INSTANCE = new APIMethodManager();
    }

    public static APIMethodManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public Subscription saveUserHRV(SaveUserHRVVo saveUserHRVVo , final IRequestCallback<SaveUserHRV> callback){

        Map<String , String> map = new HashMap<>();
        map.put("userId" , ""+saveUserHRVVo.getUserId());
        map.put("MINDFITNESS" , ""+saveUserHRVVo.getMINDFITNESS());
        map.put("BODYFITNESS" , ""+saveUserHRVVo.getBODYFITNESS());
        map.put("MOODSTABILITY" , ""+saveUserHRVVo.getMOODSTABILITY());
        map.put("STRESSTENSION" , ""+saveUserHRVVo.getSTRESSTENSION());
        map.put("MINDFATIGUE" , ""+saveUserHRVVo.getMINDFATIGUE());
        map.put("BODYFATIGUE" , ""+saveUserHRVVo.getBODYFATIGUE());
        map.put("currentTime" , ""+ MyTimeUtils.getCurrentDateTime());


        Subscription subscribe = lsmApi.saveUserHRV(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SaveUserHRV>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(SaveUserHRV saveUserHRV) {
                        callback.onSuccess(saveUserHRV);
                    }
                });

        return subscribe;
    }



    /**
     * 一分钟上传一次身体信息
     *
     * @param saveHeartByMinVo
     * @param callback
     * @return
     */
    public Subscription saveHeartByMin(SaveHeartByMinVo saveHeartByMinVo , final IRequestCallback<SaveHeartByMin> callback){

        Map<String , String> map = new HashMap<>();
        map.put("userId" , ""+saveHeartByMinVo.getUserId());
        map.put("heartNum" , ""+saveHeartByMinVo.getHeartNum());
        map.put("calorieValue" , ""+saveHeartByMinVo.getCalorieValue());
        map.put("stepNum" , ""+saveHeartByMinVo.getStepNum());
        map.put("currentTime" , ""+saveHeartByMinVo.getCurrentTime());

        Subscription subscribe = lsmApi.saveHeartByMin(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SaveHeartByMin>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(SaveHeartByMin saveHeartByMin) {
                        callback.onSuccess(saveHeartByMin);
                    }
                });

        return subscribe;
    }


    /**
     * 得到需要特别上传数据的用户
     *
     * @param callback
     * @return
     */
    public Subscription getActiveUserList(final IRequestCallback<GetActiveUser> callback){

        Subscription subscribe = lsmApi.getGetActiveUserList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GetActiveUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetActiveUser getActiveUser) {
                        callback.onSuccess(getActiveUser);
                    }
                });

        return subscribe;
    }

    /**
     * 得到消息详情
     *
     * @param id
     * @param callback
     * @return
     */
    public Subscription getMsgDetail(Integer id, final IRequestCallback<GetMsgDetail> callback) {
        Subscription subscribe = lsmApi.getMsgDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GetMsgDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetMsgDetail getMsgDetail) {
                        callback.onSuccess(getMsgDetail);
                    }
                });

        return subscribe;
    }

    /**
     * 根据用户Id得到历史推送消息
     *
     * @param userId
     * @param page
     * @param pageSize
     * @param callback
     * @return
     */
    public Subscription getMsgList(Integer userId, Integer page, Integer pageSize, final IRequestCallback<GetMsgListReturn> callback) {
        Subscription subscribe = lsmApi.getMsgList(userId, page, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GetMsgListReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetMsgListReturn getMsgListReturn) {
                        callback.onSuccess(getMsgListReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 得到HRV值
     *
     * @param hrvInfo
     * @param callback
     * @return
     */
    public Subscription getHRVInfo(QueryHRVInfo hrvInfo, final IRequestCallback<GetHRVInfoReturn> callback) {
        Map<String, String> paras = new HashMap<>();
        paras.put("serverId", hrvInfo.getServerId());
        paras.put("deviceMacAddress", hrvInfo.getDeviceMacAddress());
        paras.put("gender", hrvInfo.getGender());
        paras.put("age", "" + hrvInfo.getAge());
        paras.put("height", "" + hrvInfo.getHeight());
        paras.put("weight", "" + hrvInfo.getWeight());
        paras.put("rrInterval", hrvInfo.getRrInterval());
        paras.put("resIndex", hrvInfo.getResIndex());
        paras.put("measureTime", hrvInfo.getMeasureTime());
        paras.put("account", hrvInfo.getAccount());
        paras.put("token", hrvInfo.getToken());
        Subscription subscribe = lsmApiWithoutBaseURL
                .getHRVInfo(paras)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GetHRVInfoReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetHRVInfoReturn getHRVInfoReturn) {
                        callback.onSuccess(getHRVInfoReturn);
                    }
                });
        return subscribe;
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
        paras.put("rawData", "" + userHealthInfo.getRawData());
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
