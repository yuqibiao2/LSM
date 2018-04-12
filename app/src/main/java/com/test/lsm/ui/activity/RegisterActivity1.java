package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.test.lsm.R;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：注册界面1
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class RegisterActivity1 extends LsmBaseActivity {

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
        return R.layout.activity_register1;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        LoginRegUtils.checkEdit(tilTel , LoginRegUtils.CheckType.tel);
        LoginRegUtils.checkEdit(tilPwd , LoginRegUtils.CheckType.pwd);
    }

    @Override
    protected void initListener() {

    }

    public void toNext(View view) {
        String tel = etUserTel.getText().toString();
        String pwd = etUserPwd.getText().toString();
        if(TextUtils.isEmpty(tel) || TextUtils.isEmpty(pwd)){
            MyToast.showShort(this , "输入存在空值！");
            return;
        }
        if (tilTel.isErrorEnabled() || tilPwd.isErrorEnabled()){
            MyToast.showShort(this , "输入数据格式不正确");
            return;
        }

        RegisterActivity2.startAction(this , tel , pwd);
        //finish();
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity1.class);
        activity.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void helloEventBus(String message) {
        if ("register_finished".equals(message)){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
