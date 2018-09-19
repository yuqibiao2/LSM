package com.test.lsm.ui.activity;

import android.Manifest;
import android.support.annotation.NonNull;

import com.test.lsm.ui.dialog.LoadingDialog;
import com.yyyu.baselibrary.template.BaseActivity;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 功能：LSM项目的BaseActivity
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public abstract class LsmBaseActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private Unbinder mUnbind;
    private LoadingDialog loadingDialog;

    @Override
    public void beforeInit() {
        super.beforeInit();
        mUnbind = ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this);
        if (setDefaultStatusBarCompat()){
            StatusBarCompat.compat(this, 0xfff0f0f0);
        }
    }

    protected  boolean setDefaultStatusBarCompat(){

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
        String[] perms = {Manifest.permission.CAMERA ,
                Manifest.permission.ACCESS_FINE_LOCATION ,
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
