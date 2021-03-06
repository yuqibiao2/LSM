package com.test.lsm.ui.fragment.hr_record;

import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.UpdateDayHrRecordEvent;
import com.test.lsm.bean.event.UpdateHrRecordEvent;
import com.test.lsm.bean.vo.GetHeartChart;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/6
 */
public class DayHrFragment extends HrBaseFragment {
    @BindView(R.id.bc_hr)
    LineChart bcHr;
    @BindView(R.id.tv_current_day)
    TextView tvCurrentDay;
    private List<Entry> mValues;

    private int userId;

    private String dateTime;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_day_hr;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();

        MyApplication application = (MyApplication) getActivity().getApplication();
        UserLoginReturn.PdBean user = application.getUser();
        userId = user.getUSER_ID();

        dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", Calendar.getInstance().getTime());

        mValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            mValues.add(new Entry(i, 0));
        }

        EventBus.getDefault().register(this);

    }

    @Override
    protected void initView() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        tvCurrentDay.setText("心率紀錄 - "+day+"日");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }

    @Override
    protected int getLineColor() {

        return Color.parseColor("#76B1FF");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            getData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getData() {
        mValues.clear();
        for (int i = 0; i < 24; i++) {
            mValues.add(new Entry(i, 0));
        }
        APIMethodManager.getInstance().getHeartChart(userId, 1, dateTime, new IRequestCallback<GetHeartChart>() {
            @Override
            public void onSuccess(GetHeartChart result) {
                int maxHr = Integer.MIN_VALUE;
                int minHr = Integer.MAX_VALUE;
                int avgHr = 0;
                int total = 0;
                int count = 0;
                List<GetHeartChart.PdBean> pd = result.getPd();
                for (GetHeartChart.PdBean pdBean : pd) {
                    int hour = Integer.parseInt(pdBean.getHEART_TIME());
                    Entry entry1 = mValues.get(hour);
                    Integer hrValue = Integer.parseInt(pdBean.getHEART_VALUE());
                    entry1.setY(hrValue);
                    if (hrValue > 0) {
                        total += hrValue;
                        count++;
                    }
                    if (hrValue > maxHr && hrValue > 0) {
                        maxHr = hrValue;
                    }
                    if (hrValue < minHr && hrValue > 0) {
                        minHr = hrValue;
                    }
                }
                if (count > 0) {
                    avgHr = total / count;
                }

                if (mOnLoadDataSuccess != null) {
                    maxHr = (maxHr == Integer.MIN_VALUE) ? 0 : maxHr;
                    minHr = (minHr == Integer.MAX_VALUE) ? 0 : minHr;
                    mOnLoadDataSuccess.onSuccess(maxHr, minHr, avgHr);
                }
                initChart2(bcHr, mValues);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateHrDate(UpdateHrRecordEvent event) {
        dateTime = event.getDateTime();
        int day = event.getCalendar().get(Calendar.DAY_OF_MONTH);
        tvCurrentDay.setText("心率紀錄 - "+day+"日");
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateHrDate2(UpdateDayHrRecordEvent event) {
        dateTime = event.getDateTime();
        int day = event.getCalendar().get(Calendar.DAY_OF_MONTH);
        tvCurrentDay.setText("心率紀錄 - "+day+"日");
        getData();
    }

}
