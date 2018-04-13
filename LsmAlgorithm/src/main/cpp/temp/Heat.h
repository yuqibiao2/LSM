#pragma once
class Heat
{
private:
	int W;
	int A;
	bool is_girl;

public:
	Heat(int weight, int age, bool is_girl);
	~Heat(void);

	double countHeatCalories(int HR, double T); // 每 6 秒計算一次 T = 0.002
};

