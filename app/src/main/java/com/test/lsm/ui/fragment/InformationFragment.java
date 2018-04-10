package com.test.lsm.ui.fragment;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.test.lsm.R;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.InfoBean;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

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

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0://心电图
                    byte[] obj = (byte[]) msg.obj;
                    //TODO 处理心电图数据
                    MyLog.e(TAG, "" + HexUtil.formatHexString(obj));
                    break;
                case 1://动作
                    break;
            }
            return false;
        }
    });


    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);

        List<InfoBean> infoBeanList = new ArrayList<>();
        infoBeanList.add(new InfoBean(0,"68"));
        infoBeanList.add(new InfoBean(1,"23"));
        infoBeanList.add(new InfoBean(2,"265"));

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

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
            String serviceUUID = service.getUuid().toString();
            //MyLog.e(TAG, "serviceUUID：" + serviceUUID);
            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
            if (serviceUUID.contains("AA70") || serviceUUID.contains("aa70")) {//心电图service
                handleHeartService(characteristics, bleDevice);
            } else if (serviceUUID.contains("AA80") || serviceUUID.contains("aa80")) {//动作service
                handleMotionService(characteristics, bleDevice);
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
    private void handleMotionService(List<BluetoothGattCharacteristic> characteristics, BleDevice bleDevice) {

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
                                MyToast.showShort(getContext(), "AA72指令写入失败" + exception);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
