#ifndef ENGG_SIGNAL_H
#define ENGG_SIGNAL_H


class SignalProcessor {

public:
    double* Smooth(double* data, int length, int order);
    double* MiddleFilter(double* data, int length, int order);
    double Mean(double* data, int length);
    void Max(int*output, int* data, int length);
    void Min(int*output, int* data, int length);
    double* Diff(double* data, int length); 
    double* CenterDiff(double* data, int length);
    double* Composite(double*data, int length);
    double AdaptiveThreshold(double* composite, int length, double ADscale, double upper, double lower);
};


#endif //ENGG_SIGNAL_H
