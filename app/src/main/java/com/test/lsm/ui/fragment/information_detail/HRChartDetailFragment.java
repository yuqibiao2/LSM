package com.test.lsm.ui.fragment.information_detail;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.test.lsm.R;
import com.test.lsm.global.Constant;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyLog;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/6/11
 */
public class HRChartDetailFragment extends LsmBaseFragment {

    private static final String TAG = "HRChartDetailFragment";

    @BindView(R.id.bc_hr)
    LineChart bcHr;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initChartData();
        }
    };

    @Override
    protected void beforeInit() {
        super.beforeInit();
    }

    @Override
    public int getLayoutId() {

        return R.layout.fragment_hr_chart_detail;
    }

    @Override
    protected void initView() {
        initChart();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        initChartData();
    }

    private void initChartData() {
        MyLog.e(TAG , "initChartData===========");
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        CircularFifoQueue<Integer> hrBuffer = Constant.hrBuffer2;
       /* for (int i = 0; i < 200; i++) {
            int nextInt = new Random().nextInt(100);
            int value = 100 + nextInt;
            hrBuffer.add(value);
        }*/
        for (int i = 0; i < hrBuffer.size(); i++) {
            yVals.add(new Entry(i, hrBuffer.get(i)));
        }
        mHandler.sendEmptyMessageDelayed(0 , 2000);
        if (yVals.size()==0 ){return;}
        // 没有展开 return
        if (!Constant.isHRChartDetailShow){ return;}
        ArrayList<ILineDataSet> dataSetList = new ArrayList<>();

        LineDataSet set1;

        set1 = new LineDataSet(yVals, "心跳");
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setColor(Color.WHITE);
        set1.setColor(Color.parseColor("#4573C6"));
        dataSetList.add(set1);

        LineData data = new LineData(dataSetList);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
        bcHr.setData(data);
        //bcHr.animateXY(3000, 0);
        bcHr.invalidate();
    }

    private void initChart() {
        bcHr.setTouchEnabled(false);
        bcHr.setScaleEnabled(false);
        bcHr.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
        bcHr.getXAxis().setDrawGridLines(false);//不显示网格
        bcHr.getAxisRight().setEnabled(false);//右侧不显示Y轴
        bcHr.getDescription().setEnabled(false);//不设置描述
        bcHr.getAxisLeft().setDrawGridLines(true);//不设置Y轴网格
        //bcHr.getAxisLeft().setAxisMinValue(0.0f);
    }



}
