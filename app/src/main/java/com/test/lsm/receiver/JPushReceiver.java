package com.test.lsm.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.jpushdemo.MyReceiver;
import com.test.lsm.bean.event.RefreshTodayMsg;
import com.test.lsm.ui.activity.ExerciseRankingActivity;
import com.test.lsm.ui.activity.MsgDetailActivity;
import com.yyyu.baselibrary.utils.MyLog;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

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
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if ("心力挑戰名人賽".trim().equals(content)){
            ExerciseRankingActivity.startAction(context);
        }else{
            MsgDetailActivity.startAction(context , bundle);
        }
        EventBus.getDefault().post(new RefreshTodayMsg());
    }

}
