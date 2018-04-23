package com.test.lsm.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.WindowUtils;

import butterknife.BindView;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
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
    @BindView(R.id.iv_female)
    ImageView ivFemale;
    private UserLoginReturn.PdBean loginUser;
    private MyApplication application;
    private BleDevice currentBleDevice;

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
        currentBleDevice = application.getCurrentBleDevice();
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
        if (currentBleDevice != null) {
            String name = currentBleDevice.getDevice().getName();
            tvDeviceName.setText("" + name);
        } else {
            tvDeviceName.setText("未连接");
        }
        if (LoginRegUtils.isLogin(mContext)) {
            String user_sex = application.getUser().getUSER_SEX();
            if ("0".equals(user_sex)) {
                ivFemale.setImageResource(R.drawable.ic_male_checked1);
            } else if ("1".equals(user_sex)) {
                ivFemale.setImageResource(R.drawable.ic_female_checked);
            }
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

    @Override
    protected void afterInit() {
        super.afterInit();

        if (currentBleDevice == null || !BleManager.getInstance().isConnected(currentBleDevice)) {
            MyToast.showLong(mContext, "蓝牙设备未连接");
        } else {
            BleManager.getInstance().read(
                    currentBleDevice,
                    "0000180f-0000-1000-8000-00805f9b34fb".toLowerCase(),
                    "00002a19-0000-1000-8000-00805f9b34fb".toLowerCase(),
                    new BleReadCallback() {
                        @Override
                        public void onReadSuccess(byte[] data) {
                            // 读特征值数据成功
                            String hexStr = HexUtil.encodeHexStr(data);
                            int ecgValue = Integer.parseInt(hexStr.substring(0, 2), 16);
                            tvElectricQuantity.setText("" + ecgValue + "%");
                            MyLog.e(TAG, "ecgValue=" + ecgValue);
                        }

                        @Override
                        public void onReadFailure(BleException exception) {
                            // 读特征值数据失败
                            MyLog.e(TAG, "读取电量特性信息失败：" + exception.getDescription());
                        }
                    });
        }
    }
}
