package com.test.lsm.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.jpushdemo.MyReceiver;
import com.test.lsm.ui.activity.MsgDetailActivity;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/14
 */
public class JPushReceiver extends MyReceiver{

    private static final String TAG = "JPushReceiver";

    @Override
    protected void onMsgClicked(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        MsgDetailActivity.startAction(context , bundle);
    }

}
