#ifndef ENGG_PEDOMETER_H
#define ENGG_PEDOMETER_H

class Pedometer
{

private:
    double old_fixed;
    double new_fixed;
    double fixed_result;
    int sample_rate;
    double time_interval_;
    double amplitude_limit_;
	
	double* data_buffer_x;
	double* data_buffer_y;
	double* data_buffer_z;
	int m;
	int j;
	int steps;
	
	int countStep(double* data, int length);
	double findTheMaxValue(double* data, int length);
	double findTheMinValue(double* data, int length);
    double abs(double val);

public:
	Pedometer(int sr, int buffer_size, int time_interval, int amplitude_limit);
	~Pedometer(void);

	int countStep(double gyroX, double gyroY, double gyroZ, double accX, double accY, double accZ, double magnX, double magnY, double magnZ);
};


#endif //ENGG_PEDOMETER_H
