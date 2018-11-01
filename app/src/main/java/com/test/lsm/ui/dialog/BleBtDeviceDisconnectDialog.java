package com.test.lsm.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.test.lsm.R;

import butterknife.BindView;

/**
 * 功能：
 *
 *
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/1
 */
public class BleBtDeviceDisconnectDialog extends LsmBaseDialog {

    @BindView(R.id.tv_sure)
    TextView tvSure;

    public  static  boolean isShow = false;

    public BleBtDeviceDisconnectDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ble_bt_device_disconnect;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void dismiss() {
        if (isShow){
            super.dismiss();
        }
        isShow = false;
    }

    @Override
    public void show() {
        if (!isShow){
            super.show();
        }
        isShow = true;
    }

}
