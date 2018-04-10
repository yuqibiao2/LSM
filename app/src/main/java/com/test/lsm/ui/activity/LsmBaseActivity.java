package com.test.lsm.ui.activity;

import com.test.lsm.ui.dialog.LoadingDialog;
import com.yyyu.baselibrary.template.BaseActivity;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 功能：LSM项目的BaseActivity
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public abstract class LsmBaseActivity extends BaseActivity {

    private Unbinder mUnbind;
    private LoadingDialog loadingDialog;

    @Override
    public void beforeInit() {
        super.beforeInit();
        mUnbind = ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this);
        StatusBarCompat.compat(this, 0xfff0f0f0);
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

}
