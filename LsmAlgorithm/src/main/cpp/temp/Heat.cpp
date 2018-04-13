#include "Heat.h"


Heat::Heat(int weight, int age, bool is_girl)
{
	W = weight;
	A = age;
	this->is_girl = is_girl;
}


Heat::~Heat(void)
{
}


double Heat::countHeatCalories(int HR, double T) 
{
	//¨k:((-55.0969+(0.6309 x HR) + (0.1988 x W) + (0.2017 x A)) / 4.184) x 60 x T(hr)
	//¤k:((-20.4022+(0.4472 x HR) - (0.1236 x W) + (0.074 x A)) / 4.184) x 60 x T(hr)
	double out_calories = 0.0;
	if (is_girl) //man
	{
		out_calories = ((-20.4022 + (0.4472 *HR) - (0.1236 * W) + (0.074 * A)) / 4.184) * 60 * T;
	}
	else {
		out_calories = ((-55.0969 + (0.6309 * HR) + (0.1988 * W) + (0.2017 * A)) / 4.184) * 60 * T;
	}
	return out_calories;
}