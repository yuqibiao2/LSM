//
// Created by 楊鎮宇 on 2016/10/8.
//
#include <jni.h>
#include "SWM_ECG_ALGO.h"
extern "C" {
    JNIEXPORT int JNICALL
    Java_com_swm_core_HeartRateService_CalculateHeartRate(JNIEnv *env, jobject thiz, jintArray arr) {

        jint *data = env->GetIntArrayElements(arr, 0);
        short heartRate = APPS_ECG_SimulationRTOS(data);
        env->ReleaseIntArrayElements(arr, data, 0);
        return heartRate;
    }

    JNIEXPORT void JNICALL
    Java_com_swm_core_HeartRateService_GetRtoRIntervalData(JNIEnv *env, jobject thiz, jdoubleArray jrriCount, jdoubleArray jrriTime) {
        double *rriCount = new double[HRV_RRI_MAX_BUF];
        double *rriTime = new double[HRV_RRI_MAX_BUF];
        //APPS_ECG_InitialForModeChange(1);
        APPS_ECG_RRI_DATA(rriCount, rriTime);
        rriTime[0] = 1;
        rriTime[500] = 123;
        rriTime[600] = 1234;
        env->SetDoubleArrayRegion(jrriCount, 0, HRV_RRI_MAX_BUF, rriCount);
        env->SetDoubleArrayRegion(jrriTime, 0, HRV_RRI_MAX_BUF, rriTime);

        delete []rriCount;
        delete []rriTime;
    }

JNIEXPORT void JNICALL
Java_com_swm_core_HeartRateService_getCurrentRRI(JNIEnv *env, jclass type,
                                                 jshortArray currentRRI_) {

    short *temp = GET_CURRENT_RRI_BUF();
    env->SetShortArrayRegion(currentRRI_ , 0 , HRV_RRI_MAX_BUF , temp);

}


JNIEXPORT jdoubleArray JNICALL
Java_com_swm_core_HeartRateService_GetRtoRIntervalData2(JNIEnv *env, jclass type) {

    double *rriCount = new double[HRV_RRI_MAX_BUF];
    double *rriTime = new double[HRV_RRI_MAX_BUF];
    rriTime[0] = 1;
    rriTime[500] = 123;
    rriTime[600] = 1234;
    APPS_ECG_RRI_DATA(rriCount, rriTime);
//1.新建长度len数组
    jdoubleArray jarr = env->NewDoubleArray(HRV_RRI_MAX_BUF);
    //2.获取数组指针
    jdouble *arr = env->GetDoubleArrayElements(jarr, NULL);
    //3.赋值
    int i = 0;
    for(; i < HRV_RRI_MAX_BUF; i++){
        arr[i] = 123;
    }
    //4.释放资源
    env->ReleaseDoubleArrayElements(jarr, arr, 0);
    return jarr;
}

    JNIEXPORT jfloat JNICALL
    Java_com_swm_core_HeartRateService_GetRmssd(JNIEnv *env, jobject thiz) {
        float rmssd = APPS_ECG_GetRMSSD();
        return rmssd;
    }

    JNIEXPORT jfloat JNICALL
    Java_com_swm_core_HeartRateService_GetSdnn(JNIEnv *env, jobject thiz) {
        float rmssd = APPS_ECG_GetSDNN();
        return rmssd;
    }

    JNIEXPORT void JNICALL
    Java_com_swm_core_HeartRateService_InitialForModeChange(JNIEnv *env, jobject thiz, jint mode) {
        APPS_ECG_InitialForModeChange(mode);
    }
}