package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.test.lsm.R;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：室内运动
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class IndoorExerciseActivity extends LsmBaseActivity {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rv_user_icon)
    RoundImageView rvUserIcon;
    @BindView(R.id.rr_bt)
    RelativeLayout rrBt;
    @BindView(R.id.cc_ht)
    CombinedChart ccHt;
    private List<BarEntry> mValues;

    @Override
    public int getLayoutId() {
        return R.layout.activity_indoor_exercise;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        mValues = new ArrayList<>();
        mValues.add(new BarEntry(0, new float[]{0, 60}));
        mValues.add(new BarEntry(1, new float[]{0, 60}));
        mValues.add(new BarEntry(2, new float[]{0, 60}));
        mValues.add(new BarEntry(3, new float[]{0, 60}));
        mValues.add(new BarEntry(4, new float[]{0, 60}));
        mValues.add(new BarEntry(5, new float[]{60, 60}));
        mValues.add(new BarEntry(6, new float[]{60, 60}));
        mValues.add(new BarEntry(7, new float[]{60, 60}));
        mValues.add(new BarEntry(8, new float[]{120, 60}));
        mValues.add(new BarEntry(9, new float[]{120, 60}));
        mValues.add(new BarEntry(10, new float[]{120, 60}));
        mValues.add(new BarEntry(11, new float[]{60, 60}));
        mValues.add(new BarEntry(12, new float[]{60, 60}));
        mValues.add(new BarEntry(13, new float[]{60, 60}));
        mValues.add(new BarEntry(14, new float[]{0, 60}));
        mValues.add(new BarEntry(15, new float[]{0, 60}));
        mValues.add(new BarEntry(16, new float[]{0, 60}));
        mValues.add(new BarEntry(17, new float[]{0, 60}));
        mValues.add(new BarEntry(18, new float[]{0, 60}));
        mValues.add(new BarEntry(19, new float[]{0, 60}));
        mValues.add(new BarEntry(20, new float[]{0, 60}));
        mValues.add(new BarEntry(21, new float[]{0, 60}));
        mValues.add(new BarEntry(22, new float[]{0, 60}));
        mValues.add(new BarEntry(23, new float[]{0, 60}));
        mValues.add(new BarEntry(24, new float[]{0, 60}));
        mValues.add(new BarEntry(25, new float[]{0, 60}));
        mValues.add(new BarEntry(26, new float[]{0, 60}));
        mValues.add(new BarEntry(27, new float[]{0, 60}));
        mValues.add(new BarEntry(28, new float[]{0, 60}));
        mValues.add(new BarEntry(29, new float[]{0, 60}));
        mValues.add(new BarEntry(30, new float[]{0, 60}));
        for (int i = 0; i < 60; i++) {
            mValues.add(new BarEntry(30 + i, new float[]{0, 60}));
        }
    }

    @Override
    protected void initView() {
        initChart(ccHt);
        setChartData(ccHt, mValues);

        CombinedData data = ccHt.getData();
        BarDataSet set = (BarDataSet) data.getDataSetByIndex(0);
        data.notifyDataChanged();
        ccHt.notifyDataSetChanged();
        ccHt.setVisibleXRangeMaximum(30);
        ccHt.moveViewTo(set.getEntryCount()-31, 50f, YAxis.AxisDependency.LEFT);
    }

    @Override
    protected void initListener() {
        rvUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry();
            }
        });
    }

    private void addEntry() {


        CombinedData data = ccHt.getData();
        BarDataSet set = (BarDataSet) data.getDataSetByIndex(0);
        BarEntry barEntry = new BarEntry(set.getEntryCount()+1, new float[]{120, 60});
        set.addEntry(barEntry);
        set.notifyDataSetChanged();
        data.notifyDataChanged();
        ccHt.notifyDataSetChanged();
        ccHt.setVisibleXRangeMaximum(30);
        ccHt.moveViewTo(set.getEntryCount()-31, 50f, YAxis.AxisDependency.LEFT);

        MyLog.e("set.getEntryCount()==="+set.getEntryCount());

    }

    private void setChartData(CombinedChart mLineChart, List<BarEntry> mValues) {
        BarDataSet set1;
        //判断图表中原来是否有数据
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (BarDataSet) mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(mValues);
            //刷新数据
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(mValues, "");
            set1.setDrawValues(false);
            set1.setColors(new int[]{Color.argb(0, 0, 0, 0),
                    Color.rgb(23, 197, 255)});
            set1.setValueTextColor(Color.rgb(61, 165, 255));
            set1.setValueTextSize(10f);
            //set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            CombinedData data = new CombinedData();
            BarData barData = new BarData(set1);
            data.setData(barData);
            mLineChart.setData(data);
            data.notifyDataChanged();
            mLineChart.notifyDataSetChanged();
            //绘制图表
            mLineChart.invalidate();
        }

    }


    protected void initChart(CombinedChart mChart) {
        mChart.setNoDataText("没有数据");//没有数据时显示的文字
        mChart.setTouchEnabled(true);     //能否点击
        mChart.setDragEnabled(true);   //能否拖拽
        mChart.setScaleEnabled(false);  //能否缩放
        mChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        Legend l = mChart.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.parseColor("#242536"));
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setVisibleXRangeMaximum(6);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
    }

    public static void startAction(Context context, Integer intensiveSelected) {
        Intent intent = new Intent(context, IndoorExerciseActivity.class);
        intent.putExtra("intensiveSelected", intensiveSelected);
        context.startActivity(intent);
    }

}
