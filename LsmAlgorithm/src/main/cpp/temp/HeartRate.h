#ifndef ENGG_HEARTRATE_H
#define ENGG_HEARTRATE_H

#include "signal_processor.h"

class HeartRate {
private:
	int m;
	int j;
	double* signal;


    SignalProcessor* signalProcessor;
    double  g_decThreshold;
    double* RpeakDetection(double* fx, double* ECGdata, int length);



public:
    HeartRate(int sr, int buffer_size);
    ~HeartRate();
    double* DetectRPeak(double* signa, int length);
	int countHeartRate(double data);

public:
	double* signal1;

public:
    int DetectRPeakWrapper(double *signal, int length);

public:
      short SAMPLE_RATE;
public:
       short ONE_MINUTE;
public:
    int bufferIndex;


public:
    int countHeartRateWrapper(short *ecg, int length);

};


#endif //ENGG_HEARTRATE_H
