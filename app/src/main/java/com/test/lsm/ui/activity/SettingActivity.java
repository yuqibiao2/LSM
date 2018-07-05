package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.test.lsm.R;

/**
 * 功能：设置界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/4
 */
public class SettingActivity extends LsmBaseActivity{

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public static void startAction(Context context){
        Intent intent = new Intent(context , SettingActivity.class);
        context.startActivity(intent);
    }

}
