package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.test.lsm.R;

/**
 * 功能：室内运动
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class IndoorExerciseActivity extends LsmBaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_indoor_exercise;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public static void startAction(Context context , Integer intensiveSelected) {
        Intent intent = new Intent(context, IndoorExerciseActivity.class);
        intent.putExtra("intensiveSelected" , intensiveSelected);
        context.startActivity(intent);
    }

}
