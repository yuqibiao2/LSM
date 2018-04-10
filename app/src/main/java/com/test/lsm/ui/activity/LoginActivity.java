package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.test.lsm.R;
import com.test.lsm.utils.LoginRegUtils;

import butterknife.BindView;

/**
 * 功能：登录界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class LoginActivity extends LsmBaseActivity {

    @BindView(R.id.et_user_tel)
    EditText etUserTel;
    @BindView(R.id.til_tel)
    TextInputLayout tilTel;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void toLogin(View view) {
        LoginRegUtils.checkEdit(tilTel , LoginRegUtils.CheckType.tel);
        LoginRegUtils.checkEdit(tilPwd , LoginRegUtils.CheckType.pwd);
        showLoadDialog("登录中....");
        MainActivity.startAction(this);
    }

    public void toRegister(View view) {
        RegisterActivity1.startAction(this);
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
