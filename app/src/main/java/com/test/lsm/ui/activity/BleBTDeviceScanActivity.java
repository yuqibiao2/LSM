package com.test.lsm.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
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
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * 功能：ble蓝牙设备扫描、连接
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/9
 */
public class BleBTDeviceScanActivity extends LsmBaseActivity {


    private static final String TAG = "BleBTDeviceScanActivity";

    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.list_device)
    ListView listDevice;
    @BindView(R.id.img_loading)
    ImageView imgLoading;

    private Animation operatingAnim;
    private BleDeviceAdapter mDeviceAdapter;
    private MyApplication application;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ble_bt_device_scan;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getApplication();
        //initBleBT();
    }

    @Override
    protected void initView() {
        mDeviceAdapter = new BleDeviceAdapter(this);
        listDevice.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void initListener() {
        mDeviceAdapter.setOnDeviceClickListener(new BleDeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onConnect(BleDevice bleDevice) {
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
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


    /**
     * 开始扫描
     *
     * @param view
     */
    public void startScan(View view) {

        MyLog.e(TAG, "startScan");

        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
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
                    }
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }
        });
    }

    /**
     * 建立ble设备连接
     *
     * @param bleDevice
     */
    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                showLoadDialog("设备连接中...");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                MyToast.showShort(BleBTDeviceScanActivity.this, "连接失败" + exception.getDescription());
                hiddenLoadDialog();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                MyToast.showShort(BleBTDeviceScanActivity.this, "连接成功");
                hiddenLoadDialog();
                mDeviceAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new BleConnectMessage(1, bleDevice));
                application.setCurrentBleDevice(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                MyToast.showShort(BleBTDeviceScanActivity.this, "取消了连接");
                hiddenLoadDialog();
                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }
        });
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
    protected void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //BleManager.getInstance().disconnectAllDevice();
        //BleManager.getInstance().destroy();
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, BleBTDeviceScanActivity.class);
        activity.startActivity(intent);
    }

}
