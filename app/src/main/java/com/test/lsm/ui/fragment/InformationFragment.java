package com.test.lsm.ui.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.swm.core.HeartRateService;
import com.swm.core.temp.BleData;
import com.swm.core.temp.EcgData;
import com.swm.core.temp.FirFilter;
import com.swm.core.temp.IirFilter;
import com.swm.core.temp.SwmFilter;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.InfoBean;
import com.test.lsm.bean.PushMsgBean;
import com.test.lsm.bean.event.CalorieChgEvent;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.bean.event.StepChgEvent;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.StepService;
import com.test.lsm.db.service.inter.IStepService;
import com.test.lsm.global.Constant;
import com.test.lsm.ui.activity.ECGShowActivity;
import com.today.step.lib.ISportStepInterface;
import com.today.step.lib.SportStepJsonUtils;
import com.today.step.lib.TodayStepService;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.lsmalgorithm.MyLib;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import singularwings.com.swmalgolib.EcgAlgo;

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
    Unbinder unbinder;
    @BindView(R.id.rl_heart)
    RelativeLayout rlHeart;
    @BindView(R.id.rl_step)
    RelativeLayout rlStep;
    @BindView(R.id.rl_calorie)
    RelativeLayout rlCalorie;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    private Activity mAct;
    private MyApplication application;

    double[] rriArray = new double[700];
    double[] timeArray = new double[700];
    short[] historyArray = new short[700];

    SwmFilter swmFilter = new IirFilter(0.992);//new FirFilter(250);

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {//心电图
                    byte[] obj = (byte[]) msg.obj;
                    //TODO 处理心电图数据
                    String hexStr = HexUtil.encodeHexStr(obj);
                    String substring = hexStr.substring(0, 4);
                    int parseInt = Integer.parseInt(substring, 16);
                    //MyLog.e(TAG, "encodeHexStr===" + hexStr);
                    short[] data = new short[]{getECGValue(1, obj),
                            getECGValue(2, obj),
                            getECGValue(3, obj),
                            getECGValue(4, obj),
                            getECGValue(5, obj)};

                 /*  int[] data = new int[]{
                           Integer.parseInt(hexStr.substring(0, 4), 16),
                           Integer.parseInt(hexStr.substring(4, 8), 16),
                           Integer.parseInt(hexStr.substring(8, 12), 16),
                           Integer.parseInt(hexStr.substring(12, 16), 16),
                           Integer.parseInt(hexStr.substring(16, 20), 16)
                   };*/

                    int i1;
                    //MyLog.e("hexStr===="+hexStr);
                    String epcData = "" + data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + ",";

                    short[] ecgData = EcgAlgo.getEcgByte2Short(obj);
                    String epcData2 = "" + ecgData[0] + "," + ecgData[1] + "," + ecgData[2] + "," + ecgData[3] + "," + ecgData[4] + ",";
                    //MyLog.e(TAG , epcData);

                    Constant.sbHeartData.append(epcData);
                    //Constant.sbHeartData2.append(epcData2);

                  /*  Constant.egcDataCon.add(ecgData[0]);
                    Constant.egcDataCon.add(ecgData[1]);
                    Constant.egcDataCon.add(ecgData[2]);
                    Constant.egcDataCon.add(ecgData[3]);
                    Constant.egcDataCon.add(ecgData[4]);


                    i1 = MyLib.countHeartRate(data);
                    //i1 = MyLib.countHeartRateWrapper(data , data.length);
                    if (i1 != -1) {
                        int heartNum = i1;//i1 / 10000 < 60 ? 60 : (i1 / 10000);
                        tvHeartNum.setText(" " + heartNum);
                        MyLog.e(TAG, "==countHeartRate===" + i1);
                        application.setHeartNum(heartNum);
                    }*/

                    BleData bleData = new BleData(obj);
                    EcgData ecgData1 = EcgData.fromBle(bleData);
                    swmFilter.filter(ecgData1);
                    for (double ecg :ecgData1.samples){
                        //MyLog.e(TAG , "ecg======================"+ecg);
                        Constant.egcDataCon.add((short) ecg);
                    }



                    if (Constant.egcDataCon.size() == 1500) {
                        int[] values = new int[1500];
                        for (int i = 0; i < Constant.egcDataCon.size(); i++) {
                            values[i] = Constant.egcDataCon.get(i).intValue();
                        }
                        heartNum = HeartRateService.CalculateHeartRate(values);
                        HeartRateService.GetRtoRIntervalData(rriArray , timeArray);
                        MyLog.e(TAG, "timeArray：" + timeArray[0] + "   " + timeArray[500]+"   "+timeArray[600]);
                       // MyLog.e(TAG, "rIntervalData2：" + rIntervalData2[0] + "   " + rIntervalData2[500]+"   "+rIntervalData2[600]);
                        tvHeartNum.setText(" " + heartNum);
                        Constant.egcDataCon.clear();

                        HeartRateService.getHistoryRRI(historyArray);
                        MyLog.e(TAG, "historyArray：" + historyArray[0] + "   " + historyArray[500]+"   "+historyArray[600]);

                        float sdnn = HeartRateService.GetSdnn();
                        MyLog.e(TAG , "sdnn："+sdnn);

                    }

                    break;
                }
                case 1: {
                    byte[] obj = (byte[]) msg.obj;
                    String hexStr = HexUtil.encodeHexStr(obj);
                    MyLog.e(TAG, hexStr);
                    int gyroX = Integer.parseInt(hexStr.substring(0, 4), 16);
                    int gyroY = Integer.parseInt(hexStr.substring(4, 8), 16);
                    int gyroZ = Integer.parseInt(hexStr.substring(8, 12), 16);
                    int accX = Integer.parseInt(hexStr.substring(12, 16), 16);
                    int accY = Integer.parseInt(hexStr.substring(16, 20), 16);
                    int accZ = Integer.parseInt(hexStr.substring(20, 24), 16);
                    int magX = Integer.parseInt(hexStr.substring(24, 28), 16);
                    int magY = Integer.parseInt(hexStr.substring(28, 32), 16);
                    int magZ = Integer.parseInt(hexStr.substring(32, 36), 16);
                    int stepNum = MyLib.countStep(gyroX, gyroY, gyroZ, accX, accY, accZ, magX, magY, magZ);
                    MyLog.e(TAG, "stepNum：" + stepNum);
                    tvStepNum.setText("" + stepNum);
                    break;
                }
            }
            return false;
        }
    });
    private int heartNum;
    private IStepService stepService;


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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        mAct = getActivity();
        application = (MyApplication) getActivity().getApplication();

        List<InfoBean> infoBeanList = new ArrayList<>();
        infoBeanList.add(new InfoBean(0, "68"));
        infoBeanList.add(new InfoBean(1, "23"));
        infoBeanList.add(new InfoBean(2, "265"));

        stepService = new StepService();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        initStepCount();
    }

    @OnClick({R.id.rl_heart, R.id.rl_step, R.id.rl_calorie, R.id.rl_ecg})
    public void onItemClick(View view) {

        TransitionManager.beginDelayedTransition(llContainer);

        switch (view.getId()) {
            case R.id.rl_heart:
                if (flHeart.getVisibility() == View.GONE) {
                    EventBus.getDefault().post(new HeartChgEvent(heartNum, "心跳值变了"));
                    flHeart.setVisibility(View.VISIBLE);
                } else {
                    flHeart.setVisibility(View.GONE);
                }
                flStep.setVisibility(View.GONE);
                flCalorie.setVisibility(View.GONE);
                break;
            case R.id.rl_step:
                flHeart.setVisibility(View.GONE);
                if (flStep.getVisibility() == View.GONE) {
                    EventBus.getDefault().post(new StepChgEvent(mStepSum, "步数值更新"));
                    flStep.setVisibility(View.VISIBLE);
                } else {
                    flStep.setVisibility(View.GONE);
                }
                flCalorie.setVisibility(View.GONE);
                break;
            case R.id.rl_calorie:
                flHeart.setVisibility(View.GONE);
                flStep.setVisibility(View.GONE);
                if (flCalorie.getVisibility() == View.GONE) {
                    String calorieByStep = SportStepJsonUtils.getCalorieByStep(mStepSum);
                    EventBus.getDefault().post(new CalorieChgEvent(Float.parseFloat(calorieByStep), "步数值更新"));
                    flCalorie.setVisibility(View.VISIBLE);
                } else {
                    flCalorie.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_ecg:
                //TODO 判断蓝牙设备是否链接
                ECGShowActivity.startAction(getActivity());
                break;
        }
        view.setVisibility(View.VISIBLE);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

        Step step = new Step();
        step.setStepNum(mStepSum);
        step.setDate(MyTimeUtils.getCurrentDate());
        step.setHour(MyTimeUtils.getCurrentHour());
        stepService.addCurrentDayStep(step);
        EventBus.getDefault().post(new StepChgEvent(mStepSum, "步数值更新"));

        Log.e(TAG, "updateStepCount : " + mStepSum);
        tvStepNum.setText(mStepSum + "");
        tvCalorie.setText(SportStepJsonUtils.getCalorieByStep(mStepSum) + "");
        String distanceByStep = SportStepJsonUtils.getDistanceByStep(mStepSum);
        String caloriesValue = SportStepJsonUtils.getCalorieByStep(mStepSum);
        application.setStepNum(mStepSum);
        application.setStepDistance(Double.parseDouble(distanceByStep));
        application.setCalorieValue(Double.parseDouble(caloriesValue));
        EventBus.getDefault().post(new PushMsgBean(R.mipmap.msg1));

        EventBus.getDefault().post(new CalorieChgEvent(Float.parseFloat(caloriesValue), "步数值更新"));

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onBleDeviceStatusChg(BleConnectMessage message) {
        int status = message.getStatus();
        BleDevice bleDevice = message.getBleDevice();
        switch (status) {
            case 0://断开连接
                break;
            case 1://建立连接
                //---得到所有的Service
                BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
                List<BluetoothGattService> services = gatt.getServices();
                //---处理Service
                handleService(services, bleDevice);
                break;
        }
    }

    /**
     * 处理ble服务
     *
     * @param services
     */
    private void handleService(List<BluetoothGattService> services, BleDevice bleDevice) {

        for (BluetoothGattService service : services) {
            String serviceUUID = service.getUuid().toString().toLowerCase();
            //MyLog.e(TAG, "serviceUUID：" + serviceUUID);
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            if (serviceUUID.contains("aa70")) {//心电图service
                handleHeartService(characteristics, bleDevice);
            } else if (serviceUUID.contains("aa80")) {//动作service
                //handleMotionService(characteristics, bleDevice);
            } else if (serviceUUID.contains("180f")) {//电量service
                MyLog.e(TAG, "180f==" + serviceUUID);
                //handleBatteryService(characteristics, bleDevice);
            }
        }

    }

    /**
     * 处理电量service
     * <p>
     * F000180F-0451-4000-B000-000000000000
     *
     * @param characteristics
     * @param bleDevice
     */
    private void handleBatteryService(List<BluetoothGattCharacteristic> characteristics, BleDevice bleDevice) {
        for (final BluetoothGattCharacteristic characteristic : characteristics) {
            String characteristicUUID = characteristic.getUuid().toString().toLowerCase();
            if (characteristicUUID.contains("2A19")) {//

            }

        }
    }


    /**
     * 处理动作service
     * <p>
     * F000AA80-0451-4000-B000-000000000000
     *
     * @param characteristics
     */
    private void handleMotionService(final List<BluetoothGattCharacteristic> characteristics, final BleDevice bleDevice) {
        for (final BluetoothGattCharacteristic characteristic : characteristics) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String characteristicUUID = characteristic.getUuid().toString();
            if (characteristicUUID.contains("AA82") || characteristicUUID.contains("aa82")) {
                //发送01使设备处于工作状态 00 是休眠状态
                BleManager.getInstance().write(
                        bleDevice,
                        serviceUUID,
                        characteristicUUID,
                        HexUtil.hexStringToBytes("1"),
                        new BleWriteCallback() {
                            @Override
                            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                startNotify();
                            }

                            @Override
                            public void onWriteFailure(final BleException exception) {
                                MyToast.showLong(getContext(), "AA82指令写入失败" + exception);
                                MyLog.e(TAG, "AA82指令写入失败" + exception);
                            }

                            //开启通知
                            private void startNotify() {
                                for (final BluetoothGattCharacteristic characteristic : characteristics) {
                                    String serviceUUID = characteristic.getService().getUuid().toString();
                                    String characteristicUUID = characteristic.getUuid().toString();
                                    if (characteristicUUID.contains("AA81") || characteristicUUID.contains("aa81")) {
                                        BleManager.getInstance().notify(
                                                bleDevice,
                                                serviceUUID,
                                                characteristicUUID,
                                                new BleNotifyCallback() {
                                                    @Override
                                                    public void onNotifySuccess() {
                                                        MyLog.d(TAG, "AA81通知开始成功===============");
                                                    }

                                                    @Override
                                                    public void onNotifyFailure(final BleException exception) {
                                                        MyLog.e(TAG, "AA81通知开始失败===============");
                                                    }

                                                    @Override
                                                    public void onCharacteristicChanged(byte[] data) {
                                                        Message message = new Message();
                                                        message.what = 1;
                                                        message.obj = characteristic.getValue();
                                                        mHandler.sendMessage(message);
                                                    }
                                                });
                                    }

                                }
                            }
                        });
            }

        }
    }

    /**
     * 处理心电图service
     * <p>
     * F000AA70-0451-4000-B000-000000000000
     *
     * @param characteristics
     */
    private void handleHeartService(final List<BluetoothGattCharacteristic> characteristics, final BleDevice bleDevice) {

        for (final BluetoothGattCharacteristic characteristic : characteristics) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String characteristicUUID = characteristic.getUuid().toString();
            if (characteristicUUID.contains("AA72") || characteristicUUID.contains("aa72")) {
                //发送01使设备处于工作状态 00 是休眠状态
                BleManager.getInstance().write(
                        bleDevice,
                        serviceUUID,
                        characteristicUUID,
                        HexUtil.hexStringToBytes("01"),
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
                                for (final BluetoothGattCharacteristic characteristic : characteristics) {
                                    String serviceUUID = characteristic.getService().getUuid().toString();
                                    String characteristicUUID = characteristic.getUuid().toString();
                                    if (characteristicUUID.contains("AA71") || characteristicUUID.contains("aa71")) {
                                        BleManager.getInstance().notify(
                                                bleDevice,
                                                serviceUUID,
                                                characteristicUUID,
                                                new BleNotifyCallback() {
                                                    @Override
                                                    public void onNotifySuccess() {
                                                        MyLog.d(TAG, "AA71通知开始成功===============");
                                                    }

                                                    @Override
                                                    public void onNotifyFailure(final BleException exception) {
                                                        MyLog.e(TAG, "AA71通知开始失败===============");
                                                    }

                                                    @Override
                                                    public void onCharacteristicChanged(byte[] data) {
                                                        Message message = new Message();
                                                        message.what = 0;
                                                        message.obj = characteristic.getValue();
                                                        mHandler.sendMessage(message);
                                                    }
                                                });
                                    }

                                }
                            }
                        });
            }

        }

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
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        EventBus.getDefault().unregister(this);
        mAct.unbindService(stepServiceConnect);
    }
}