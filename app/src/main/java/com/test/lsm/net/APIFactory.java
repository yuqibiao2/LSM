package com.test.lsm.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.test.lsm.net.api.LsmApi;
import com.test.lsm.utils.gson.DoubleDefault0Adapter;
import com.test.lsm.utils.gson.IntegerDefault0Adapter;
import com.test.lsm.utils.gson.LongDefault0Adapter;
import com.yyyu.baselibrary.utils.MyLog;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 功能：retrofit 网络请求创建
 *
 * @author yu
 * @version 1.0
 * @date 2017/8/10
 */

public class APIFactory {

    //云端测试库
    public static final String BASE_URL = "http://101.132.125.217:8080/test/";

    private static final int DEFAULT_TIMEOUT = 1;

    private Retrofit.Builder builder;


    private APIFactory() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                MyLog.i("APIFactory: OkHttpClient", "OkHttpMessage:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        OkHttpClient.Builder httpClientBuild = new OkHttpClient.Builder();
        httpClientBuild.addInterceptor(loggingInterceptor);
        httpClientBuild.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        httpClientBuild.readTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        httpClientBuild.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                })
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter());
        Gson gson = gsonBuilder.create();
        builder = new Retrofit.Builder()
                .client(httpClientBuild.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    private static class SingletonHolder {
        private static final APIFactory INSTANCE = new APIFactory();
    }

    public static APIFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public LsmApi createLsmApi() {
        Retrofit retrofit = builder.baseUrl(BASE_URL).build();
        LsmApi lsmApi = retrofit.create(LsmApi.class);
        return lsmApi;
    }


}
