package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.test.lsm.R;

/**
 * 功能：注册界面1
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class RegisterActivity1 extends LsmBaseActivity {
    @Override
    public int getLayoutId() {

        return R.layout.activity_register1;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void toNext(View view){
        RegisterActivity2.startAction(this);
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity,RegisterActivity1.class);
        activity.startActivity(intent);
    }

}
