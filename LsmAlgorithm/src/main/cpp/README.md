1. 心率演算法
=============


1.1 參數初始化
--------------

 1. sample_rate: 取樣頻率；預設為 250。
 2. buffer_size: 輸入訊號大小；預設為 2 秒；數值為 500。


```
	HeartRate* heart_rate = new HeartRate(sample_rate, buffer_size);
```

1.2 演算法計算
---------------

一次輸入一個電壓值 `sample`。  
若取樣頻率為 250 則一個電壓值為 1/250 秒。

**當回傳值 hr 不為 -1 時，才是經計算後的心率**

```
	int hr = heart_rate->countHeartRate(sample);
	if (hr != -1)
	{
		// 輸出心率。
	}
```

-----------------------------------------------------------------------------------------------

2. 熱量演算法
=============

`熱量演算法` 仰賴 `心率演算法` 的輸出。

2.1 參數初始化
--------------

2.1.1 心率演算法參數初始化
----------------------------

 1. sample_rate: 取樣頻率；預設為 250。
 2. buffer_size: 輸入訊號大小；預設為 2 秒；數值為 500。

```
	HeartRate* heart_rate = new HeartRate(sample_rate, buffer_size);
```

2.1.2 心率演算法參數初始化
----------------------------

 1. weight: 體重；預設為 50 公斤。
 2. age: 年齡；預設為 25 歲。
 3. is_girl: 是女性；預設為 true。
 
```
	Heat* heat = new Heat(weight, age, is_girl);
	
```


2.2 演算法計算
---------------

```
	int hr = heart_rate->countHeartRate(data[0]);
	
	if (hr != -1)
	{
		double t = 0.002;
		double calories = heat->countHeatCalories(hr, t); // 每 6 秒計算一次 T = 0.002
		cout << calories << "\n";
	}

```

-----------------------------------------------------------------------------------------------

3. 計步器演算法
===============


3.1 參數初始化
--------------

 1. sample_rate: 取樣頻率；預設為 50。
 2. buffer_size: 輸入訊號大小；預設為 0.5 秒；數值為 25。
 3. time_interval: 時間限制區間；預設為 2 個取樣頻率。
 4. amplitude_limit: 震幅限制；預設為 5。

```
	Pedometer* pedometer = new Pedometer(sample_rate, buffer_size, time_interval, 5);
```

3.2 演算法計算
---------------

輸入來自藍牙的 Motion 訊號依序為
data[0]: Gyroscope X
data[1]: Gyroscope Y
data[2]: Gyroscope Z
data[3]: Acceleration X
data[4]: Acceleration Y
data[5]: Acceleration Z
data[6]: Magnetic X
data[7]: Magnetic Y
data[8]: Magnetic Z

```
	const int steps = pedometer->countStep(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
	if (steps != -1)
	{
		cout << steps << "\n";
	}
```



作者群: Moriarty, Zhen, Gaduo, Rouyi