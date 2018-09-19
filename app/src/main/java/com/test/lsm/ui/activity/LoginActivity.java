package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.example.jpushdemo.TagAliasOperatorHelper;
import com.google.gson.Gson;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;

import static com.example.jpushdemo.TagAliasOperatorHelper.ACTION_SET;
import static com.example.jpushdemo.TagAliasOperatorHelper.sequence;

/**
 * 功能：登录界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class LoginActivity extends LsmBaseActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_user_tel)
    EditText etUserTel;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    private APIMethodManager apiMethodManager;
    private Gson mGson;
    private MyApplication application;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();

        toRequestPermission();

        application = (MyApplication) getApplication();
        apiMethodManager = APIMethodManager.getInstance();
        mGson = new Gson();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void toLogin(View view) {


        String tel = etUserTel.getText().toString();
        String pwd = etUserPwd.getText().toString();

        showLoadDialog(getStr(R.string.login_loading));

        apiMethodManager.login(tel, pwd, new IRequestCallback<UserLoginReturn>() {
            @Override
            public void onSuccess(UserLoginReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {//登录成功
                    UserLoginReturn.PdBean pd = result.getPd();

                    setJPushAlias("" + pd.getUSER_ID());

                    String pdStr = mGson.toJson(pd);
                    //保存用户信息
                    MySPUtils.put(LoginActivity.this, LoginRegUtils.USER_INFO, pdStr);
                    application.setUser(LoginRegUtils.getLoginUser(LoginActivity.this));
                    MainActivity.startAction(LoginActivity.this);
                    finish();
                } else /*if("07".equals(code))*/ {//用户名密码错误
                    MyToast.showShort(LoginActivity.this, getStr(R.string.username_or_pwd_error));
                }
                hiddenLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                hiddenLoadDialog();
                MyToast.showShort(LoginActivity.this, "网络异常：" + throwable.getMessage());
            }
        });

    }

    private void setJPushAlias(String userId) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = userId;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), 1, tagAliasBean);
    }

    public void toRegister(View view) {
        RegisterActivity1.startAction(this);
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
