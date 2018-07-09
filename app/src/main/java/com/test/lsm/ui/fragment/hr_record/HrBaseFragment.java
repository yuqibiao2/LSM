package com.test.lsm.ui.fragment.hr_record;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.test.lsm.R;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.DimensChange;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/6
 */
public abstract class HrBaseFragment extends LsmBaseFragment {


    /**
     * #FFBF45  时
     * #FFBF45 日
     *
     * @param mLineChart
     * @param mValues
     */
    protected void initChart2(LineChart mLineChart, List<Entry> mValues) {
        mLineChart.getDescription().setEnabled(false);//取消文字描述
        mLineChart.setNoDataText("没有数据");//没有数据时显示的文字
        mLineChart.setTouchEnabled(true);     //能否点击
        mLineChart.setDragEnabled(false);   //能否拖拽
        mLineChart.setScaleEnabled(false);  //能否缩放
        mLineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        Legend l = mLineChart.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例
        //---x轴相关
        XAxis xAxis = mLineChart.getXAxis();       //获取x轴线
        xAxis.setDrawAxisLine(false);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        //xAxis.setTextSize(12f);//设置文字大小
        xAxis.setAxisMinimum(0f);//设置x轴的最小值 //
        xAxis.setLabelCount(mValues.size() / 2);  //设置X轴的显示个数
        //---y轴相关
        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis axisRight = mLineChart.getAxisRight();
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawLabels(false);
        axisRight.setStartAtZero(true);
        axisRight.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);        //是否显示Y轴刻度
        leftAxis.setStartAtZero(true);        //设置Y轴数值 从零开始
        leftAxis.setDrawGridLines(false);      //是否使用 Y轴网格线条
        //---设置marker
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);  //设置 marker ,点击后显示的功能 ，布局可以自定义
        setData(mLineChart, mValues);
    }

    protected void initChart(LineChart mLineChart, List<Entry> mValues) {
        mLineChart.setLogEnabled(true);//打印日志
        //取消描述文字
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setNoDataText("没有数据");//没有数据时显示的文字
        mLineChart.setNoDataTextColor(Color.WHITE);//没有数据时显示文字的颜色
        mLineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        mLineChart.setDrawBorders(false);//是否禁止绘制图表边框的线
        mLineChart.setBorderColor(Color.WHITE); //设置 chart 边框线的颜色。
        mLineChart.setBorderWidth(3f); //设置 chart 边界线的宽度，单位 dp。
        mLineChart.setTouchEnabled(true);     //能否点击
        mLineChart.setDragEnabled(false);   //能否拖拽
        mLineChart.setScaleEnabled(false);  //能否缩放
        //mLineChart.animateX(1000);//绘制动画 从左到右
        mLineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        mLineChart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        mLineChart.setDragDecelerationEnabled(false);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        //---x轴相关
        XAxis xAxis = mLineChart.getXAxis();       //获取x轴线
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setDrawGridLines(false);//设置x轴上每个点对应的线
        xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setTextSize(12f);//设置文字大小
        xAxis.setAxisMinimum(0f);//设置x轴的最小值 //`
        //xAxis.setAxisMaximum(31f);//设置最大值 //
        xAxis.setLabelCount(10);  //设置X轴的显示个数
        xAxis.setAvoidFirstLastClipping(false);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
        xAxis.setAxisLineColor(Color.WHITE);//设置x轴线颜色
        xAxis.setAxisLineWidth(0.5f);//设置x轴线宽度
        //---y轴相关
        YAxis leftAxis = mLineChart.getAxisLeft();
        YAxis axisRight = mLineChart.getAxisRight();
        leftAxis.setDrawAxisLine(false);
        axisRight.setDrawAxisLine(false);
    /*    leftAxis.enableGridDashedLine(10f, 10f, 0f);  //设置Y轴网格线条的虚线，参1 实线长度，参2 虚线长度 ，参3 周期
        leftAxis.setGranularity(20f); // 网格线条间距
        axisRight.setEnabled(false);   //设置是否使用 Y轴右边的
        leftAxis.setEnabled(true);     //设置是否使用 Y轴左边的
        leftAxis.setGridColor(Color.parseColor("#7189a9"));  //网格线条的颜色
        leftAxis.setDrawLabels(false);        //是否显示Y轴刻度
        leftAxis.setStartAtZero(true);        //设置Y轴数值 从零开始
        leftAxis.setDrawGridLines(false);      //是否使用 Y轴网格线条
        leftAxis.setTextSize(12f);            //设置Y轴刻度字体
        leftAxis.setTextColor(Color.WHITE);   //设置字体颜色
        leftAxis.setAxisLineColor(Color.WHITE); //设置Y轴颜色
        leftAxis.setAxisLineWidth(0.5f);
        leftAxis.setDrawAxisLine(false);//是否绘制轴线
        leftAxis.setMinWidth(0f);
        leftAxis.setMaxWidth(200f);
        leftAxis.setDrawGridLines(false);//设置x轴上每个点对应的线*/
        //---图例
        Legend l = mLineChart.getLegend();//图例
        l.setEnabled(false);   //是否使用 图例
        //---mark设置
        /*MyMarkerView mv = new MyMarkerView(mActivity,
                R.layout.custom_marker_view);
        mv.setChartView(mLineChart); // For bounds control
        mLineChart.setMarker(mv);        //设置 marker ,点击后显示的功能 ，布局可以自定义*/

        setData(mLineChart, mValues);
    }

    private void setData(LineChart mLineChart, List<Entry> mValues) {
        LineDataSet set1;
        //判断图表中原来是否有数据
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(mValues);
            //刷新数据
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(mValues, "测试数据1");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setColor(getLineColor());
            set1.setHighLightColor(Color.WHITE);//设置点击交点后显示交高亮线的颜色
            set1.setHighlightEnabled(true);//是否使用点击高亮线
            set1.setDrawCircles(true);
            set1.setValueTextColor(Color.parseColor("#FBA165"));
            set1.setLineWidth(DimensChange.dp2px(getActivity(), 2f));//设置线宽
            set1.setDrawCircles(false);//是否画焦点
            set1.setDrawValues(false);
            //格式化显示数据
            set1.setDrawFilled(true);//设置使用 范围背景填充
            set1.setFillAlpha(0);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.lc_mouth_bg);
                set1.setFillDrawable(drawable);//设置范围背景填充
            } else {
                set1.setFillColor(Color.parseColor("#FEE1CB"));
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            mLineChart.setData(data);
            //绘制图表
            mLineChart.invalidate();
        }

    }


    /**
     * 自定义maker
     */
    public class MyMarkerView extends MarkerView {

        private TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
            } else {
                tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
            }

            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

    protected int getLineColor() {

        return Color.parseColor("#FB6EC8");
    }

}
