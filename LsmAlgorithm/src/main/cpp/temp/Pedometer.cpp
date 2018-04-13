#include "Pedometer.h"


Pedometer::Pedometer(int sr, int buffer_size, int time_interval, int amplitude_limit)
{
    sample_rate = sr;
	m = buffer_size;
	time_interval_ = sample_rate * time_interval; // 5
	amplitude_limit_ = amplitude_limit;

	j = 0;
	steps = 0;
    old_fixed = 0;
    new_fixed = 0;
    fixed_result = 0;
	
	data_buffer_x = new double[m];
	data_buffer_y = new double[m];
	data_buffer_z = new double[m];
}


Pedometer::~Pedometer(void)
{
}

int Pedometer::countStep(double gyroX, double gyroY, double gyroZ, double accX, double accY, double accZ, double magnX, double magnY, double magnZ)
{
	if((j+1)%(m+1) == 0) {
		steps += this->countStep(data_buffer_y, m);


		data_buffer_x = new double[m];
		data_buffer_y = new double[m];
		data_buffer_z = new double[m];

		j = 0;

		return steps;
	}

	data_buffer_x[j] = accX;
	data_buffer_y[j] = accY;
	data_buffer_z[j] = accZ;

	j++;

	return -1;
}

int Pedometer::countStep(double* data, int length)
{
    const double max = this->findTheMaxValue(data, length);
    const double min = this->findTheMinValue(data, length);
    const double dynamic_threshold = (max + min) / 2;
    const double different = (max - min);

    int* indexes = new int[length];
    double* sites = new double[length];
    for (int i = 0; i < length; i++) {
        sites[i] = -9000;
    }

    int steps = 0;

    for(int i = 0; i < length; i++) {
        this->old_fixed = this->new_fixed;
        this->new_fixed = this->fixed_result;

        /**
         * max - min > 0.25
         * window = [0.2, 2]; run:5 step/sec, walk: 1 step/2sec
         * */
        if((old_fixed > dynamic_threshold) && (dynamic_threshold > new_fixed) &&
           abs(i - indexes[steps]) > time_interval_ && abs(different) > amplitude_limit_) {
            indexes[(steps+1)] = i;
            sites[i] = dynamic_threshold;
            steps++;
        }

        fixed_result = data[i];
    }

	return steps;
}

double Pedometer::findTheMaxValue(double* data, int length) {
    double max = data[0];
    for (int i = 1; i < length; i++) {
        if (data[i] > max) {
            max = data[i];
        }
    }
    return max;
}

double Pedometer::findTheMinValue(double *data, int length) {
    double min = data[0];
    for (int i = 1; i < length; i++) {
        if (data[i] < min) {
            min = data[i];
        }
    }
    return min;
}

double Pedometer::abs(double _a) {
    return _a < 0 ? -_a : _a;
}

