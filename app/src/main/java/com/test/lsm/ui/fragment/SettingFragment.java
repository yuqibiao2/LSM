package com.test.lsm.ui.fragment;

import android.bluetooth.BluetoothGatt;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.github.ybq.android.spinkit.SpinKitView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.BleDeviceAdapter2;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.ui.activity.UpdateUserActivity1;
import com.test.lsm.utils.LoginRegUtils;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * 功能：设置界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/2
 */
public class SettingFragment extends LsmBaseFragment {

    private static final String TAG = "SettingFragment";

    @BindView(R.id.rv_user_icon)
    RoundImageView rvUserIcon;
    @BindView(R.id.tv_waring_tip)
    TextView tvWaringTip;
    @BindView(R.id.sb_waring_hr)
    SeekBar sbWaringHr;
    @BindView(R.id.rv_bt_device)
    RecyclerView rvBtDevice;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.ib_menu_setting)
    ImageButton ibMenuSetting;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_waring_hr)
    TextView tvWaringHr;
    @BindView(R.id.sv_loading)
    SpinKitView svLoading;
    @BindView(R.id.tv_bt_total)
    TextView tvBtTotal;
    @BindView(R.id.srl_bt)
    SmartRefreshLayout srlBt;
    @BindView(R.id.ri_urgent_icon)
    RoundImageView riUrgentIcon;
    @BindView(R.id.tv_urgent_name)
    TextView tvUrgentName;
    @BindView(R.id.tv_urgent_tel)
    TextView tvUrgentTel;
    private MyApplication application;
    private UserLoginReturn.PdBean user;
    private BleDeviceAdapter2 bleDeviceAdapter;
    private List<BleDevice> deviceList = new ArrayList<>();
    private BleManager bleManager;

    private boolean isDestroy = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
        bleManager = BleManager.getInstance();
        bleManager.setOperateTimeout(10 * 1000);
    }

    @Override
    protected void initView() {
        //---inflate date
        tvUsername.setText("" + user.getUSERNAME());
        tvHeight.setText("" + user.getUSER_HEIGHT() + " cm");
        tvWeight.setText("" + user.getUSER_WEIGHT() + " kg");
        tvUrgentName.setText("" + user.getURGENT_USER());
        tvUrgentTel.setText("" + user.getURGENT_PHONE());
        String sex = user.getUSER_SEX();
        if (!TextUtils.isEmpty(sex)) {
            switch (sex) {
                case "0":
                    ivSex.setImageResource(R.drawable.ic_male_unchecked);
                    break;
                case "1":
                    ivSex.setImageResource(R.drawable.ic_female_unchecked);
                    break;
            }
        }
        int waringHr = (int) MySPUtils.get(getContext(), "waringHr", -1);
        if (waringHr > 0) {
            tvWaringHr.setText("" + waringHr + " bpm");
            sbWaringHr.setProgress(waringHr);
        }
        srlBt.setEnableLoadMore(false);
        srlBt.setRefreshHeader(new MaterialHeader(getContext()));
        deviceList = BleManager.getInstance().getAllConnectedDevice();//已连接得ble设备
        bleDeviceAdapter = new BleDeviceAdapter2(R.layout.rv_item_bt_device, deviceList);
        rvBtDevice.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBtDevice.setAdapter(bleDeviceAdapter);
    }

    @Override
    protected void initListener() {

        ibMenuSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUserActivity1.startAction(getActivity());
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRegUtils.logout(getActivity());
            }
        });

        sbWaringHr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvWaringHr.setText("" + i + " bpm");
                MySPUtils.put(getContext(), "waringHr", i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bleDeviceAdapter.setOnDeviceConnectListener(new BleDeviceAdapter2.OnDeviceConnectListener() {
            @Override
            public void toConnect(BleDevice bleDevice) {
                connect(bleDevice);
            }

            @Override
            public void toDisConnect(BleDevice bleDevice) {
                bleManager.disconnect(bleDevice);
            }
        });

        srlBt.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startScan();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        // 连接蓝牙设备
        svLoading.setVisibility(View.VISIBLE);
        startScan();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        bleManager.cancelScan();
        svLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
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

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                showToast("连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                showToast("连接成功");
                BleBTUtils.saveConnectDevice(getContext(), bleDevice.getMac());
                EventBus.getDefault().post(new BleConnectMessage(1, bleDevice));
                application.setCurrentBleDevice(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                showToast("取消了连接");
            }
        });

        /*BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleException exception) {
                showToast("连接失败");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                showToast("连接成功");
                BleBTUtils.saveConnectDevice(getContext(), bleDevice.getMac());
                EventBus.getDefault().post(new BleConnectMessage(1, bleDevice));
                application.setCurrentBleDevice(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                showToast("取消了连接");
            }
        });*/
    }

    /**
     * 扫描设备
     */
    private void startScan() {
        deviceList.clear();
        List<BleDevice> connectedDevice = BleManager.getInstance().getAllConnectedDevice();
        deviceList.addAll(connectedDevice);
        tvBtTotal.setText("全部 (" + deviceList.size() + ")");
        bleDeviceAdapter.notifyDataSetChanged();
        bleManager.scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                String dName = bleDevice.getDevice().getName();
                //MyLog.e(TAG, "dName：" + dName);
                if (dName != null) {
                    if (!TextUtils.isEmpty(dName)/*dName.contains("CC") || dName.contains("Sensor") || dName.contains("Tag")*/) {
                        deviceList.add(bleDevice);
                        bleDeviceAdapter.notifyDataSetChanged();
                        tvBtTotal.setText("全部 (" + deviceList.size() + ")");
                    }
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                if (svLoading != null) {
                    svLoading.setVisibility(View.GONE);
                    srlBt.finishRefresh();
                }
            }
        });
    }

    private void showToast(String str) {
        if (!isDestroy) {
            MyToast.showLong(getContext(), str);
        }
    }

}
