#include <jni.h>
#include <string>
#include "temp/heartrate.h"
#include "temp/heartrate.cpp"

extern "C" {
HeartRate *heartRate;
JNIEXPORT jint JNI_OnLoad(JavaVM *pVM, void *reserved) {
    JNIEnv *env;
    if (pVM->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        abort();
    }
    heartRate = new HeartRate(250, 500);
    return JNI_VERSION_1_6;
}


JNIEXPORT jstring JNICALL
Java_com_yyyu_lsmalgorithm_MyLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jint JNICALL
Java_com_yyyu_lsmalgorithm_MyLib_countRPeaks(JNIEnv *env, jobject instance, jdoubleArray ecg_,
                                             jint length) {
    jdouble *ecg = env->GetDoubleArrayElements(ecg_, NULL);
    double *rPeaks = heartRate->DetectRPeak(ecg, length);
    int hr = 0;
    for (int i = 0; i < length; i++) {
        if (rPeaks[i] != 0) {
            hr++;
        }
    }
    env->ReleaseDoubleArrayElements(ecg_, ecg, 0);
    return hr;
}

JNIEXPORT jint JNICALL
Java_com_yyyu_lsmalgorithm_MyLib_countHeartRate(JNIEnv *env, jclass type, jdouble data) {
    int result = heartRate->countHeartRate(data);
    return result;
}

}
