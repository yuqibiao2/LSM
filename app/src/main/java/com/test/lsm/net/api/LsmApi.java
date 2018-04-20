package com.test.lsm.net.api;

import com.test.lsm.bean.json.GetHealthInfoDtlReturn;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.test.lsm.bean.json.SaveRunRecordReturn;
import com.test.lsm.bean.json.SaveUserHealthInfoReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.json.UserRegReturn;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 功能：网络请求地址
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public interface LsmApi {


    @GET("getHealthInfoDtl")
    Observable<GetHealthInfoDtlReturn>  getHealthInfoDtl(@Query("id") String id);

    @FormUrlEncoded
    @POST("queryUserRunInfo")
    Observable<QueryUserRunInfoReturn> queryUserRunInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("saveRunRecord")
    Observable<SaveRunRecordReturn> saveRunRecord(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("saveUserHealthInfo")
    Observable<SaveUserHealthInfoReturn> saveUserHealthInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("appUserLogin")
    Observable<UserLoginReturn> login(@Field("USERNAME") String username , @Field("PASSWORD") String pwd);

    @FormUrlEncoded
    @POST("appUserReg")
    Observable<UserRegReturn> register(@FieldMap Map<String, String> map);


}
