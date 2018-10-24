package com.test.lsm.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.swm.algorithm.Algorithm;
import com.swm.algorithm.support.IirFilter;
import com.swm.algorithm.support.heat.SwmQuantityOfHeat;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.InfoBean;
import com.test.lsm.bean.event.CalorieChgEvent;
import com.test.lsm.bean.event.ECGChgEvent;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.bean.event.OnUserInfoChg;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.event.StepChgEvent;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.db.bean.Calorie;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.CalorieService;
import com.test.lsm.db.service.StepService;
import com.test.lsm.db.service.inter.IStepService;
import com.test.lsm.global.Constant;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.ui.activity.ECGShowActivity3;
import com.test.lsm.ui.activity.SettingActivity;
import com.test.lsm.utils.AlgorithmWrapper;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.SportStepJsonUtils;
import com.today.step.lib.TodayStepService;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.lsmalgorithm.MyLib;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：数据信息界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class InformationFragment extends LsmBaseFragment {

    private static final String TAG = "InformationFragment";
    @BindView(R.id.tv_heart_num)
    TextView tvHeartNum;
    @BindView(R.id.ll_con)
    LinearLayout llCon;
    @BindView(R.id.tv_step_num)
    TextView tvStepNum;
    @BindView(R.id.ll_con2)
    LinearLayout llCon2;
    @BindView(R.id.ll_con3)
    LinearLayout llCon3;
    @BindView(R.id.tv_calorie)
    TextView tvCalorie;
    @BindView(R.id.fl_heart)
    FrameLayout flHeart;
    @BindView(R.id.fl_step)
    FrameLayout flStep;
    @BindView(R.id.fl_calorie)
    FrameLayout flCalorie;
    @BindView(R.id.rl_heart)
    RelativeLayout rlHeart;
    @BindView(R.id.rl_step)
    RelativeLayout rlStep;
    @BindView(R.id.rl_calorie)
    RelativeLayout rlCalorie;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.fl_hr_chart)
    FrameLayout flHrChart;
    @BindView(R.id.rv_user_icon)
    RoundImageView rvUserIcon;
    @BindView(R.id.rl_connected_device)
    RelativeLayout rlConnectedDevice;
    @BindView(R.id.tv_bt_name)
    TextView tvBtName;
    @BindView(R.id.tv_bry_pct)
    TextView tvBryPct;
    @BindView(R.id.srl_info)
    SmartRefreshLayout srlInfo;

    private static final int ECG_CODE=1001;
    private static final int BATTERY_CODE=1002;

    private Activity mAct;
    private MyApplication application;

    CircularFifoQueue<Integer> ecgBuffer = new CircularFifoQueue(2500);

    private List<Integer> rriList = new ArrayList<>();

    private boolean isHandBatteryService = false;

    private IirFilter iirFilter = Algorithm.newIirFilterInstance();

    private int startCount = 0;
    private int displayHR = -1;
    private int zeroCount = 0;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (isDestroy) return false;
            switch (msg.what) {
                case ECG_CODE: {//心电图

                    byte[] obj = (byte[]) msg.obj;

                    short[] ecgData = Algorithm.getEcgByte2Short(obj);

                    EventBus.getDefault().post(new ECGChgEvent(ecgData, "得到ECG的值了==="));
                    for (short ecg : ecgData) {
                        ecgBuffer.add(Integer.valueOf(ecg));
                    }

                    String hexString = HexUtil.formatHexString(obj);
                    String hrStr = hexString.substring(0, 2);
                    MyLog.e(TAG, "hexString=============" + hexString);
                    String RRIStrL = hexString.substring(2, 4);
                    String RRIStrH = hexString.substring(4, 6);
                    String RRIStr = RRIStrH + RRIStrL;
                    //MyLog.e(TAG , "RRIL==============="+Integer.parseInt(RRIStrL , 16));
                    //MyLog.e(TAG , "RRIH==============="+Integer.parseInt(RRIStrH , 16));
                    MyLog.e(TAG , "RRI==============="+Integer.parseInt(RRIStr , 16));

                    AlgorithmWrapper.startRRI();

                    int heartNum = Integer.parseInt(hrStr, 16);

                    int heartRate = heartNum;
                    startCount++;
                    if (startCount > 6) {
                        if (heartRate == 0) {
                            zeroCount++;
                        } else {
                            zeroCount = 0;
                        }
                        if (zeroCount > 3) {
                            displayHR = 0;
                            zeroCount = 0;
                        } else {
                            if ((heartRate >= 30) && (heartRate <= 250)) {
                                if (displayHR == -1) {
                                    displayHR = heartRate;
                                } else {
                                    if (Math.abs(heartRate - displayHR) > 8) {
                                        if ((heartRate - displayHR) > 0) {
                                            displayHR = displayHR + 5;
                                        } else {
                                            displayHR = displayHR - 5;
                                        }
                                    } else {
                                        displayHR = heartRate;
                                    }
                                }
                            }
                        }
                    }

                    if (displayHR != -1) {
                        //得到心跳值得回调
                        if (application.mOnGetHrValueListener != null) {
                            application.mOnGetHrValueListener.onGet(displayHR);
                        }
                        Constant.oneMinHeart.add(displayHR);
                        Constant.hrBuffer.add(displayHR);
                        Constant.hrBuffer2.add(displayHR);
                        tvHeartNum.setText("" + displayHR);
                        application.setHeartNum(displayHR);
                        EventBus.getDefault().post(new HeartChgEvent(displayHR, "心跳变化了"));
                        MyLog.e(TAG, "tvHeartNum：" + displayHR);
                    }

                    //double rriValue = Algorithm.getEstimateRRI(heartNum);
                    double rriValue = Integer.parseInt(RRIStr, 16);
                    if (rriValue > 200 && rriValue < 2000) {
                        rriList.add(Double.valueOf(rriValue).intValue());
                        // 通知刷新 HRV
                        EventBus.getDefault().post(new RefreshHearthInfoEvent("更新HRV", rriList));
                        if (rriList.size() >= 220) {//
                            Constant.lastedUsefulRriList.clear();
                            Constant.lastedUsefulRriList.addAll(rriList);
                            rriList.clear();
                        }
                    }

                    //MyLog.e(TAG , epcData);
                    if (Constant.sbHeartData.length() < 500) {
                        Short ecg0 = ecgData[0];
                        Short ecg1 = ecgData[1];
                        Short ecg2 = ecgData[2];
                        Short ecg3 = ecgData[3];
                        Short ecg4 = ecgData[4];
                        String epcData2 = "" + iirFilter.filter(ecg0.intValue()) + ","
                                + iirFilter.filter(ecg1.intValue()) + ","
                                + iirFilter.filter(ecg2.intValue()) + ","
                                + iirFilter.filter(ecg3.intValue()) + ","
                                + iirFilter.filter(ecg4.intValue()) + ",";
                        Constant.sbHeartData.append(epcData2);
                    }
                    //Constant.sbHeartData2.append(epcData2);

                    Constant.egcDataCon.add(ecgData[0]);
                    Constant.egcDataCon.add(ecgData[1]);
                    Constant.egcDataCon.add(ecgData[2]);
                    Constant.egcDataCon.add(ecgData[3]);
                    Constant.egcDataCon.add(ecgData[4]);
                    break;
                }

                case BATTERY_CODE: {
                    if (application.isBleConnected()){
                        handleBatteryService(application.getCurrentBleDevice());
                    }
                    break;
                }
            }
            return false;
        }
    });
    private IStepService stepService;
    private List<FrameLayout> itemContainer;
    private boolean isDestroy = false;
    private boolean isGirl;
    private int weight = 64;
    private int age;
    private CalorieService calorieService;

    private float totalBleCalorie;

    /**
     * 得到数据转成short
     *
     * @param n     1、2、3、4、5
     * @param value
     * @return
     */
    public short getECGValue(int n, byte[] value) {
        if (n <= 0 || n > 5) {
            throw new IndexOutOfBoundsException();
        }

        return (short) (((value[(n - 1) * 2 + 1] & 0xFF) << 8) | (value[(n - 1) * 2] & 0xFF));
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index 第几位开始取
     * @return
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        isDestroy = false;
        EventBus.getDefault().register(this);
        mAct = getActivity();
        application = (MyApplication) getActivity().getApplication();

        //Algorithm.initialForModeChange(1);

        List<InfoBean> infoBeanList = new ArrayList<>();
        infoBeanList.add(new InfoBean(0, "68"));
        infoBeanList.add(new InfoBean(1, "23"));
        infoBeanList.add(new InfoBean(2, "265"));

        stepService = new StepService();
        calorieService = new CalorieService();

        itemContainer = new ArrayList<>();
        itemContainer.add(flHeart);
        itemContainer.add(flStep);
        itemContainer.add(flCalorie);
        itemContainer.add(flHrChart);

        UserLoginReturn.PdBean user = application.getUser();
        String userSex = user.getUSER_SEX();
        isGirl = userSex.equals("0");
        String userWeight = user.getUSER_WEIGHT();
        if (!TextUtils.isEmpty(userWeight)) {
            try {
                weight = Integer.parseInt(userWeight);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String birthday = user.getBIRTHDAY();
        age = 30;
        if (!TextUtils.isEmpty(birthday)) {
            age = MyTimeUtils.getAge(birthday);
        }
        totalBleCalorie = calorieService.getCurrentDateTotalCalorie();
    }

    @Override
    protected void initView() {
        srlInfo.setEnableLoadMore(false);
        String userImage = application.getUser().getUSER_IMAGE();
        GlidUtils.load(getContext(), rvUserIcon, userImage);

        //---蓝牙已连接（SplashActivity）
        if (application.isBleConnected()) {
            BleDevice currentBleDevice = application.getCurrentBleDevice();
            rlConnectedDevice.setVisibility(View.VISIBLE);
            tvBtName.setText(currentBleDevice.getName());
            //---得到所有的Service
            //BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(currentBleDevice);
            //List<BluetoothGattService> services = gatt.getServices();
            //---处理Service
            handleService(currentBleDevice);
        }

        // 删除
        //genTestHr();
        // 删除
    }

  /*  private void genTestHr() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if (application.mOnGetHrValueListener != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int num = new Random().nextInt(20);
                                    application.mOnGetHrValueListener.onGet(100+num);
                                }
                            });
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }*/

    @Override
    protected void initListener() {

        srlInfo.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mAct.unbindService(stepServiceConnect);
                initStepCount();
                srlInfo.finishRefresh(2000);
            }
        });

        rvUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.startAction(getActivity());
            }
        });

        //---蓝牙断开连接
        BleManager.getInstance().setOnConnectDismissListener(new BleManager.OnConnectDismiss() {
            @Override
            public void dismiss(BleDevice device) {
                if (!isDestroy) {
                    rlConnectedDevice.setVisibility(View.GONE);
                }
            }
        });

        //---蓝牙连接成功
        BleManager.getInstance().setOnConnectSuccessListener(new BleManager.OnConnectSuccess() {
            @Override
            public void onSuccess(BleDevice bleDevice) {
                //---得到所有的Service
                //BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
                //List<BluetoothGattService> services = gatt.getServices();
                //---处理Service
                handleService(bleDevice);
                if (application.isBleConnected()) {
                    BleDevice currentBleDevice = application.getCurrentBleDevice();
                    rlConnectedDevice.setVisibility(View.VISIBLE);
                    tvBtName.setText(currentBleDevice.getName());
                }
                BleBTUtils.saveConnectDevice(getContext(), bleDevice.getMac());
            }
        });

        //---根据一分钟心跳计算卡路里值
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isDestroy) {
                    try {
                        if (!application.isBleConnected()) {
                            continue;
                        }
                        //计算平均值
                        CircularFifoQueue<Integer> oneMinHeart = Constant.oneMinHeart;
                        int total = 0;
                        for (Integer heartNum : oneMinHeart) {
                            total += heartNum;
                        }
                        int size = oneMinHeart.size();
                        int avgHearNum = size > 0 ? total / size : 0;
                        SwmQuantityOfHeat quantityOfHeat = Algorithm.newQuantityOfHeat();
                        double calorieOnMinute = quantityOfHeat.getQuantityOfHeatMinutes(avgHearNum, isGirl, age, weight, 1);

                        //MyLog.e(TAG , "calorieOnMinute=="+calorieOnMinute);

                        totalBleCalorie = (float) (totalBleCalorie + calorieOnMinute);
                        Calorie calorie = new Calorie();
                        calorie.setDate(MyTimeUtils.getCurrentDate());
                        calorie.setHour(MyTimeUtils.getCurrentHour());
                        calorie.setCalorieValue(totalBleCalorie);
                        calorieService.addCurrentDayCalorie(calorie);

                        String calorieByStep = SportStepJsonUtils.getCalorieByStep(mStepSum);
                        float calorieSys = Float.valueOf(calorieByStep);
                        final String calorieStr = String.format("%.1f", totalBleCalorie / 1000 + calorieSys);
                        //MyLog.e(TAG , "calorieStr=="+totalBleCalorie / 1000 + calorieSys);
                        tvCalorie.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isDestroy) return;
                                tvCalorie.setText("" + calorieStr);
                            }
                        });
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    protected void initData() {
        super.initData();
        initStepCount();
    }

    @OnClick({R.id.rl_heart, R.id.rl_step, R.id.rl_calorie, R.id.rl_ecg, R.id.rl_hr_chart})
    public void onItemClick(View view) {

        // TransitionManager.beginDelayedTransition(llContainer);

        switch (view.getId()) {
            case R.id.rl_heart:
                //HrRecordActivity.startAction(getActivity());
                openItem(0);
                break;
            case R.id.rl_step:
                openItem(1);
                if (flStep.getVisibility() == View.VISIBLE) {
                    EventBus.getDefault().post(new StepChgEvent(mStepSum, "步数值更新"));
                }
                break;
            case R.id.rl_calorie:
                openItem(2);
                if (flCalorie.getVisibility() == View.VISIBLE) {
                    String calorieByStep = SportStepJsonUtils.getCalorieByStep(mStepSum);
                    EventBus.getDefault().post(new CalorieChgEvent(Float.parseFloat(calorieByStep), "步数值更新"));
                }
                break;
            case R.id.rl_ecg:
                if (!application.isBleConnected()) {
                    MyToast.showLong(getContext(), "蓝牙设备未连接");
                } else {
                    ECGShowActivity3.startAction(getActivity());
                }
                break;
            case R.id.rl_hr_chart:
                openItem(3);
                if (flHrChart.getVisibility() == View.VISIBLE) {
                    Constant.isHRChartDetailShow = true;
                }
                break;
        }
        view.setVisibility(View.VISIBLE);
    }

    private void openItem(int position) {
        Constant.isHRChartDetailShow = false;
        for (int i = 0; i < itemContainer.size(); i++) {
            FrameLayout item = itemContainer.get(i);
            if (position == i) {
                if (item.getVisibility() == View.VISIBLE) {
                    item.setVisibility(View.GONE);
                } else {
                    item.setVisibility(View.VISIBLE);
                }
            } else {
                item.setVisibility(View.GONE);
            }
        }
    }

    private static final int REFRESH_STEP_WHAT = 1000;
    //循环取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL_REFRESH = 500;
    private Handler mDelayHandler = new Handler(new TodayStepCounterCall());
    private int mStepSum;
    private ISportStepInterface iSportStepInterface;
    ServiceConnection stepServiceConnect;

    private void initStepCount() {
        //开启计步Service，同时绑定Activity进行aidl通信
        Intent intent = new Intent(mAct, TodayStepService.class);
        mAct.startService(intent);
        mAct.bindService(intent, stepServiceConnect = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //Activity和Service通过aidl进行通信
                iSportStepInterface = ISportStepInterface.Stub.asInterface(service);
                try {
                    mStepSum = iSportStepInterface.getCurrentTimeSportStep();
                    updateStepCount();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class TodayStepCounterCall implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_STEP_WHAT: {
                    //每隔500毫秒获取一次计步数据刷新UI
                    if (null != iSportStepInterface) {
                        int step = 0;
                        try {
                            step = iSportStepInterface.getCurrentTimeSportStep();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (mStepSum != step) {
                            mStepSum = step;
                            updateStepCount();
                        }
                    }
                    mDelayHandler.sendEmptyMessageDelayed(REFRESH_STEP_WHAT, TIME_INTERVAL_REFRESH);

                    break;
                }
            }
            return false;
        }
    }

    private void updateStepCount() {

        if (isDestroy) {
            return;
        }

        Step step = new Step();
        step.setStepNum(mStepSum);
        step.setDate(MyTimeUtils.getCurrentDate());
        step.setHour(MyTimeUtils.getCurrentHour());
        stepService.addCurrentDayStep(step);

        Log.e(TAG, "updateStepCount : " + mStepSum);
        //MyToast.showLong(getContext() ,"当前步数======updateStepCount======"+mStepSum );
        tvStepNum.setText(mStepSum + "");
        String calorieByStep = SportStepJsonUtils.getCalorieByStep(mStepSum);
        float calorieSys = Float.valueOf(calorieByStep);
        final String calorieStr = String.format("%.1f", totalBleCalorie / 1000 + calorieSys);
        tvCalorie.setText(calorieStr + "");
        String distanceByStep = SportStepJsonUtils.getDistanceByStep(mStepSum);
        String caloriesValue = SportStepJsonUtils.getCalorieByStep(mStepSum);
        application.setStepNum(mStepSum);
        application.setStepDistance(Double.parseDouble(distanceByStep));
        application.setCalorieValue(Double.parseDouble(caloriesValue));
        EventBus.getDefault().post(new StepChgEvent(mStepSum, "步数值更新"));
    }

    /**
     * 处理ble服务
     */
    private void handleService(BleDevice bleDevice) {

        //心电图
        handleHeartService(bleDevice);
        //电池电量
        //handleBatteryService(bleDevice);

    }

    /**
     * 处理电量service
     * <p>
     * F000180F-0451-4000-B000-000000000000
     *
     * @param bleDevice
     */
    private void handleBatteryService(BleDevice bleDevice) {

        BleManager.getInstance()
                .read(bleDevice,
                        "0000180f-0000-1000-8000-00805f9b34fb",
                        "00002a19-0000-1000-8000-00805f9b34fb",
                        new BleReadCallback() {
                            @Override
                            public void onReadSuccess(byte[] data) {
                                String hexString = HexUtil.formatHexString(data).substring(0, 2);
                                MyLog.e(TAG, "电量：" + Integer.parseInt(hexString, 16));
                                if (tvBryPct != null) {
                                    tvBryPct.setText(Integer.parseInt(hexString, 16) + "%");
                                }
                                mHandler.sendEmptyMessageDelayed(BATTERY_CODE , 15*1000);
                            }

                            @Override
                            public void onReadFailure(BleException exception) {

                            }
                        });
    }

    /**
     * 处理心电图service
     * <p>
     * F000AA70-0451-4000-B000-000000000000
     */
    private void handleHeartService(final BleDevice bleDevice) {

        //发送01使设备处于工作状态 00 是休眠状态
        BleManager.getInstance().write(
                bleDevice,
                "f000aa70-0451-4000-b000-000000000000",
                "f000aa72-0451-4000-b000-000000000000",
                //HexUtil.hexStringToBytes("01"),
                HexUtil.hexStringToBytes("03"),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        startNotify();
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        MyToast.showLong(getContext(), "AA72指令写入失败" + exception);
                        MyLog.e(TAG, "AA72指令写入失败" + exception);
                    }

                    //开启通知
                    private void startNotify() {
                        BleManager.getInstance().notify(
                                bleDevice,
                                "f000aa70-0451-4000-b000-000000000000",
                                "f000aa71-0451-4000-b000-000000000000",
                                new BleNotifyCallback() {
                                    @Override
                                    public void onNotifySuccess() {
                                        MyLog.d(TAG, "AA71通知开始成功===============");
                                        //发送通知读取电量
                                        mHandler.sendEmptyMessageDelayed(BATTERY_CODE , 10*1000);
                                    }

                                    @Override
                                    public void onNotifyFailure(final BleException exception) {
                                        MyLog.e(TAG, "AA71通知开始失败===============");
                                    }

                                    @Override
                                    public void onCharacteristicChanged(byte[] data) {
                                        Message message = new Message();
                                        message.what = ECG_CODE;
                                        message.obj = data;
                                        mHandler.sendMessage(message);
                                    }
                                });
                    }
                });


    }

    @Override
    protected void afterInit() {
        super.afterInit();
        BleDevice currentBleDevice = application.getCurrentBleDevice();
        if (currentBleDevice != null &&
                BleManager.getInstance().isConnected(currentBleDevice)) {
            EventBus.getDefault().post(new BleConnectMessage(1, currentBleDevice));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            //---统计
            FirebaseAnalytics.getInstance(getActivity())
                    .setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        mHandler.removeMessages(ECG_CODE);
        mHandler.removeMessages(BATTERY_CODE);
        EventBus.getDefault().unregister(this);
        mAct.unbindService(stepServiceConnect);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUserInfoChanged(OnUserInfoChg onUserInfoChg) {
        String userImage = application.getUser().getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)) {
            GlidUtils.load(getContext(), rvUserIcon, userImage);
        }
    }
}
