package com.test.lsm.ui.dialog;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.BleDeviceAdapter;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.service.CheckBleIsConnectService;
import com.test.lsm.ui.activity.MainActivity;
import com.test.lsm.ui.activity.SplashActivity;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.WindowUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/20
 */
public class BleBTDeviceScanDialog extends LsmBaseDialog {

    private static final String TAG = "BleBTDeviceScanDialog";

    @BindView(R.id.list_device)
    ListView listDevice;
    @BindView(R.id.img_loading)
    ImageView imgLoading;

    private Animation operatingAnim;
    private BleDeviceAdapter mDeviceAdapter;
    private MyApplication application;

    public BleBTDeviceScanDialog(Context context) {
        super(context);
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = WindowUtils.getSize(mContext)[0] / 5 * 4;
        lp.height = WindowUtils.getSize(mContext)[1] / 2;
        return lp;
    }

    @Override
    protected int getLayoutId() {

        return R.layout.activity_ble_bt_device_scan;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) ((Activity) mContext).getApplication();
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void initView() {
        mDeviceAdapter = new BleDeviceAdapter(mContext);
        listDevice.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void initListener() {
        mDeviceAdapter.setOnDeviceClickListener(new BleDeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    imgLoading.startAnimation(operatingAnim);
                    imgLoading.setVisibility(View.VISIBLE);
                    connect(bleDevice);
                }
            }

            @Override
            public void onDisConnect(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
            }

            @Override
            public void onDetail(BleDevice bleDevice) {
                if (BleManager.getInstance().isConnected(bleDevice)) {
                   /* Intent intent = new Intent(BleBTDeviceScanActivity.this, OperationActivity.class);
                    intent.putExtra(OperationActivity.KEY_DATA, bleDevice);
                    startActivity(intent);*/
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        scan();
    }

    @OnClick(R.id.tv_scan)
    public void startScan(View view) {
        scan();
    }

    private void scan(){
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                if (imgLoading!=null){
                    imgLoading.startAnimation(operatingAnim);
                    imgLoading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String dName = bleDevice.getDevice().getName();
                if (dName != null) {
                    if (dName.contains("CC") || dName.contains("Sensor") || dName.contains("Tag")) {
                        mDeviceAdapter.addDevice(bleDevice);
                        mDeviceAdapter.notifyDataSetChanged();
/*                        String mac = bleDevice.getMac();
                        String connectDeviceMac = BleBTUtils.getConnectDevice(getContext());
                        if (!TextUtils.isEmpty(mac) && mac.equals(connectDeviceMac)) {//已经配对过的设备
                            BleManager.getInstance().connectWapper(bleDevice, new BleGattCallback() {
                                @Override
                                public void onStartConnect() {
                                    MyLog.d(TAG, "onStartConnect===");
                                }

                                @Override
                                public void onConnectFail(BleException exception) {
                                    MyLog.e(TAG, "onConnectFail===" + exception.getDescription());
                                    MyToast.showShort(mContext, "连接失败" + exception.getDescription());
                                }

                                @Override
                                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                                    application.setCurrentBleDevice(bleDevice);
                                    mDeviceAdapter.notifyDataSetChanged();
                                    MyLog.d(TAG, "onConnectSuccess===");
                                }

                                @Override
                                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                                    MyLog.d(TAG, "onDisConnected===");
                                }
                            });
                        }*/

                    }
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                if (imgLoading!=null){
                    imgLoading.clearAnimation();
                    imgLoading.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 建立ble设备连接
     *
     * @param bleDevice
     */
    private void connect(final BleDevice bleDevice) {
        /*BleManager.getInstance().connectWapper(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleException exception) {
                MyToast.showShort(mContext, "连接失败" + exception.getDescription());
                if (imgLoading!=null){
                    imgLoading.clearAnimation();
                    imgLoading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                MyToast.showShort(mContext, "连接成功");
                mDeviceAdapter.notifyDataSetChanged();
                BleBTUtils.saveConnectDevice(mContext , bleDevice.getMac());
                EventBus.getDefault().post(new BleConnectMessage(1, bleDevice));
                application.setCurrentBleDevice(bleDevice);
                if (imgLoading!=null){
                    imgLoading.clearAnimation();
                    imgLoading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                MyToast.showShort(mContext, "取消了连接");
                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }
        });*/
    }

    /**
     * 显示已连接的ble设备
     */
    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showConnectedDevice();
    }

    @Override
    public void onDetachedFromWindow() {
        BleManager.getInstance().cancelScan();
        super.onDetachedFromWindow();
    }
}
