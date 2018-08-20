package com.test.lsm.ui.fragment.indoor_run;

import android.graphics.Color;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.code.microlog4android.format.command.util.StringUtil;
import com.test.lsm.R;
import com.test.lsm.bean.event.UpdateIndoorRunDataEvent;
import com.test.lsm.ui.activity.IndoorExerciseActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.test.lsm.ui.activity.IndoorExerciseActivity.maxBaseHr;

/**
 * 功能：室内运动结束后心率
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/20
 */
public class IndoorRunHrFragment extends LsmBaseFragment {

    @BindView(R.id.cc_ht)
    CombinedChart ccHt;
    private List<Entry> lineValues;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_indoor_run_hr;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        lineValues = new ArrayList<>();
        lineValues.add(new Entry(0, maxBaseHr*0.5f));
    }

    @Override
    protected void initView() {
        initChart(ccHt);
    }

    @Override
    protected void initListener() {

    }


    public void resetLineChart(){
        CombinedData data = ccHt.getData();
        LineDataSet lineDataSet = (LineDataSet) data.getDataSetByIndex(0);
        lineDataSet.clear();
        lineDataSet.addEntry(new Entry(0, maxBaseHr * 0.5f));
        lineDataSet.notifyDataSetChanged();
        data.notifyDataChanged();
        ccHt.moveViewTo( 0, 50f, YAxis.AxisDependency.LEFT);
    }

    public void addLineEntry(int hrValue) {
        CombinedData data = ccHt.getData();
        LineDataSet lineDataSet = (LineDataSet) data.getDataSetByIndex(0);
        int i = lineDataSet.getEntryCount() - 1;
        final float x = i / 30f;
        lineDataSet.addEntry(new Entry(x, hrValue));
        lineDataSet.notifyDataSetChanged();
        data.notifyDataChanged();
        ccHt.setVisibleXRangeMaximum(7);
        int entryCount = lineDataSet.getEntryCount();
        ccHt.moveViewTo( entryCount- 8, 50f, YAxis.AxisDependency.LEFT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void setBaseData(UpdateIndoorRunDataEvent event) {
        List<BarEntry> values = event.getmValues();
        BarEntry entry = new BarEntry(values.size()+0.5f, new float[]{0, 0});
        values.add(entry);
        setChartData(ccHt, values);
        CombinedData data = ccHt.getData();
        data.notifyDataChanged();
        ccHt.notifyDataSetChanged();
        ccHt.setVisibleXRangeMaximum(7);
        ccHt.setVisibleXRangeMinimum(7);
    }

    private void setChartData(CombinedChart mLineChart, List<BarEntry> mValues) {
        BarDataSet set1;
        LineDataSet set2;
        //判断图表中原来是否有数据
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (BarDataSet) mLineChart.getData().getDataSetByIndex(1);
            set1.setValues(mValues);
            set2 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set2.setValues(lineValues);
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

            //设置数据1  参数1：数据源 参数2：图例名称
            set2 = new LineDataSet(lineValues, "测试数据1");
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set2.setColor(Color.parseColor("#FBA165"));
            set2.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
            set2.setHighlightEnabled(true);//是否使用点击高亮线
            set2.setDrawCircles(true);
            set2.setValueTextColor(Color.parseColor("#FBA165"));
            set2.setLineWidth(DimensChange.dp2px(getContext() , 1.5f));//设置线宽
            set2.setDrawCircles(false);//是否画焦点
            set2.setDrawValues(false);
            LineData lineData = new LineData(set2);
            data.setData(lineData);

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

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(true);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMinimum(50);
        rightAxis.setAxisMaximum(100);
        rightAxis.setSpaceTop(0);
        rightAxis.setSpaceBottom(0);
        rightAxis.setLabelCount(6, true);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = new Float(value).intValue();
                return i+" %";
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(maxBaseHr);
        leftAxis.setAxisMinimum(maxBaseHr * 0.5f);
        leftAxis.setSpaceTop(0);
        leftAxis.setSpaceBottom(0);
        leftAxis.setLabelCount(6, true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f); //设置横向表格为虚线
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) (value * 5);
                return ""+v;
            }
        });
    }

}
