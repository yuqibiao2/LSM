package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swm.algorithm.Algorithm;
import com.swm.algorithm.support.IirFilter;
import com.test.lsm.R;
import com.test.lsm.bean.event.ECGChgEvent;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.global.Constant;
import com.yyyu.baselibrary.ui.widget.EcgView;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：心电图展示
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/9
 */
public class ECGShowActivity2 extends LsmBaseActivity {

    private List<Integer> datas = new ArrayList<Integer>();

    private static final String TAG = "ECGShowActivity";

    @BindView(R.id.ib_nav_lit)
    ImageButton ibNavLit;
    @BindView(R.id.tv_max_hr)
    TextView tvMaxHr;
    @BindView(R.id.tv_hr)
    TextView tvHr;
    @BindView(R.id.tv_avg_hr)
    TextView tvAvgHr;
    private IirFilter iirFilter;

    private int minHeart = Integer.MAX_VALUE;
    private int maxHeart = Integer.MIN_VALUE;

    private Queue<Integer> data0Q = new LinkedList<Integer>();
    private Queue<Integer> data1Q = new LinkedList<Integer>();

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
                    //tvMinHr.setText(""+minHeart);
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
        return R.layout.activity_ecg_show2;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        iirFilter = Algorithm.newIirFilterInstance();
        EventBus.getDefault().register(this);
        simulator();
        try{
            String data0;
            InputStream in = getResources().openRawResource(R.raw.ecgdata);
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
            data0 = new String(buffer);
            in.close();
            String[] data0s = data0.split(",");
            for(String str : data0s){
                datas.add(Integer.parseInt(str));
            }
            data0Q.addAll(datas);
            data1Q.addAll(datas);
        }catch (Exception e){

        }


    }

    private void simulator(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(EcgView.isRunning){
                    if(data0Q.size() > 0){
                        EcgView.addEcgData0(data0Q.poll());
                    }
                }
            }
        }, 0, 2);
    }


    @Override
    protected boolean setDefaultStatusBarCompat() {
        StatusBarCompat.compat(this, 0xff000000);
        return false;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {

    }


    private boolean flag = true;

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

    private int takeIndex = 0;

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateECG(ECGChgEvent ecgChgEvent) {
       /* short[] ecgValue = ecgChgEvent.getEcgValue();
        for (short value : ecgValue){
            Integer filter = iirFilter.filter(Integer.valueOf(value));
            MyLog.e(TAG , "filter==="+filter);
            EcgView.addEcgData0(filter);
        }*/
      /*  if (takeIndex==datas.size()){
            takeIndex = 0;
        }
        EcgView.addEcgData0(datas.get(takeIndex));*/
    }

    public static void startAction(Activity activity) {
        activity.startActivity(new Intent(activity, ECGShowActivity2.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
        EventBus.getDefault().unregister(this);
    }
}
