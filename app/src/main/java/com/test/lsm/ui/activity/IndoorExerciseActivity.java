package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.UpdateIndoorRunDataEvent;
import com.test.lsm.bean.json.GetCourseParams;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.indoor_run.IndoorHrStatsFragment;
import com.test.lsm.ui.fragment.indoor_run.IndoorRunHrFragment;
import com.test.lsm.ui.fragment.indoor_run.IndoorRunHrvFragment;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import me.relex.circleindicator.CircleIndicator;

import static com.test.lsm.ui.activity.IndoorExerciseActivity.RunStatus.NONE;
import static com.test.lsm.ui.activity.IndoorExerciseActivity.RunStatus.PAUSE;
import static com.test.lsm.ui.activity.IndoorExerciseActivity.RunStatus.START;
import static com.test.lsm.ui.activity.IndoorExerciseActivity.RunStatus.STOP;

/**
 * 功能：室内运动
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class IndoorExerciseActivity extends LsmBaseActivity {

    private static final String TAG = "IndoorExerciseActivity";

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
    @BindView(R.id.cv_indoor_run)
    CardView cvIndoorRun;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_center)
    ImageView ivCenter;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_real_time_hr)
    TextView tvRealTimeHr;
    @BindView(R.id.tv_max_hr)
    TextView tvMaxHr;
    @BindView(R.id.tv_real_time_point)
    TextView tvRealTimePoint;
    @BindView(R.id.tv_real_time_tip)
    TextView tvRealTimeTip;
    private List<BarEntry> realTimeValues;
    private List<BarEntry> endTimeValues;
    private List<Entry> lineValues;
    private UserLoginReturn.PdBean user;
    private int courseLevel;
    private String courseType;
    public static int maxBaseHr;
    private List<Fragment> fragmentList;
    private MyApplication application;

    private boolean isActDestroy = false;

    List<Integer> hrBufferPerTenSec = new ArrayList<>();
    List<Integer> hrBufferPerFiveMin = new ArrayList<>();

    List<Integer> avgHrPerTenSec = new ArrayList<>();
    List<Integer> avgHrPerFiveMin = new ArrayList<>();

    private RunStatus runStatus = NONE;
    private IndoorRunHrFragment indoorRunHrFragment;
    private IndoorHrStatsFragment indoorHrStatsFragment;
    private String courseName;

    public enum RunStatus {
        NONE,
        START,
        PAUSE,
        STOP
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_indoor_exercise;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();

        application = (MyApplication) getApplication();

        fragmentList = new ArrayList<>();
        indoorRunHrFragment = new IndoorRunHrFragment();
        indoorHrStatsFragment = new IndoorHrStatsFragment();
        fragmentList.add(indoorRunHrFragment);
        fragmentList.add(new IndoorRunHrvFragment());
        fragmentList.add(indoorHrStatsFragment);

        Intent intent = getIntent();
        courseLevel = intent.getIntExtra("courseLevel", 0);
        courseType = intent.getStringExtra("courseType");
        courseName = intent.getStringExtra("courseName");
        MyApplication application = (MyApplication) getApplication();
        user = application.getUser();

        String birthday = application.getUser().getBIRTHDAY();

        int age = 30;
        if (!TextUtils.isEmpty(birthday)){
            age = MyTimeUtils.getAge(birthday);
        }

        maxBaseHr = 220 - age; //220-用户年纪
        realTimeValues = new ArrayList<>();
        endTimeValues = new ArrayList<>();
        lineValues = new ArrayList<>();
        lineValues.add(new Entry(0, maxBaseHr * 0.5f));

    }

    @Override
    protected void initView() {

        cvIndoorRun.setAlpha(0.8f);

        vpIndoorRun.setOffscreenPageLimit(3);

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
        GlidUtils.load(this, rvUserIcon, userImage);
        tvRealTimeTip.setText("# "+courseName);

        initChart(ccHt);
    }

    @Override
    protected void initListener() {

        //---结束
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runStatus == START || runStatus == PAUSE) {
                    ivLeft.setVisibility(View.VISIBLE);
                    ivLeft.setImageResource(R.mipmap.ic_run_stop_disable);
                    ivCenter.setVisibility(View.VISIBLE);
                    ivCenter.setImageResource(R.mipmap.ic_indoor_run_start);
                    ivRight.setVisibility(View.VISIBLE);
                    ivRight.setImageResource(R.mipmap.ic_run_pause_disable);
                    runStatus = STOP;
                    MyToast.showLong(IndoorExerciseActivity.this, "停止");
                }
            }
        });

        //---开始
        ivCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!application.isBleConnected()) {
                    MyToast.showLong(IndoorExerciseActivity.this, "蓝牙设备未连接");
                    return;
                }
                if (runStatus==STOP){//stop-->start 重新开始
                    resetLineChart();
                }
                if (runStatus == NONE || runStatus == STOP || runStatus == PAUSE) {
                    ivCenter.setVisibility(View.INVISIBLE);
                    ivLeft.setVisibility(View.VISIBLE);
                    ivLeft.setImageResource(R.mipmap.ic_run_stop);
                    ivRight.setVisibility(View.VISIBLE);
                    ivRight.setImageResource(R.mipmap.ic_run_pause);
                    runStatus = START;
                    MyToast.showLong(IndoorExerciseActivity.this, "开始");
                }
            }
        });

        //---暂停
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runStatus == START) {
                    ivLeft.setVisibility(View.VISIBLE);
                    ivLeft.setImageResource(R.mipmap.ic_run_stop);
                    ivCenter.setVisibility(View.VISIBLE);
                    ivCenter.setImageResource(R.mipmap.ic_indoor_run_start);
                    ivRight.setVisibility(View.VISIBLE);
                    ivRight.setImageResource(R.mipmap.ic_run_pause_disable);
                    MyToast.showLong(IndoorExerciseActivity.this, "暂停");
                    runStatus = PAUSE;
                }
            }
        });


        rvUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
      /*          int nextInt = new Random().nextInt(80) + 100;
                addLineEntry(nextInt);
                indoorRunHrFragment.addLineEntry(nextInt);
                indoorHrStatsFragment.initLineChartData(nextInt);*/
            }
        });

        //---获取心跳值的回调
        application.setOnGetHrValueListener(new MyApplication.OnGetHrValueListener() {
            private int maxHr = 0;

            @Override
            public void onGet(int hrValue) {
                if (isActDestroy) {
                    return;
                }

                hrBufferPerTenSec.add(hrValue);
                hrBufferPerFiveMin.add(hrValue);

                if (runStatus == START) {
                    tvRealTimeHr.setText(hrValue + " bpm");
                    if (hrValue > maxHr) {
                        maxHr = hrValue;
                    }
                    tvMaxHr.setText(maxHr + " bpm");
                    indoorHrStatsFragment.initLineChartData(hrValue);
                }

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
                    int index = 1;
                    float index2 = 0.5f;
                    for (GetCourseParams.PdBean bean : pd) {

                        int paramLevel = Integer.parseInt(bean.getPARAM_LEVEL().trim());
                        double proportion = 0;
                        switch (paramLevel) {
                            case 1://50%
                                proportion = 0.5;
                                break;
                            case 2://60%
                                proportion = 0.6;
                                break;
                            case 3://70%
                                proportion = 0.7;
                                break;
                            case 4://80%
                                proportion = 0.8;
                                break;
                            case 5://90%
                                proportion = 0.9;
                                break;
                            case 6://100%
                                proportion = 1.0;
                                break;
                        }

                        for (int i = 0; i < 30; i++) {//5分钟一个数据 转换为10s间隔一个数据
                            int y1 = (int) (maxBaseHr * proportion);
                            BarEntry entry = new BarEntry(index++, new float[]{y1, maxBaseHr * 0.1f});
                            realTimeValues.add(entry);
                        }

                        int y1 = (int) (maxBaseHr *proportion);
                        BarEntry entry = new BarEntry(index2++, new float[]{y1, maxBaseHr * 0.1f});
                        endTimeValues.add(entry);
                    }
                }

                setChartData(ccHt, realTimeValues);
                CombinedData data = ccHt.getData();
                data.notifyDataChanged();
                ccHt.notifyDataSetChanged();
                ccHt.setVisibleXRangeMaximum(30);
                EventBus.getDefault().post(new UpdateIndoorRunDataEvent(endTimeValues));
                toRecordHr();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoadDialog();
            }
        });
    }

    private void resetLineChart() {
        avgHrPerTenSec.clear();
        avgHrPerFiveMin.clear();
        CombinedData data = ccHt.getData();
        LineDataSet lineDataSet = (LineDataSet) data.getDataSetByIndex(0);
        lineDataSet.clear();
        lineDataSet.addEntry(new Entry(0, maxBaseHr * 0.5f));
        lineDataSet.notifyDataSetChanged();
        data.notifyDataChanged();
        ccHt.moveViewTo(0, 50f, YAxis.AxisDependency.LEFT);
        indoorRunHrFragment.resetLineChart();
        indoorHrStatsFragment.resetChartData();
    }

    private void toRecordHr() {
        //10s中记录一次平均心跳
        new Thread(new Runnable() {
            private int qualifiedNum = 0;

            @Override
            public void run() {
                while (!isActDestroy) {
                    try {
                        Thread.sleep(10 * 1000);
                        if (isActDestroy || runStatus != START) {
                            continue;
                        }
                        if (hrBufferPerTenSec.size() > 0) {
                            int total = 0;
                            for (Integer hrValue : hrBufferPerTenSec) {
                                if (hrValue > 0) {
                                    total += hrValue;
                                }
                            }
                            int avgHr = total / hrBufferPerTenSec.size();
                            avgHrPerTenSec.add(avgHr);
                            hrBufferPerTenSec.clear();
                            addLineEntry(avgHr);
                            //判断值是否在标准内
                            int size = avgHrPerTenSec.size() > realTimeValues.size() ? realTimeValues.size() : avgHrPerTenSec.size();
                            BarEntry entry = realTimeValues.get(size - 1);
                            float endY = entry.getY();
                            float startY = endY - 0.1f * maxBaseHr;
                            if (avgHr > startY && avgHr < endY) {//满足计分条件
                                qualifiedNum++;
                            }
                            final int point = qualifiedNum * 100 / realTimeValues.size();
                            tvRealTimePoint.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvRealTimePoint.setText("" + point);
                                }
                            });
                        }
                        MyLog.e(TAG, "==============toRecordHr=======");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //5min记录一次平均心跳
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isActDestroy) {
                    try {
                        Thread.sleep(1 * 60 * 1000);
                        if (isActDestroy || runStatus != START) {
                            continue;
                        }
                        if (hrBufferPerFiveMin.size() > 0) {
                            int total = 0;
                            for (Integer hrValue : hrBufferPerFiveMin) {
                                if (hrValue > 0) {
                                    total += hrValue;
                                }
                            }
                            int avgHr = total / hrBufferPerFiveMin.size();
                            avgHrPerFiveMin.add(avgHr);
                            hrBufferPerFiveMin.clear();
                            indoorRunHrFragment.addLineEntry(avgHr);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void back(View view) {
        finish();
    }

    public void toSetting(View view) {
        SettingActivity.startAction(this);
    }

    private void addLineEntry(int hrValue) {
        CombinedData data = ccHt.getData();
        LineDataSet lineDataSet = (LineDataSet) data.getDataSetByIndex(0);
        int i = lineDataSet.getEntryCount() - 1;
        lineDataSet.addEntry(new Entry(i, hrValue));
        lineDataSet.notifyDataSetChanged();
        data.notifyDataChanged();
        ccHt.setVisibleXRangeMaximum(30);
        ccHt.moveViewTo(lineDataSet.getEntryCount() - 31, 50f, YAxis.AxisDependency.LEFT);
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
        mChart.setVisibleXRangeMaximum(25);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(6, true);
        rightAxis.setAxisMaximum(maxBaseHr);
        rightAxis.setAxisMinimum(maxBaseHr * 0.5f); // this replaces setStartAtZero(true)
        rightAxis.setSpaceTop(0);
        rightAxis.setSpaceBottom(0);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(6, true);
        leftAxis.setAxisMaximum(maxBaseHr);
        leftAxis.setAxisMinimum(maxBaseHr * 0.5f); // this replaces setStartAtZero(true)
        leftAxis.setSpaceTop(0);
        leftAxis.setSpaceBottom(0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActDestroy = true;
    }

    public static void startAction(Context context, Integer courseLevel, String courseType , String courseName) {
        Intent intent = new Intent(context, IndoorExerciseActivity.class);
        intent.putExtra("courseLevel", courseLevel);
        intent.putExtra("courseType", courseType);
        intent.putExtra("courseName", courseName);
        context.startActivity(intent);
    }

}
