#ifndef ENGG_HEARTRATE_H
#define ENGG_HEARTRATE_H


#include "signal_processor.h"

class HeartRate {
private:
    SignalProcessor* signalProcessor;
    double  g_decThreshold = 0;
    double* RpeakDetection(double* fx, double* ECGdata, int length);

public:
    HeartRate();
    ~HeartRate();
    double* DetectRPeak(double* signa, int length);

};


#endif //ENGG_HEARTRATE_H
