package com.test.lsm.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.test.lsm.R;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/4
 */
public class BleDeviceAdapter2 extends BaseQuickAdapter<BleDevice, BaseViewHolder> {

    private SwitchCompat scDevice;
    private final BleManager bleManager;

    public BleDeviceAdapter2(int layoutResId, @Nullable List<BleDevice> data) {
        super(layoutResId, data);
        bleManager = BleManager.getInstance();
    }

    @Override
    protected void convert(BaseViewHolder helper, final BleDevice item) {

        String dName = item.getName();
        if (dName.contains("CC") || dName.contains("Sensor") || dName.contains("Tag")) {
            ImageView ivDeviceIcon = helper.getView(R.id.iv_bt_device_icon);
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(ivDeviceIcon);
        }
        helper.setText(R.id.tv_bt_name, item.getName());
        helper.setText(R.id.tv_bt_mac, item.getMac());
        scDevice = helper.getView(R.id.sc_device);
        if (bleManager.isConnected(item)){
            scDevice.setChecked(true);
        }
        scDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {// 连接
                    if (!bleManager.isConnected(item)){
                        mOnDeviceConnectListener.toConnect(item);
                    }
                } else {//取消连接
                    if (bleManager.isConnected(item)){
                        mOnDeviceConnectListener.toDisConnect(item);
                    }
                }
            }
        });
    }

    OnDeviceConnectListener mOnDeviceConnectListener;

    public void setOnDeviceConnectListener(OnDeviceConnectListener onDeviceConnectListener){
        this.mOnDeviceConnectListener = onDeviceConnectListener;
    }

    public interface OnDeviceConnectListener {

        void toConnect(BleDevice bleDevice);

        void toDisConnect(BleDevice bleDevice);

    }

}
