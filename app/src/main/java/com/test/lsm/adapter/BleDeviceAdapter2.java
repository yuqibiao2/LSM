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
import com.yyyu.baselibrary.ui.widget.SwitchCompatWrapper;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/4
 */
public class BleDeviceAdapter2 extends BaseQuickAdapter<BleDevice, BaseViewHolder> {

    private final BleManager bleManager;

    public BleDeviceAdapter2(int layoutResId, @Nullable List<BleDevice> data) {
        super(layoutResId, data);
        bleManager = BleManager.getInstance();
    }

    @Override
    protected void convert(BaseViewHolder helper, final BleDevice item) {

        String dName = ""+item.getName();
        ImageView ivDeviceIcon = helper.getView(R.id.iv_bt_device_icon);
        if (dName.contains("CC") || dName.contains("Sensor") || dName.contains("Tag")) {
            Glide.with(mContext).load(R.mipmap.ic_cca10).into(ivDeviceIcon);
        }else if(dName.contains("watch")||dName.contains("Watch")||dName.contains("WATCH")){
            Glide.with(mContext).load(R.mipmap.ic_iwatch).into(ivDeviceIcon);
        }
        helper.setText(R.id.tv_bt_name, item.getName());
        helper.setText(R.id.tv_bt_mac, item.getMac());
        final SwitchCompat scDevice = helper.getView(R.id.sc_device);
        final SwitchCompatWrapper switchCompatWrapper = new SwitchCompatWrapper(scDevice);
        if (bleManager.isConnected(item)){
            switchCompatWrapper.setCheckedNotCallbackChgEvent(true);
        }
        switchCompatWrapper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {// 连接
                    if (!bleManager.isConnected(item)){
                        mOnDeviceConnectListener.toConnect(item , switchCompatWrapper);
                    }
                } else {//取消连接
                    if (bleManager.isConnected(item)){
                        mOnDeviceConnectListener.toDisConnect(item , switchCompatWrapper);
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

        void toConnect(BleDevice bleDevice, SwitchCompatWrapper switchCompat);

        void toDisConnect(BleDevice bleDevice , SwitchCompatWrapper switchCompat);

    }

}
