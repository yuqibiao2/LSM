#include <jni.h>
#include <stdlib.h>
#include "heartrate.h"
#include "heartrate.cpp"

using namespace std;

extern "C" {
    HeartRate* heartRate;

    JNIEXPORT jint JNI_OnLoad(JavaVM* pVM, void* reserved) {
        JNIEnv *env;
        if (pVM->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
            abort();
        }

        heartRate = new HeartRate();

        return JNI_VERSION_1_6;
    }


    JNIEXPORT jint JNICALL
    Java_com_swm_engg_algorithm_heartrate_HeartRateNativeImpl_countRPeaks(JNIEnv *env, jobject instance, jdoubleArray ecg_, jint length) {
        /* 轉換 JNI double array 到 Java double array */
        jdouble *ecg = env->GetDoubleArrayElements(ecg_, NULL);

        double *rPeaks = heartRate->DetectRPeak(ecg, length);

        int hr = 0;
        for (int i = 0; i < length; i++) {
            if (rPeaks[i] != 0) {
                hr++;
            }
        }

        return hr;
    }

    JNIEXPORT jdoubleArray JNICALL
    Java_com_swm_engg_algorithm_heartrate_HeartRateNativeImpl_findRPeakSites(JNIEnv *env, jobject instance, jdoubleArray ecg_, jint length) {
        jdouble *ecg = env->GetDoubleArrayElements(ecg_, NULL);

        double *rPeaks = heartRate->DetectRPeak(ecg, length);

        jdoubleArray result = env->NewDoubleArray(length);
        env->SetDoubleArrayRegion(result, 0, length, rPeaks);
        return result;
    }
}
