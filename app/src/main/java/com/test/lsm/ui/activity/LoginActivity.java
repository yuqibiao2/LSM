package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.UserBean;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

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
        LoginRegUtils.checkEdit(tilTel, LoginRegUtils.CheckType.tel);
        LoginRegUtils.checkEdit(tilPwd, LoginRegUtils.CheckType.pwd);
    }

    @Override
    protected void initListener() {

    }

    public void toLogin(View view) {

        String tel = etUserTel.getText().toString();
        String pwd = etUserPwd.getText().toString();

        showLoadDialog("登录中....");

        String userStr = (String) MySPUtils.get(this, "register_user", "");
        UserBean userBean = new Gson().fromJson(userStr, UserBean.class);
        if (userBean == null) {
            MyToast.showShort(this, "该用户不存在");
        } else {
            if (pwd.equals(userBean.getPassword()) && tel.equals(userBean.getTel())){
                MainActivity.startAction(this);
                finish();
            }else{
                MyToast.showShort(this, "用户名或密码错误");
            }
        }
        hiddenLoadDialog();
    }

    public void toRegister(View view) {
        RegisterActivity1.startAction(this);
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
