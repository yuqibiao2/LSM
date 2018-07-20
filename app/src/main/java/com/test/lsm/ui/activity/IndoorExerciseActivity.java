package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CombinedChart;
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
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.UpdateIndoorRunDataEvent;
import com.test.lsm.bean.json.GetCourseParams;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.indoor_run.IndoorRunHrFragment;
import com.test.lsm.ui.fragment.indoor_run.IndoorRunHrvFragment;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import me.relex.circleindicator.CircleIndicator;

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
    @BindView(R.id.vp_indoor_run)
    ViewPager vpIndoorRun;
    @BindView(R.id.ci_indoor_run)
    CircleIndicator ciIndoorRun;
    private List<BarEntry> realTimeValues;
    private List<BarEntry> endTimeValues;
    private List<Entry> lineValues;
    private UserLoginReturn.PdBean user;
    private int courseLevel;
    private String courseType;
    private int maxBaseHr;
    private List<Fragment> fragmentList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_indoor_exercise;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();

        fragmentList = new ArrayList<>();
        fragmentList.add(new IndoorRunHrFragment());
        fragmentList.add(new IndoorRunHrvFragment());

        Intent intent = getIntent();
        courseLevel = intent.getIntExtra("courseLevel", 0);
        courseType = intent.getStringExtra("courseType");
        MyApplication application = (MyApplication) getApplication();
        user = application.getUser();

        maxBaseHr = 220; //220-用户年纪
        realTimeValues = new ArrayList<>();
        endTimeValues = new ArrayList<>();
        lineValues = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            int nextInt = new Random().nextInt(180);
            lineValues.add(new Entry(i, nextInt));
        }

    }

    @Override
    protected void initView() {

        vpIndoorRun.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        ciIndoorRun.setViewPager(vpIndoorRun);

        String userImage = user.getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)) {
            Glide.with(this).load(userImage).into(rvUserIcon);
        }
        initChart(ccHt);
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

    @Override
    protected void initData() {
        super.initData();
        showLoadDialog();
        APIMethodManager.getInstance().getCourseParamByType(courseType, courseLevel, new IRequestCallback<GetCourseParams>() {
            @Override
            public void onSuccess(GetCourseParams result) {
                dismissLoadDialog();
                if (result.getResult().equals("01")) {
                    List<GetCourseParams.PdBean> pd = result.getPd();
                    int index = 0;
                    int index2 = 0;
                    for (GetCourseParams.PdBean bean : pd) {
                        String paramNumber = bean.getPARAM_NUMBER();
                        String[] split = paramNumber.split("~");
                        float start = MyStringUtils.percentToDecimals(split[0]);
                        float end = MyStringUtils.percentToDecimals(split[1]);
                        for (int i = 0; i < 30; i++) {//5分钟一个数据 转换为10s间隔一个数据
                            int y1 = (int) (maxBaseHr * end);
                            int y2 = (int) (maxBaseHr * start);
                            BarEntry entry = new BarEntry(index++, new float[]{y1, y1 - y2});
                            realTimeValues.add(entry);
                        }
                        int y1 = (int) (maxBaseHr * end);
                        int y2 = (int) (maxBaseHr * start);
                        BarEntry entry = new BarEntry(index2++, new float[]{y1, y1 - y2});
                        endTimeValues.add(entry);
                    }
                }

                setChartData(ccHt, realTimeValues);
                CombinedData data = ccHt.getData();
                data.notifyDataChanged();
                ccHt.notifyDataSetChanged();
                ccHt.setVisibleXRangeMaximum(30);
                //BarDataSet set = (BarDataSet) data.getDataSetByIndex(1);
                //ccHt.moveViewTo(set.getEntryCount() - 31, 50f, YAxis.AxisDependency.LEFT);

                EventBus.getDefault().post(new UpdateIndoorRunDataEvent(endTimeValues));

            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoadDialog();
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public void toSetting(View view) {
        SettingActivity.startAction(this);
    }

    private void addEntry() {
        CombinedData data = ccHt.getData();
        BarDataSet set = (BarDataSet) data.getDataSetByIndex(1);
        BarEntry barEntry = new BarEntry(set.getEntryCount() + 1, new float[]{120, 60});
        set.addEntry(barEntry);
        set.notifyDataSetChanged();
        data.notifyDataChanged();
        ccHt.notifyDataSetChanged();
        ccHt.setVisibleXRangeMaximum(30);
        ccHt.moveViewTo(set.getEntryCount() - 31, 50f, YAxis.AxisDependency.LEFT);

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
            set2.setLineWidth(DimensChange.dp2px(this, 1.5f));//设置线宽
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
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
    }

    public static void startAction(Context context, Integer courseLevel, String courseType) {
        Intent intent = new Intent(context, IndoorExerciseActivity.class);
        intent.putExtra("courseLevel", courseLevel);
        intent.putExtra("courseType", courseType);
        context.startActivity(intent);
    }

}
