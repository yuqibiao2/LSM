package com.test.lsm.net;

import android.text.TextUtils;

import com.test.lsm.bean.json.ConnectMonitorReturn;
import com.test.lsm.bean.vo.AFibExpRecordVo;
import com.test.lsm.bean.vo.GetHeartChart;
import com.test.lsm.bean.vo.GroupAttach;
import com.test.lsm.bean.vo.HealthRecordVo;
import com.test.lsm.bean.vo.QueryHRVInfo;
import com.test.lsm.bean.vo.QueryRunInfoVo;
import com.test.lsm.bean.vo.RunRecord;
import com.test.lsm.bean.vo.SaveCurrentHealthVo;
import com.test.lsm.bean.vo.SaveHeartByMinVo;
import com.test.lsm.bean.vo.SaveLocationVo;
import com.test.lsm.bean.vo.SaveUserHRVVo;
import com.test.lsm.bean.vo.UserCourseTimeVo;
import com.test.lsm.bean.vo.UserHealthInfo;
import com.test.lsm.bean.vo.UserJoinCourseVo;
import com.test.lsm.bean.vo.UserRegVo;
import com.test.lsm.bean.vo.UserUpdateVo;
import com.test.lsm.bean.json.DoFooBean;
import com.test.lsm.bean.json.EmptyDataReturn;
import com.test.lsm.bean.json.GetAFibExpRecordReturn;
import com.test.lsm.bean.json.GetActiveUser;
import com.test.lsm.bean.json.GetCoachByCourseType;
import com.test.lsm.bean.json.GetCourseParams;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.bean.json.GetHealthInfoDtlReturn;
import com.test.lsm.bean.json.GetHealthRecordReturn;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.bean.json.GetMonitorGroupMemDetailReturn;
import com.test.lsm.bean.json.GetMonitorGroupReturn;
import com.test.lsm.bean.json.GetUserMonitorsReturn;
import com.test.lsm.bean.json.GetMsgDetail;
import com.test.lsm.bean.json.GetMsgListReturn;
import com.test.lsm.bean.json.GetUserInfoReturn;
import com.test.lsm.bean.json.ModifyScoreReturn;
import com.test.lsm.bean.json.QueryActivityGoodsReturn;
import com.test.lsm.bean.json.QueryAmongReturn;
import com.test.lsm.bean.json.QueryUserRakingReturn;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.test.lsm.bean.json.SaveHeartByMin;
import com.test.lsm.bean.json.SaveRunRecordReturn;
import com.test.lsm.bean.json.SaveUserHRV;
import com.test.lsm.bean.json.SaveUserHealthInfoReturn;
import com.test.lsm.bean.json.UserCourseTimeReturn;
import com.test.lsm.bean.json.UserJoinCourseReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.json.UserRegReturn;
import com.test.lsm.net.api.LsmApi;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.Path;
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

    /**
     * 保存当前心跳、卡路里、步数
     *
     * @param healthVo
     * @param callback
     * @return
     */
    public Subscription saveCurrentHealthInfo(

                                              SaveCurrentHealthVo healthVo ,
                                            final IRequestCallback<EmptyDataReturn> callback){
        Subscription subscribe = lsmApi.saveCurrentHealthInfo(healthVo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EmptyDataReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(EmptyDataReturn emptyDataReturn) {
                        callback.onSuccess(emptyDataReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 保存当前经纬度
     *
     * @param provider
     * @param saveLocationVo
     * @param callback
     * @return
     */
    public Subscription saveCurrentLocation(LifecycleProvider<ActivityEvent> provider ,
                                            SaveLocationVo saveLocationVo ,
                                            final IRequestCallback<EmptyDataReturn> callback){
        Subscription subscribe = lsmApi.saveCurrentLocation(saveLocationVo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<EmptyDataReturn>bindToLifecycle())
                .subscribe(new Subscriber<EmptyDataReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(EmptyDataReturn emptyDataReturn) {
                        callback.onSuccess(emptyDataReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 连接监听人
     *
     */
    public Subscription connectMonitor(
                                       Integer userId,
                                       String monitorId,
                                       final IRequestCallback<ConnectMonitorReturn> callback) {

        Subscription subscribe = lsmApi.connectMonitor(userId, monitorId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ConnectMonitorReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(ConnectMonitorReturn connectMonitorReturn) {
                        callback.onSuccess(connectMonitorReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 修改监听状态
     *
     * @param provider
     * @param groupAttachList
     * @param callback
     * @return
     */
    public Subscription modifyGroupAttachStatus(LifecycleProvider<ActivityEvent> provider,
                                                List<GroupAttach> groupAttachList,
                                                final IRequestCallback<EmptyDataReturn> callback) {

        Subscription subscribe = lsmApi.modifyGroupAttachStatus(groupAttachList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<EmptyDataReturn>bindToLifecycle())
                .subscribe(new Subscriber<EmptyDataReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(EmptyDataReturn emptyDataReturn) {
                        callback.onSuccess(emptyDataReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 得到所有监听人
     *
     * @param provider
     * @param userId
     * @param callback
     * @return
     */
    public Subscription getMonitorsByUserId(LifecycleProvider<ActivityEvent> provider,
                                            Integer userId,
                                            final IRequestCallback<GetUserMonitorsReturn> callback) {
        Subscription subscribe = lsmApi.getMonitorsByUserId(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetUserMonitorsReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetUserMonitorsReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetUserMonitorsReturn getUserMonitorsReturn) {
                        callback.onSuccess(getUserMonitorsReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 得到群组成员的详细信息
     *
     * @param provider
     * @param userId
     * @param callback
     * @return
     */
    public Subscription getMonitorGroupMemDetail(LifecycleProvider<ActivityEvent> provider,
                                                 Integer userId,
                                                 final IRequestCallback<GetMonitorGroupMemDetailReturn> callback) {
        Subscription subscribe = lsmApi.getMonitorGroupMemDetail(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetMonitorGroupMemDetailReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetMonitorGroupMemDetailReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetMonitorGroupMemDetailReturn getMonitorGroupMemDetailReturn) {
                        callback.onSuccess(getMonitorGroupMemDetailReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 得到某一群组详情
     *
     * @param provider
     * @param groupId
     * @param callback
     * @return
     */
    public Subscription getMonitorGroupDetail(LifecycleProvider<ActivityEvent> provider,
                                              Long groupId,
                                              final IRequestCallback<GetMonitorGroupDetailReturn> callback) {
        Subscription subscribe = lsmApi.getMonitorGroupDetail(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetMonitorGroupDetailReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetMonitorGroupDetailReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetMonitorGroupDetailReturn getMonitorGroupDetailReturn) {
                        callback.onSuccess(getMonitorGroupDetailReturn);
                    }
                });
        return subscribe;
    }

    /**
     * 得到用户所创建的群组
     *
     * @param provider
     * @param userId
     * @param status   1：开启  0：已关闭
     * @param callback
     * @return
     */
    public Subscription getMonitorGroups(LifecycleProvider<ActivityEvent> provider,
                                         Integer userId,
                                         Integer status,
                                         final IRequestCallback<GetMonitorGroupReturn> callback) {
        Subscription subscribe = lsmApi.getMonitorGroups(userId, status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetMonitorGroupReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetMonitorGroupReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetMonitorGroupReturn getMonitorGroupReturn) {
                        callback.onSuccess(getMonitorGroupReturn);
                    }
                });
        return subscribe;
    }

    /**
     * 得到身体讯息记录
     *
     * @param provider
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param callback
     * @return
     */
    public Subscription getHealthRecords(LifecycleProvider<ActivityEvent> provider,
                                         Integer userId,
                                         Integer pageNum,
                                         Integer pageSize,
                                         final IRequestCallback<GetHealthRecordReturn> callback) {
        Subscription subscribe = lsmApi.getHealthRecords(userId, pageNum, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetHealthRecordReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetHealthRecordReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetHealthRecordReturn getHealthRecordReturn) {
                        callback.onSuccess(getHealthRecordReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 保存身体讯息
     *
     * @param provider
     * @param healthRecordVo
     * @return
     */
    public Subscription saveHealthRecords(LifecycleProvider<ActivityEvent> provider,
                                          HealthRecordVo healthRecordVo,
                                          final IRequestCallback<EmptyDataReturn> callback) {
        Subscription subscribe = lsmApi.saveHealthRecords(healthRecordVo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<EmptyDataReturn>bindToLifecycle())
                .subscribe(new Subscriber<EmptyDataReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(EmptyDataReturn emptyDataReturn) {
                        callback.onSuccess(emptyDataReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 得到AFib异常信息记录
     *
     * @param provider
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Subscription getAfibExpRecords(LifecycleProvider<ActivityEvent> provider,
                                          Integer userId,
                                          Integer pageNum,
                                          Integer pageSize,
                                          final IRequestCallback<GetAFibExpRecordReturn> callback) {
        Subscription subscribe = lsmApi.getAfibExpRecords(userId, pageNum, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetAFibExpRecordReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetAFibExpRecordReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetAFibExpRecordReturn getAFibExpRecordReturn) {
                        callback.onSuccess(getAFibExpRecordReturn);
                    }
                });
        return subscribe;
    }

    /**
     * 保存AFib异常信息
     *
     * @param provider
     * @param aFibExpRecordVo
     * @return
     */
    public Subscription saveAfibExpRecords(LifecycleProvider<ActivityEvent> provider,
                                           AFibExpRecordVo aFibExpRecordVo,
                                           final IRequestCallback<EmptyDataReturn> callback) {
        Subscription subscribe = lsmApi.saveAfibExpRecords(aFibExpRecordVo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<EmptyDataReturn>bindToLifecycle())
                .subscribe(new Subscriber<EmptyDataReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(EmptyDataReturn emptyDataReturn) {
                        callback.onSuccess(emptyDataReturn);
                    }
                });

        return subscribe;
    }


    /**
     * 查询当前用户得排名
     *
     * @param provider
     * @param userId
     * @param queryTime
     * @param callback
     * @return
     */
    public Subscription queryUserRankingByDate(LifecycleProvider<ActivityEvent> provider, Integer userId, String queryTime, final IRequestCallback<QueryUserRakingReturn> callback) {

        Subscription subscribe = lsmApi.queryUserRankingByDate(userId, queryTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<QueryUserRakingReturn>bindToLifecycle())
                .subscribe(new Subscriber<QueryUserRakingReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(QueryUserRakingReturn queryUserRakingReturn) {
                        callback.onSuccess(queryUserRakingReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 根据日期，查询用户分数排行
     *
     * @param provider
     * @param queryTime
     * @param callback
     * @return
     */
    public Subscription queryAmongByDate(LifecycleProvider<ActivityEvent> provider, String queryTime, final IRequestCallback<QueryAmongReturn> callback) {

        Subscription subscribe = lsmApi.queryAmongByDate(queryTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<QueryAmongReturn>bindToLifecycle())
                .subscribe(new Subscriber<QueryAmongReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(QueryAmongReturn queryAmongReturn) {
                        callback.onSuccess(queryAmongReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 每分钟修改用户活动分数
     *
     * @param provider
     * @param usId
     * @param scoreValue
     * @param callback
     * @return
     */
    public Subscription modifyUserScoreByMin(LifecycleProvider<ActivityEvent> provider, String usId, String scoreValue, final IRequestCallback<ModifyScoreReturn> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("US_ID", usId);
        map.put("SCORE_VALUE", scoreValue);
        Subscription subscribe = lsmApi.modifyUserScoreByMin(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<ModifyScoreReturn>bindToLifecycle())
                .subscribe(new Subscriber<ModifyScoreReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(ModifyScoreReturn modifyScoreReturn) {
                        callback.onSuccess(modifyScoreReturn);
                    }
                });

        return subscribe;

    }


    /**
     * 用户查询当期和下期奖品
     *
     * @param provider
     * @param callback
     * @return
     */
    public Subscription queryActivityGoods(LifecycleProvider<ActivityEvent> provider, final IRequestCallback<QueryActivityGoodsReturn> callback) {
        Subscription subscribe = lsmApi.queryActivityGoods()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<QueryActivityGoodsReturn>bindToLifecycle())
                .subscribe(new Subscriber<QueryActivityGoodsReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(QueryActivityGoodsReturn queryActivityGoodsReturn) {
                        callback.onSuccess(queryActivityGoodsReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 更新会员参加课程运动起止时间
     *
     * @param userCourseTimeVo
     * @param callback
     * @return
     */
    public Subscription userCourseTime(UserCourseTimeVo userCourseTimeVo, final IRequestCallback<UserCourseTimeReturn> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("UC_ID", "" + userCourseTimeVo.getUC_ID());
        map.put("START_TIME", "" + userCourseTimeVo.getSTART_TIME());
        map.put("END_TIME", "" + userCourseTimeVo.getEND_TIME());
        Subscription subscribe = lsmApi.userCourseTime(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserCourseTimeReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(UserCourseTimeReturn userCourseTimeReturn) {
                        callback.onSuccess(userCourseTimeReturn);
                    }
                });

        return subscribe;
    }


    /**
     * 会员加入教练课程
     *
     * @param userJoinCourseVo
     * @param callback
     * @return
     */
    public Subscription userJoinCourse(UserJoinCourseVo userJoinCourseVo, final IRequestCallback<UserJoinCourseReturn> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("USER_ID", "" + userJoinCourseVo.getUSER_ID());
        map.put("COURSE_TYPE", "" + userJoinCourseVo.getCOURSE_TYPE());
        map.put("COURSE_LEVEL", "" + userJoinCourseVo.getCOURSE_LEVEL());
        map.put("COACH_ID", "" + userJoinCourseVo.getCOACH_ID());
        map.put("CC_TYPE", "" + userJoinCourseVo.getCC_TYPE());
        Subscription subscribe = lsmApi.userJsonCourse(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserJoinCourseReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(UserJoinCourseReturn userJoinCourseReturn) {
                        callback.onSuccess(userJoinCourseReturn);
                    }
                });

        return subscribe;
    }

    /**
     * 室内运动 心跳参照数据
     *
     * @param courseType
     * @param courseLevel
     * @param coachId
     * @param ccType
     * @param callback
     * @return
     */
    public Subscription getCourseParamByType(String courseType, Integer courseLevel, Integer coachId, Integer ccType, final IRequestCallback<GetCourseParams> callback) {

        Subscription subscribe = lsmApi.getCourseParamsByType(courseType, courseLevel, coachId, ccType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<GetCourseParams>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetCourseParams getCourseParams) {
                        callback.onSuccess(getCourseParams);
                    }
                });

        return subscribe;
    }

    /**
     * 获取心率记录信息
     *
     * @param userId
     * @param type     0：按时；1：按日；2：按周；3：按月
     * @param dateTime 2018-07-18 15:10
     * @return
     */
    public Subscription getHeartChart(Integer userId, Integer type, String dateTime, final IRequestCallback<GetHeartChart> callback) {
        Subscription subscribe = lsmApi.getHeartChart(userId, type, dateTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<GetHeartChart>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetHeartChart getHeartChart) {
                        callback.onSuccess(getHeartChart);
                    }
                });
        return subscribe;
    }

    /**
     * 获取训练课程
     *
     * @param callback
     * @return
     */
    public Subscription getCoachByCourseType(final IRequestCallback<GetCoachByCourseType> callback) {

        Subscription subscribe = lsmApi.getCoachByCourseType()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<GetCoachByCourseType>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetCoachByCourseType getCoachByCourseType) {
                        callback.onSuccess(getCoachByCourseType);
                    }
                });

        return subscribe;
    }

    /**
     * 删除跑步记录
     *
     * @param recordId
     * @param callback
     * @return
     */
    public Subscription deleteRunRecordById(Integer recordId, final IRequestCallback<DoFooBean> callback) {

        Subscription subscribe = lsmApi.delRunRecord(recordId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DoFooBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(DoFooBean doFooBean) {
                        callback.onSuccess(doFooBean);
                    }
                });

        return subscribe;
    }

    /**
     * 保存HRV的值
     *
     * @param saveUserHRVVo
     * @param callback
     * @return
     */
    public Subscription saveUserHRV(SaveUserHRVVo saveUserHRVVo, final IRequestCallback<SaveUserHRV> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + saveUserHRVVo.getUserId());
        map.put("MINDFITNESS", "" + saveUserHRVVo.getMINDFITNESS());
        map.put("BODYFITNESS", "" + saveUserHRVVo.getBODYFITNESS());
        map.put("MOODSTABILITY", "" + saveUserHRVVo.getMOODSTABILITY());
        map.put("STRESSTENSION", "" + saveUserHRVVo.getSTRESSTENSION());
        map.put("MINDFATIGUE", "" + saveUserHRVVo.getMINDFATIGUE());
        map.put("BODYFATIGUE", "" + saveUserHRVVo.getBODYFATIGUE());
        map.put("currentTime", "" + MyTimeUtils.getCurrentDateTime());


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
    public Subscription saveHeartByMin(SaveHeartByMinVo saveHeartByMinVo, final IRequestCallback<SaveHeartByMin> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", "" + saveHeartByMinVo.getUserId());
        map.put("heartNum", "" + saveHeartByMinVo.getHeartNum());
        map.put("calorieValue", "" + saveHeartByMinVo.getCalorieValue());
        map.put("stepNum", "" + saveHeartByMinVo.getStepNum());
        map.put("currentTime", "" + saveHeartByMinVo.getCurrentTime());

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
    public Subscription getActiveUserList(final IRequestCallback<GetActiveUser> callback) {

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
    public Subscription getMsgList(LifecycleProvider<ActivityEvent> provider, Integer userId, Integer page, Integer pageSize, final IRequestCallback<GetMsgListReturn> callback) {
        Subscription subscribe = lsmApi.getMsgList(userId, page, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(provider.<GetMsgListReturn>bindToLifecycle())
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
        paras.put("avgHeart", runRecord.getAvgHeart());
        paras.put("maxHeart", runRecord.getMaxHeart());
        paras.put("calorieValue", runRecord.getCalorieValue());
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
     * 用户修改
     *
     * @param userRegVo
     * @param callback
     * @return
     */
    public Subscription updateUser(UserUpdateVo userRegVo, final IRequestCallback<UserRegReturn> callback) {

        Map<String, String> paras = new HashMap<>();
        paras.put("USER_ID", "" + userRegVo.getUSER_ID());
        paras.put("USERNAME", userRegVo.getUSERNAME());
        paras.put("PASSWORD", userRegVo.getPASSWORD());
        paras.put("PHONE", userRegVo.getPHONE());
        paras.put("USER_WEIGHT", userRegVo.getUSER_WEIGHT());
        paras.put("USER_HEIGHT", userRegVo.getUSER_HEIGHT());
        paras.put("USER_SEX", userRegVo.getUSER_SEX());
        paras.put("BIRTHDAY", userRegVo.getBIRTHDAY());
        paras.put("URGENT_USER", userRegVo.getURGENT_USER());
        paras.put("URGENT_PHONE", userRegVo.getURGENT_PHONE());
        String userImage = userRegVo.getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)) {
            paras.put("USER_IMAGE", userImage);
        }
        paras.put("HEALTH_PARAM", userRegVo.getHEALTH_PARAM());

        Subscription subscribe = lsmApi.updateUser(paras)
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
        String userImage = userRegVo.getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)) {
            paras.put("USER_IMAGE", userImage);
        }
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
     * 根据用户名（手机号）获取用户信息
     *
     * @param provider
     * @param username 手机号
     * @param callback
     * @return
     */
    public Subscription getUserInfoByUsername(LifecycleProvider<ActivityEvent> provider, String username, final IRequestCallback<GetUserInfoReturn> callback) {

        Subscription subscribe = lsmApi.getAppuserByUm(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(provider.<GetUserInfoReturn>bindToLifecycle())
                .subscribe(new Subscriber<GetUserInfoReturn>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onNext(GetUserInfoReturn getUserInfoReturn) {
                        callback.onSuccess(getUserInfoReturn);
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
