
/*
 * authors: Moriarty, Zhen, Gaduo, Rouyi
 * There are some functions about Heart Rate Computing, Heat Computing, Pedometer.
 */


#include <iostream>
#include <fstream>
#include "Loader.h"
#include "HeartRate.h"
#include "Pedometer.h"
#include "Heat.h"

using namespace std;

void heart_rate();
void count_steps();
void count_heat();

int main(void) {
	heart_rate(); // 計算心率範例
	// count_heat(); // 計算熱量範例，請解開後執行 
	// count_steps(); // 計算步數範例，請解開後執行 
	system("Pause");
	return 0;
}

void heart_rate()
{
	/* ==================== 心率演算法 ==================== */
    HeartRate* heart_rate = new HeartRate(250, 500); // 取樣頻率 與 處裡的資料
	/* ==================== 心率演算法 ==================== */
	

	string filename = "../dataset/ECG/System-ECG-20180323-112904.dat";
	Loader loader;
	int buffer_size = 2;
	fstream file;

	file.open(filename, ios::in | ios::binary);

	if(file.is_open()) {

		file.seekg(0, ios::end);
		streamoff file_size = file.tellg();

		file.seekg(0, ios::beg);
		for(int i = 0; i < (file_size/buffer_size); i++) {
			char* buffer = new char[buffer_size];
			file.read(buffer, buffer_size);

			// 每兩個 byte 合併成 一個 short Value
			short* data = loader.convertByteToShortArrayForECG(buffer, 1);
			
			/* ==================== 心率演算法 ==================== */
			int hr = heart_rate->countHeartRate(data[0]);
			if (hr != -1)
			{
				cout << ", Heart Rate: " << hr << "\n";
			}
			/* ==================== 心率演算法 ==================== */

			file.seekg(0, ios::cur);
		}
	}
}

void count_heat() 
{
	/* ==================== 心率演算法 ==================== */
    HeartRate* heart_rate = new HeartRate(250, 500);
	/* ==================== 心率演算法 ==================== */
	
	/* ====================計算熱量演算法 ==================== */
	int w = 50; //預設值
	int age = 25;//預設值
	bool sex = false;//預設值
	Heat* heat = new Heat(w, age, sex);
	/* ====================計算熱量演算法 ==================== */
	
	string filename = "../dataset/ECG/System-ECG-20180323-112904.dat";
	Loader loader;
	int buffer_size = 2;
	fstream file;

	file.open(filename, ios::in | ios::binary);

	if(file.is_open()) {

		file.seekg(0, ios::end);
		streamoff file_size = file.tellg();

		file.seekg(0, ios::beg);
		for(int i = 0; i < (file_size/buffer_size); i++) {
			char* buffer = new char[buffer_size];
			file.read(buffer, buffer_size);

			// 每兩個 byte 合併成 一個 short Value
			short* data = loader.convertByteToShortArrayForECG(buffer, 1);
			
			/* ==================== 心率演算法 ==================== */
			int hr = heart_rate->countHeartRate(data[0]);
			/* ==================== 心率演算法 ==================== */
			
			/* ==================== 計算熱量演算法 ==================== */
			
			if (hr != -1)
			{
				double t = 0.00056;
				double calories = heat->countHeatCalories(hr, t);
				//calories = calories +heat->countHeatCalories(hr, t); // 熱量應為計算後累加
				cout << "Calories: "<<calories << "\n";
			}
			/* ==================== 計算熱量演算法==================== */

			file.seekg(0, ios::cur);
		}
	}
}

void count_steps()
{
	/* ==================== 計步器演算法 ==================== */
	Pedometer* pedometer = new Pedometer(50, 25, 2, 500);
	/* ==================== 計步器演算法 ==================== */

	const string filename ="../dataset/Motion/gaduo@singularwings.com-Accelerator-20171127-204829.txt";

	/**
	 * A data composed by 19 bytes;
	 * | Gyro-xL | Gyro-xH | Gyro-yL | Gyro-yH | Gyro-zL | Gyro-zH |
	 * | Acc-xL  | Acc-xH  | Acc-yL  | Acc-yH  | Acc-zL  | Acc-zH  |
	 * | Mag-xL  | Mag-xH  | Mag-yL  | Mag-yH  | Mag-zL  | Mag-zH  |
	 * | idx     |
	 */
	Loader loader;
	const int buffer_size = 19;
	fstream file;

	file.open(filename, ios::in | ios::binary);

	if(file.is_open()) {

		file.seekg(0, ios::end);
		streamoff file_size = file.tellg();

		file.seekg(0, ios::beg);
		for(int i = 0; i < (file_size/buffer_size); i++) {
			char* buffer = new char[buffer_size];
			file.read(buffer, buffer_size);

			short* data = loader.convertByteToShortArrayForMotion(buffer);
			
			/* ==================== 計步器演算法 ==================== */
			const int steps = pedometer->countStep(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
			if (steps != -1)
			{
				cout << steps << "\n";
			}
			/* ==================== 計步器演算法 ==================== */

			file.seekg(0, ios::cur);
		}
	}
}