#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <string.h>
#include "HeartRate.h"
#include "signal_processor.h"
#include "signal_processor.cpp"

using namespace std;

HeartRate::HeartRate(int sr, int buffer_size) {
	m = buffer_size;
	j = 0;

	signal = new double[m];
	g_decThreshold = 0;
    signalProcessor = new SignalProcessor();
}

HeartRate::~HeartRate() {
    free(signalProcessor);
    signalProcessor = NULL;
}

int HeartRate::countHeartRate(double data)
{
	if((j+1)%(m+1) == 0) {	
		double * result = DetectRPeak(signal, m);

		int Heartbeat = 0;
		for (int k = 0; k < m; k++) {
					
			if (result[k] > 0) { // ???>0?h?????????I?A?G?p??h??????0???????????
				//Heartbeat++;
				Heartbeat = result[k];
			}
		}
		cout << "Heartbeat : "<<Heartbeat << " ";

		signal = new double[m];
		j = 0;

		return Heartbeat * 30; // 2????*30=?C??????v
	}
	signal[j++] = data;

	return -1;
}

double *HeartRate::DetectRPeak(double *signal, int length) {

	double *Crawdata = new double[length];
	double* QRStrack = new double[length];
	double *Adpative_threshold = new double[length];
	double *Ccomposite = new double[length];

	Crawdata = signalProcessor->MiddleFilter(signal, length, 100);// baseline correct ,
	//Crawdata = Smooth(buffer, RAsize, 3, 0);
	//buffer = MiddleFilter(input, RAsize, 100, 0);// baseline correct ,

	Ccomposite = signalProcessor->Composite(Crawdata, length, 0);
	//Ccomposite=Smooth( Ccomposite,  length, 10, 0);
	for (int i = 0; i < length; i++)//Composite
	{
		Adpative_threshold[i] = Ccomposite[i];
	}
//----------------Adative Threshold-----------------------------
	g_decThreshold = signalProcessor->AdaptiveThreshold(Ccomposite, length, 0.1, 0.2, 0.8);
//---------------------------------------------------------------
	for (int i = 0; i < length; i++) {
		if (Adpative_threshold[i] >= g_decThreshold)QRStrack[i] = Crawdata[i];
		else QRStrack[i] = 0;
	}
	QRStrack = signalProcessor->Smooth(QRStrack, length, 5);
	for (int i = 0; i < length; i++) {
		if (QRStrack[i] < 0)QRStrack[i] = 0;
	}
//    memcpy((void *) mxGetPr(output2), (void *) QRStrack, length * sizeof(double)); // output setup
	return RpeakDetection(QRStrack, Crawdata, length);
}

double *HeartRate::RpeakDetection(double *fx, double *ECGdata, int length) {
	// input1: fx : QRS complex wave
	// input2: ECGdata
	// length :  ECGdata's length
	// output : Rpeak  (value>0)
	int const pointsize = 20;
	double* output = new double[length];
	memset(output, 0, sizeof(double)*length);
	double* buffer = new double[length];
	double* buffer2 = new double[length];
	int Spoint[pointsize] = { -200 };
	int Epoint[pointsize] = { -200 };
	int QRSinterval[pointsize] = { 0 };
	int intervalThreshold = 5;//

	for (int i = 0; i < length; i++)
	{
		if (fx[i] <= 0)fx[i] = 0;
		else fx[i] = 1;
	}

	buffer = signalProcessor->Diff(fx, length, 0);
	int flage = 0;
	int Sindex = -1;
	int Eindex = -1;
	int interval_index = -1;
	int Rindex = -1;
	for (int i = 0; i < length; i++) // find QRS interval (1 -1)
	{
		if (flage == 0 && buffer[i] == 1)
		{
			Sindex++;
			Spoint[Sindex] = i;
			flage = 1;
		}
		else if (flage == 1 && buffer[i] == -1)
		{
			Eindex++;
			Epoint[Eindex] = i;
			interval_index++;
			QRSinterval[interval_index] = Epoint[Eindex] - Spoint[Sindex];
			if (QRSinterval[interval_index] >= intervalThreshold) // find the R peak between  QRS interval
			{
				double MaxPeak = -10000000000000;
				int ans=-1;
				for (int j = Spoint[Sindex] - 1; j <= Epoint[Eindex] + 1; j++)
				{
					if (ECGdata[j - 1] < ECGdata[j] && ECGdata[j + 1] < ECGdata[j] && ECGdata[j]>MaxPeak)
					{
						MaxPeak = ECGdata[j];
						ans = j;
					}
				}
//                mexPrintf("R ans= %i ", ans);
				if(ans>=0) output[ans] = ECGdata[ans]; // R peak 才有值，非R peak 或找不到 peak  "ans <0" ;
			}
			flage = 0;
		}
	}
	free(buffer);
	return output;// Rpeak
}
