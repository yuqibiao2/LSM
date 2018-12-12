package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.event.ECGChgEvent;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.global.Constant;
import com.test.lsm.ui.wdiget.CompositeView;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/6/19
 */
public class ECGShowActivity3 extends LsmBaseActivity {

    @BindView(R.id.tv_max_hr)
    TextView tvMaxHr;
    @BindView(R.id.tv_avg_hr)
    TextView tvAvgHr;
    @BindView(R.id.tv_hr)
    TextView tvHr;
    @BindView(R.id.tb_scan)
    Toolbar tbScan;
    @BindView(R.id.ecg_hr)
    CompositeView ecgHr;

    private int minHeart = Integer.MAX_VALUE;
    private int maxHeart = Integer.MIN_VALUE;

    private boolean flag = true;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (flag){
                CircularFifoQueue<Integer> oneMinHeart = Constant.hrBuffer;
                int total = 0;
                for (Integer heartNum : oneMinHeart) {
                    if (heartNum>maxHeart){
                        maxHeart = heartNum;
                    }
                    if (heartNum<minHeart){
                        minHeart = heartNum;
                    }
                    total += heartNum;
                }
                int size = oneMinHeart.size();
                int avgHearNum =size>0? total /size : 0;
                tvAvgHr.setText(""+avgHearNum);
                if (minHeart<Integer.MAX_VALUE){
                    tvAvgHr.setText(""+minHeart);
                }
                if (maxHeart>Integer.MIN_VALUE){
                    tvMaxHr.setText(""+maxHeart);
                }
            }
            mHandler.sendEmptyMessageDelayed(0 , 2*1000);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_ecg_show3;
    }

    @Override
    protected boolean setDefaultStatusBarCompat() {
        StatusBarCompat.compat(this, 0xff000000);
        return false;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        mHandler.sendEmptyMessageDelayed(0 , 2*1000);
    }

    public void back(View view) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateHeart(HeartChgEvent heartChgEvent) {
        tvHr.setText(""+heartChgEvent.getHeartNUm());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateECG(ECGChgEvent ecgChgEvent) {
        short[] ecgValue = ecgChgEvent.getEcgValue();
        ecgHr.addEcgRawData(ecgValue , 0 , 0);
    }

    public static void startAction(Activity activity) {
        activity.startActivity(new Intent(activity, ECGShowActivity3.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
        EventBus.getDefault().unregister(this);
    }
}
