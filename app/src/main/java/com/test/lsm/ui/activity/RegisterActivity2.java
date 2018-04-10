package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.test.lsm.R;

/**
 * 功能：注册界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class RegisterActivity2 extends LsmBaseActivity {
    @Override
    public int getLayoutId() {

        return R.layout.activity_register2;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void toNext(View view){

    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity,RegisterActivity2.class);
        activity.startActivity(intent);
    }

}
