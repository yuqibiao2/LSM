package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.test.lsm.R;
import com.test.lsm.bean.json.GetUserInfoReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
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
        //LoginRegUtils.checkEdit(tilTel , LoginRegUtils.CheckType.tel);
        //LoginRegUtils.checkEdit(tilPwd , LoginRegUtils.CheckType.pwd);
    }

    @Override
    protected void initListener() {

    }

    public void toNext(View view) {
        final String tel = etUserTel.getText().toString();
        final String pwd = etUserPwd.getText().toString();
        if(TextUtils.isEmpty(tel) || TextUtils.isEmpty(pwd)){
            MyToast.showShort(this , getStr(R.string.input_empty));
            return;
        }
        if (tilTel.isErrorEnabled() || tilPwd.isErrorEnabled()){
            MyToast.showShort(this , getStr(R.string.input_illegal));
            return;
        }

        //判断手机号是否已经被注册
        showLoadDialog();
        APIMethodManager.getInstance().getUserInfoByUsername(provider, tel, new IRequestCallback<GetUserInfoReturn>() {
            @Override
            public void onSuccess(GetUserInfoReturn result) {
                dismissLoadDialog();
                String code = result.getResult();
                if ("02".equals(code)){//没有被注册
                    RegisterActivity2.startAction(RegisterActivity1.this , tel , pwd);
                }else{
                    MyToast.showLong(RegisterActivity1.this , getStr(R.string.account_is_already_exist));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(RegisterActivity1.this , getStr(R.string.net_error));
                dismissLoadDialog();
            }
        });


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
