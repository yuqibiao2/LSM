package com.test.lsm.ui.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.ui.dialog.BleBtDeviceDisconnectDialog;
import com.test.lsm.ui.dialog.EmergencyContactDialog;
import com.test.lsm.ui.dialog.LoadingDialog;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.test.lsm.utils.logic.EmergencyContactJudge;
import com.yyyu.baselibrary.template.BaseActivity;
import com.yyyu.baselibrary.utils.MyActUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.test.lsm.global.SpConstant.WARING_HR;

/**
 * 功能：LSM项目的BaseActivity
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public abstract class LsmBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = "LsmBaseActivity";

    private Unbinder mUnbind;
    private LoadingDialog loadingDialog;

    @Override
    public void beforeInit() {
        super.beforeInit();
        //设置所有Activity竖屏
        setActPortrait();
        mUnbind = ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this);
        if (setDefaultStatusBarCompat()) {
            StatusBarCompat.compat(this, 0xfff0f0f0);
        }

        //判斷心率 彈出緊急聯係人彈窗
        final MyApplication application = (MyApplication) getApplication();
        application.setOnGetHrValueListener(new MyApplication.OnGetHrValueListener() {
            @Override
            public void onGet(int hrValue) {
                int waringHr = (int) MySPUtils.get(LsmBaseActivity.this, WARING_HR, 180);
                boolean judge = EmergencyContactJudge.doJudge(hrValue, waringHr);
                if (judge && !EmergencyContactDialog.isShow && MyActUtils.isTopAct(LsmBaseActivity.this)) {
                    EmergencyContactDialog emergencyContactDialog = new EmergencyContactDialog(LsmBaseActivity.this);
                    emergencyContactDialog.show();
                    //发送通知
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(LsmBaseActivity.this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("BeatInfo")
                            .setContentText("心率異常警示！");
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(123, builder.build());

                }
            }

        });

        //---蓝牙断开提示框
        BleManager.getInstance().setOnConnectDismissListener(new BleManager.OnConnectDismiss() {
            @Override
            public void dismiss(BleDevice device) {
                if (MyActUtils.isTopAct(LsmBaseActivity.this)){
                    BleBtDeviceDisconnectActivity.startAction(LsmBaseActivity.this);
                }
            }
        });

    }

    protected boolean setDefaultStatusBarCompat() {

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbind != null) {
            mUnbind.unbind();
        }
        dismissLoadDialog();
    }


    public void showLoadDialog() {
        loadingDialog.show();
    }

    public void showLoadDialog(String tipStr) {
        loadingDialog.show(tipStr);
    }

    protected void hiddenLoadDialog() {
        loadingDialog.hide();
    }

    protected void dismissLoadDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected void toRequestPermission() {
        String[] perms = {Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this,
                    "部分必須權限沒有開啓，可能導致部分功能無法使用，是否開啓？",
                    10001,
                    perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


}
