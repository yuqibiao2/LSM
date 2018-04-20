package com.test.lsm.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.UserLoginReturn;
import com.yyyu.baselibrary.utils.WindowUtils;

import butterknife.BindView;

import static com.test.lsm.utils.LoginRegUtils.getLoginUser;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/27
 */

public class DeviceInformationDialog extends LsmBaseDialog {


    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_urgent_username)
    TextView tvUrgentUsername;
    @BindView(R.id.tv_urgent_tel)
    TextView tvUrgentTel;
    @BindView(R.id.ll_urgent)
    LinearLayout llUrgent;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_electric_quantity)
    TextView tvElectricQuantity;
    private UserLoginReturn.PdBean loginUser;
    private MyApplication application;

    public DeviceInformationDialog(Context context) {
        super(context);
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = WindowUtils.getSize(mContext)[0] / 4 * 3;
        lp.height = WindowUtils.getSize(mContext)[1] / 3 * 2;
        return lp;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        loginUser = getLoginUser(getContext());
        application = (MyApplication) ((Activity) mContext).getApplication();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_device_information;
    }

    @Override
    protected void initView() {
        tvUsername.setText(loginUser.getUSERNAME());
        tvHeight.setText(loginUser.getUSER_HEIGHT() + " cm");
        tvUrgentUsername.setText(loginUser.getURGENT_USER());
        tvUrgentTel.setText(loginUser.getURGENT_PHONE());
        BleDevice currentBleDevice = application.getCurrentBleDevice();
        if (currentBleDevice != null) {
            String name = currentBleDevice.getDevice().getName();
            tvDeviceName.setText(""+name);
        }
    }

    @Override
    protected void initListener() {
        llUrgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urgent_phone = loginUser.getURGENT_PHONE();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + urgent_phone.trim()));
                mContext.startActivity(intent);
            }
        });
    }
}
