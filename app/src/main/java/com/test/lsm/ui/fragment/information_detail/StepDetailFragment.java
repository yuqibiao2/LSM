package com.test.lsm.ui.fragment.information_detail;

import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.test.lsm.R;
import com.test.lsm.bean.event.StepChgEvent;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.StepService;
import com.test.lsm.db.service.inter.IStepService;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.ui.wdiget.MyMarkerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：步数详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/8
 */
public class StepDetailFragment extends LsmBaseFragment {

    @BindView(R.id.bc_step_num)
    BarChart bcStepNum;
    @BindView(R.id.v_status1)
    View vStatus1;
    @BindView(R.id.v_status3)
    View vStatus3;
    @BindView(R.id.v_status2)
    View vStatus2;
    private List<View> statusList;
    private IStepService stepService;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        stepService = new StepService();
    }

    @Override
    public int getLayoutId() {

        return R.layout.fragment_step_detail;
    }

    @Override
    protected void initView() {
        statusList = new ArrayList<>(3);
        statusList.add(vStatus1);
        statusList.add(vStatus2);
        statusList.add(vStatus3);
        setStatus(0);
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @Override
    protected void initListener() {
        bcStepNum.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                RectF bounds = mOnValueSelectedRectF;
                bcStepNum.getBarBounds((BarEntry) e, bounds);
                MPPointF position = bcStepNum.getPosition(e, YAxis.AxisDependency.LEFT);

                MPPointF.recycleInstance(position);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        setData();
    }

    private void setData( ) {
        List<Step> currentDatStep = stepService.getCurrentDateStep();

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < 24; i++) {
            int val = 0;
            yVals.add(new BarEntry(i, (int) val));
        }

        for (int i=0 ; i< currentDatStep.size() ; i++){
            Step step = currentDatStep.get(i);
            int hour = step.getHour();
            BarEntry barEntry = yVals.get(hour);
            barEntry.setY(step.getStepNum());
        }


        BarDataSet set = new BarDataSet(yVals, "步数");
        set.setColors(getResources().getColor(R.color.chart_bar));
        set.setDrawValues(false);//不显示value
        set.setShowConner(true);
        set.setHighLightColor(Color.rgb(254, 199, 109));
        BarData data = new BarData(set);

        bcStepNum.setData(data);
        bcStepNum.invalidate();
        bcStepNum.animateY(800);
        bcStepNum.setScaleEnabled(false);
        bcStepNum.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);//设置X轴的位置
        bcStepNum.getXAxis().setDrawGridLines(false);//不显示网格
        bcStepNum.getXAxis().setDrawAxisLine(false);
        bcStepNum.getAxisRight().setEnabled(false);//右侧不显示Y轴
        bcStepNum.getAxisLeft().setEnabled(false);
        bcStepNum.getXAxis().setLabelCount(yVals.size());
        bcStepNum.getDescription().setEnabled(false);//不设置描述
        bcStepNum.getAxisLeft().setDrawGridLines(true);//不设置Y轴网格
        bcStepNum.getAxisLeft().setAxisMinValue(0.0f);
        bcStepNum.setDrawValueAboveBar(true);
        //---设置marker
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view2);
        mv.setChartView(bcStepNum);
        bcStepNum.setMarker(mv);  //设置 marker ,点击后显示的功能 ，布局可以自定义
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateStep(StepChgEvent stepChgEvent){
        int stepNum = stepChgEvent.getStep();
        if (stepNum<10){
            setStatus(0);
        }else if (stepNum<100){
            setStatus(1);
        }else if(stepNum<1000){
            setStatus(2);
        }else if(stepNum<10000){
            setStatus(3);
        }else{
            setStatus(3);
        }
        setData();
    }

    public void setStatus(int index){
        for (int i =0 ; i<statusList.size() ; i++){
            View view = statusList.get(i);
            if (index-1==i){
                view.setEnabled(true);
            }else{
                view.setEnabled(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
