package com.test.lsm.ui.fragment.care_group;

import android.graphics.Color;
import android.text.TextUtils;

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
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupMemDetailReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;


/**
 * 功能：关心群组成员HRV
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/29
 */
public class CareGroupMemHRRecordFragment extends LsmBaseFragment {

    private   int maxBaseHr;

    @BindView(R.id.cc_ht)
    CombinedChart ccHt;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_care_group_mem_hr_record;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        MyApplication application = (MyApplication) (getActivity().getApplication());
        UserLoginReturn.PdBean user = application.getUser();
        String birthday = user.getBIRTHDAY();
        int age = 30;
        if (!TextUtils.isEmpty(birthday)) {
            age = MyTimeUtils.getAge(birthday);
        }

        maxBaseHr = 220 - age; //220-用户年纪
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
    }

    public void inflateLineChart(List<GetMonitorGroupMemDetailReturn.DataBean.HeartInfoListBean> mData){
        initChart(ccHt);
        if (mData.size()<=0) return;
        List<Entry> lineValues = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupMemDetailReturn.DataBean.HeartInfoListBean heartInfo = mData.get(i);
            lineValues.add(new Entry(i, heartInfo.getHeartNum()));
        }
        CombinedData data = new CombinedData();
        LineDataSet lineDataSet;
        lineDataSet = new LineDataSet(lineValues, "测试数据1");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setColor(Color.parseColor("#FBA165"));
        lineDataSet.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
        lineDataSet.setHighlightEnabled(true);//是否使用点击高亮线
        lineDataSet.setDrawCircles(true);
        lineDataSet.setValueTextColor(Color.parseColor("#FBA165"));
        lineDataSet.setLineWidth(DimensChange.dp2px(getContext() , 1.5f));//设置线宽
        lineDataSet.setDrawCircles(false);//是否画焦点
        lineDataSet.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet);
        data.setData(lineData);
        ccHt.setData(data);
        ccHt.setVisibleXRangeMaximum(50);
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
     /*   rightAxis.setAxisMinimum(50);
        rightAxis.setAxisMaximum(100);*/
        rightAxis.setSpaceTop(0);
        rightAxis.setSpaceBottom(0);
        rightAxis.setLabelCount(6, true);
        /*rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int i = new Float(value).intValue();
                return i+" %";
            }
        });*/

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(Color.WHITE);
/*        leftAxis.setAxisMaximum(maxBaseHr);
        leftAxis.setAxisMinimum(maxBaseHr * 0.5f);*/
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
   /*     xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) (value * 5);
                return ""+v;
            }
        });*/
    }

}
