package com.test.lsm.ui.fragment.hr_record;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.test.lsm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/6
 */
public class MouthHrFragment extends HrBaseFragment {

    @BindView(R.id.bc_hr)
    LineChart bcHr;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mouth_hr;
    }

    @Override
    protected void initView() {
        List<Entry> mValues = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            int nextInt = new Random().nextInt(100);
            mValues.add(new Entry(i , nextInt));
        }
        initChart2(bcHr , mValues);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLineColor() {
        return Color.parseColor("#FB61E2");
    }
}
