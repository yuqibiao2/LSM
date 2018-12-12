package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.test.lsm.R;

import butterknife.BindView;

/**
 * 功能：蓝牙断开弹窗
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/1
 */
public class BleBtDeviceDisconnectActivity extends LsmBaseActivity {

    @BindView(R.id.tv_sure)
    TextView tvSure;

    @Override
    public int getLayoutId() {
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
                finish();
            }
        });
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context , BleBtDeviceDisconnectActivity.class);
        context.startActivity(intent);
    }

}
