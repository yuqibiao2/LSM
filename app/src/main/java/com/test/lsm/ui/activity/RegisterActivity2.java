package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.clj.fastble.utils.HexUtil;
import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.UserBean;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：注册界面2
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class RegisterActivity2 extends LsmBaseActivity {

    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.et_user_birthday)
    EditText etUserBirthday;
    @BindView(R.id.til_birthday)
    TextInputLayout tilBirthday;
    @BindView(R.id.et_user_height)
    EditText etUserHeight;
    @BindView(R.id.til_height)
    TextInputLayout tilHeight;
    @BindView(R.id.et_user_weight)
    EditText etUserWeight;
    @BindView(R.id.til_weight)
    TextInputLayout tilWeight;

    private String tel;
    private String pwd;

    @Override
    public int getLayoutId() {

        return R.layout.activity_register2;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        Intent intent = getIntent();
        tel = intent.getStringExtra("tel");
        pwd = intent.getStringExtra("pwd");
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initListener() {

    }

    public void toNext(View view) {
        String username = etUsername.getText().toString();
        String birthday = etUserBirthday.getText().toString();
        String height = etUserHeight.getText().toString();
        String weight = etUserWeight.getText().toString();

        if (TextUtils.isEmpty(username)){
            MyToast.showShort(this , "用户名不能为空");
            return;
        }else if (TextUtils.isEmpty(birthday)){
            MyToast.showShort(this , "生日不能为空");
            return;
        }else if (TextUtils.isEmpty(birthday)){
            MyToast.showShort(this , "身高不能为空");
            return;
        }else if (TextUtils.isEmpty(birthday)){
            MyToast.showShort(this , "体重不能为空");
            return;
        }

        UserBean user = new UserBean();
        user.setTel(tel);
        user.setPassword(pwd);
        user.setUsername(username);
        user.setBirthday(birthday);
        user.setHeight(height);
        user.setWeight(weight);

        MySPUtils.put(this , "register_user" , new Gson().toJson(user));

        RegisterActivity3.startAction(this);

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void helloEventBus(String message) {
        if ("register_finished".equals(message)){
            finish();
        }
    }


    public static void startAction(Activity activity, String tel, String pwd) {
        Intent intent = new Intent(activity, RegisterActivity2.class);
        intent.putExtra("tel", tel);
        intent.putExtra("pwd", pwd);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
