package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.test.lsm.R;
import com.test.lsm.global.Constant;
import com.yyyu.baselibrary.utils.MyLog;

import java.util.ArrayList;

import butterknife.BindView;
import singularwings.com.swmalgolib.ble.filter.IirFilter;

/**
 * 功能：心电图展示
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/9
 */
public class ECGShowActivity extends LsmBaseActivity {

    private static final String TAG = "ECGShowActivity";

    @BindView(R.id.lc_egc)
    LineChart mChart;
    private IirFilter iirFilter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ecg_show;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        iirFilter = new IirFilter();
    }

    @Override
    protected void initView() {
        initChart();
    }

    @Override
    protected void initListener() {

    }


    private boolean flag = true;

    @Override
    protected void initData() {

        super.initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        mChart.post(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                                mChart.animateXY(2000, 0);
                                mChart.invalidate();
                            }
                        });
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void initChart() {
        mChart.setTouchEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.getXAxis().setEnabled(true);
        mChart.getXAxis().setDrawGridLines(true);//不显示网格
        mChart.getAxisRight().setEnabled(true);//右侧不显示Y轴
        mChart.getAxisLeft().setEnabled(true);//左不显示Y轴
        mChart.getDescription().setEnabled(false);//不设置描述
        mChart.getLegend().setEnabled(false);
    }


    private void setData() {
        //String epcStr = Constant.sbHeartData2.toString();
        //MyLog.e(TAG , "epcStr1："+Constant.sbHeartData.toString());
       // MyLog.e(TAG , "epcStr2："+epcStr);
       /* if (TextUtils.isEmpty(epcStr)) {
            return;
        }*/
       // String[] split = epcStr.split(",");
        ArrayList<Entry> yVals = new ArrayList<Entry>();
       /* for (int i = 0; i <Constant.egcDataCon.size(); i++) {
            Short aShort = Constant.egcDataCon.get(i);
           Integer canDrawValue = iirFilter.filter(Integer.valueOf(aShort));

            yVals.add(new Entry(i, canDrawValue));
            MyLog.e("aShort::"+aShort);
        }*/

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i <Constant.egcDataCon.size(); i++) {
            Short aShort = Constant.egcDataCon.get(i);
            yVals2.add(new Entry(i, aShort));
        }

       /* short[] data1 = new short[]{
        16901,16901,16902,16902,16902,16903,16903,16904,16904,16904,16904,16903,16903,16901,16901,16902,16903,16904,16907,16909,16910,16911,16911,16912,16913,16913,16913,16912,16910,16910,16910,16910,16910,16910,16911,16911,16911,16911,16911,16909,16904,16901,16900,16908,16922,16955,17028,17138,17264,17380,17444,17392,17254,17102,16995,16946,16928,16915,16905,16903,16910,16924,16937,16947,16953,16958,16962,16967,16972,16976,16979,16983,16986,16990,16993,16995,16999,17002,17007,17012,17017,17022,17026,17030,17036,17042,17049,17056,17063,17071,17080,17088,17098,17109,17118,17129,17141,17153,17167,17181,17196,17212,17229,17247,17265,17283,17302,17319,17335,17351,17366,17380,17393,17404,17413,17418,17419,17417,17412,17403,17389,17372,17350,17326,17302,17276,17252,17227,17203,17181,17161,17142,17125,17109,17095,17082,17071,17060,17052,17044,17037,17030,17027,17022,17017,17014,17010,17007,17006,17004,17002,17000,16998,16996,16995,16995,16994,16994,16992,16991,16989,16988,16987,16986,16985,16983,16982,16980,16978,16976,16974,16972,16970,16968,16965,16964,16963,16961,16960,16959,16958,16955,16955,16953,16951,16949,16947,16947,16946,16944,16944,16943,16943,16942,16942,16941,16939,16938,16936,16935,16936,16936,16936,16935,16935,16935,16935,16933,16933,16933,16933,16934,16933,16933,16933,16931,16930,16930,16929,16930,16929,16930,16930,16930,16930,16930,16930,16929,16928,16927,16926,16925,16924,16924,16923,16923,16923,16923,16923,16923,16922,16921,16919,16919,16919,16918,16919,16919,16921,16921,16920,16920,16919,16919,16919,16919,16920,16921,16918,16917,16917,16915,16915,16916,16917,16916,16914,16914,16914,16914,16913,16911,16911,16911,16911,16910,16910,16910,16908,16908,16907,16905,16904,16903,16901,16898,16897,16897,16898,16899,16899,16899,16897,16897,16899,16898,16900,16901,16900,16898,16897,16897,16897,16896,16896,16897,16896,16896,16896,16896,16895,16894,16890,16885,16888,16901,16921,16971,17063,17184,17304,17400,17401,17295,17141,17009,16936,16903,16884,16871,16865,16872};
        for (int i = 0; i <data1.length; i++) {
            yVals.add(new Entry(i, data1[i]));
        }*/

       ArrayList<ILineDataSet> dataSetList = new ArrayList<>();

        LineDataSet set1;
        LineDataSet set2;

      /*  if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {


            set1 = new LineDataSet(yVals, "");
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setColor(Color.WHITE);
            dataSetList.add(set1);

            set2 = new LineDataSet(yVals, "");
            set2.setDrawCircles(false);
            set2.setLineWidth(1.8f);
            set2.setColor(Color.WHITE);
            dataSetList.add(set2);


            LineData data = new LineData(dataSetList);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            mChart.setData(data);
        }*/
        set1 = new LineDataSet(yVals, "");
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setColor(Color.WHITE);
        dataSetList.add(set1);

        set2 = new LineDataSet(yVals2, "");
        set2.setDrawCircles(false);
        set2.setLineWidth(1.8f);
        set2.setColor(Color.WHITE);
        dataSetList.add(set2);


        LineData data = new LineData(dataSetList);
        data.setValueTextSize(9f);
        data.setDrawValues(false);
        mChart.setData(data);
    }

    public void back(View view){
        finish();
    }

    public static void startAction(Activity activity) {
        activity.startActivity(new Intent(activity, ECGShowActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }
}