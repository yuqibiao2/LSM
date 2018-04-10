package com.test.lsm.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.lsm.R;
import com.test.lsm.adapter.DeviceAdapter;
import com.test.lsm.utils.bt.tradition.BTManager;
import com.test.lsm.utils.bt.tradition.LsmDeviceManager;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 功能：经典蓝牙设备连接Activity
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/23
 */

public class BTDeviceScanActivity extends LsmBaseActivity {


    ListView listDevice;
    private BTManager btManager;
    private LsmDeviceManager lsmDeviceManager;
    private DeviceAdapter btDeviceAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bt_device_scan;
    }

    @Override
    public void beforeInit() {
        btManager = BTManager.getInstance(this);
        lsmDeviceManager = LsmDeviceManager.getInstance(this);
    }

    @Override
    protected void initView() {
        listDevice = (ListView)findViewById(R.id.list_device);
    }

    @Override
    protected void initData() {
        btDeviceAdapter = new DeviceAdapter(this);
        listDevice.setAdapter(btDeviceAdapter);
    }

    @Override
    protected void initListener() {
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = btDeviceAdapter.getBluetoothDeviceList().get(position);
                String deviceName = device.getName();
                if (deviceName.startsWith("NP")) {
                } else if (deviceName.startsWith("SwingU")) {
                    if (lsmDeviceManager.isConnected()){
                        MyToast.showShort(BTDeviceScanActivity.this , "设备已连接");
                    }else{
                        lsmDeviceManager.connectDevice(device);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //---刷新蓝牙设备（已绑定的）
        Set<BluetoothDevice> deviceSet = btManager.getBoundsDevice();
        List deviceList = new ArrayList();
        Iterator it = deviceSet.iterator();
        while (it.hasNext()) {
            deviceList.add(it.next());
        }
        btDeviceAdapter.setBluetoothDeviceList(deviceList);
        btDeviceAdapter.notifyDataSetChanged();
    }

    public void setBT(View view){
        btManager.toSysBTActivity(this);
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity,BTDeviceScanActivity.class);
        activity.startActivity(intent);
    }

}
