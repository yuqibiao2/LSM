package com.test.lsm.ui.fragment.indoor_run;

import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.code.microlog4android.format.command.util.StringUtil;
import com.test.lsm.R;
import com.test.lsm.ui.activity.IndoorExerciseActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.TimeUtils;
import com.yyyu.baselibrary.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static com.test.lsm.ui.activity.IndoorExerciseActivity.maxBaseHr;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/26
 */
public class IndoorHrStatsFragment extends LsmBaseFragment {

    @BindView(R.id.lc_hr)
    LineChart lcHr;
    @BindView(R.id.bc_hr)
    BarChart bcHr;
    @BindView(R.id.tv_time5)
    TextView tvTime5;
    @BindView(R.id.tv_time4)
    TextView tvTime4;
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_time1)
    TextView tvTime1;

    private List<Integer> hrLevel1 = new ArrayList<>();
    private List<Integer> hrLevel2 = new ArrayList<>();
    private List<Integer> hrLevel3 = new ArrayList<>();
    private List<Integer> hrLevel4 = new ArrayList<>();
    private List<Integer> hrLevel5 = new ArrayList<>();

    @Override
    public int getLayoutId() {

        return R.layout.fragment_indoor_hr_stats;
    }

    @Override
    protected void initView() {
        initLineChart();
        initBarChart();
    }

    @Override
    protected void initListener() {

    }

    private List<Integer> hrValueList = new ArrayList<>();

    public void initLineChartData(Integer hrValue) {
        initLineChart();
        putHrWithLevel(hrValue);

        hrValueList.add(hrValue);
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < hrValueList.size(); i++) {
            yVals.add(new Entry(i, hrValueList.get(i)));
        }
        if (yVals.size() == 0) {
            return;
        }
        ArrayList<ILineDataSet> dataSetList = new ArrayList<>();
        LineDataSet set1;
        set1 = new LineDataSet(yVals, "心跳");
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setColor(Color.parseColor("#ffffff"));
        dataSetList.add(set1);
        LineData data = new LineData(dataSetList);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
        lcHr.setData(data);
        lcHr.invalidate();
    }


    public void resetChartData() {
        hrValueList.clear();
        lcHr.getLineData().clearValues();
        hrLevel1.clear();
        hrLevel2.clear();
        hrLevel3.clear();
        hrLevel4.clear();
        hrLevel5.clear();
        bcHr.getBarData().clearValues();
    }

    private void putHrWithLevel(int hrValue) {
        if (hrValue > maxBaseHr * 0.9) {//level5
            hrLevel5.add(hrValue);
        } else if (hrValue > maxBaseHr * 0.8) {//level4
            hrLevel4.add(hrValue);
        } else if (hrValue > maxBaseHr * 0.7) {//level3
            hrLevel3.add(hrValue);
        } else if (hrValue > maxBaseHr * 0.6) {//level2
            hrLevel2.add(hrValue);
        } else if (hrValue > maxBaseHr * 0.5) {//level1
            hrLevel1.add(hrValue);
        } else {

        }

        List<Integer> hrCountList = new ArrayList<>();
        hrCountList.add(hrLevel1.size());
        hrCountList.add(hrLevel2.size());
        hrCountList.add(hrLevel3.size());
        hrCountList.add(hrLevel4.size());
        hrCountList.add(hrLevel5.size());
        initBarChartData(hrCountList);

        tvTime1.setText(""+ TimeUtils.countTimer(hrLevel1.size()*10L));
        tvTime2.setText(""+ TimeUtils.countTimer(hrLevel2.size()*10L));
        tvTime3.setText(""+ TimeUtils.countTimer(hrLevel3.size()*10L));
        tvTime4.setText(""+ TimeUtils.countTimer(hrLevel4.size()*10L));
        tvTime5.setText(""+ TimeUtils.countTimer(hrLevel5.size()*10L));

    }

    private void initLineChart() {
        //lcHr.setMinOffset(0f);
        lcHr.setNoDataText("没有数据");//没有数据时显示的文字
        lcHr.setTouchEnabled(false);     //能否点击
        lcHr.setDragEnabled(false);   //能否拖拽
        lcHr.setScaleEnabled(false);  //能否缩放
        lcHr.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        lcHr.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lcHr.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        Legend l = lcHr.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例
        lcHr.getDescription().setEnabled(false);

        YAxis rightAxis = lcHr.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(true);
        rightAxis.setAxisMaximum(100);
        rightAxis.setAxisMinimum(50);
        rightAxis.setSpaceBottom(0);
        rightAxis.setSpaceTop(0);
        rightAxis.setLabelCount(6 , true);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = new Float(value).intValue();
                return i+" %";
            }
        });

        YAxis leftAxis = lcHr.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(0);
        leftAxis.setSpaceBottom(0);
        leftAxis.setAxisMaximum(maxBaseHr);
        leftAxis.setAxisMinimum(maxBaseHr * 0.5f);
        leftAxis.setLabelCount(6 ,true);

        XAxis xAxis = lcHr.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
    }



    public void initBarChartData(List<Integer> hrCountList) {

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        for (int i = 0; i < hrCountList.size(); i++) {
            yVals.add(new BarEntry(i, hrCountList.get(i)));
        }
        if (yVals.size() == 0) {
            return;
        }
        ArrayList<IBarDataSet> dataSetList = new ArrayList<>();
        BarDataSet set1;
        set1 = new BarDataSet(yVals, "心跳");
        set1.setColors(Color.parseColor("#C4CACA"),
                Color.parseColor("#47C7EE"),
                Color.parseColor("#9DBE49"),
                Color.parseColor("#FECC2D"),
                Color.parseColor("#DD0D59"));
        dataSetList.add(set1);
        BarData data = new BarData(dataSetList);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
        bcHr.setData(data);
        bcHr.invalidate();
    }

    private float index = 0.5f;

    private void initBarChart() {

        Legend l = bcHr.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例
        bcHr.setFitBars(true);
        bcHr.setTouchEnabled(false);
        bcHr.setScaleEnabled(false);
        bcHr.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);//设置X轴的位置
        bcHr.getXAxis().setDrawGridLines(false);//不显示网格
        bcHr.getXAxis().setDrawLabels(false);
        bcHr.getXAxis().setDrawAxisLine(false);
        bcHr.getAxisLeft().setLabelCount(5);
        bcHr.getAxisRight().setLabelCount(5);
        bcHr.getDescription().setEnabled(false);//不设置描述
        bcHr.getAxisLeft().setDrawGridLines(false);//不设置Y轴网格
        bcHr.getAxisRight().setDrawGridLines(false);
        bcHr.getAxisLeft().setDrawAxisLine(false);
        bcHr.getAxisRight().setDrawAxisLine(false);
        bcHr.getAxisLeft().setDrawLabels(false);
        bcHr.getAxisRight().setDrawLabels(false);
    }

}
