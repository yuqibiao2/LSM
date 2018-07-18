package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.ui.dialog.DateTimeSelectDialog;
import com.test.lsm.ui.fragment.hr_record.DayHrFragment;
import com.test.lsm.ui.fragment.hr_record.HourHrFragment;
import com.test.lsm.ui.fragment.hr_record.MouthHrFragment;
import com.yyyu.baselibrary.ui.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 功能：心率记录界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class HrRecordActivity extends LsmBaseActivity {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rv_user_icon)
    RoundImageView rvUserIcon;
    @BindView(R.id.rr_bt)
    RelativeLayout rrBt;
    @BindView(R.id.tv_datetime)
    TextView tvDatetime;
    @BindView(R.id.vp_hr)
    ViewPager vpHr;
    @BindView(R.id.ci_hr)
    CircleIndicator ciHr;
    @BindView(R.id.tv_max)
    TextView tvMax;
    @BindView(R.id.tv_avg)
    TextView tvAvg;
    @BindView(R.id.tv_min)
    TextView tvMin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_hr_record;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        tvDatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeSelectDialog dateTimeSelectDialog = new DateTimeSelectDialog(HrRecordActivity.this);
                dateTimeSelectDialog.setmOnDateTimeSelectListener(new DateTimeSelectDialog.OnDateTimeSelectListener() {
                    @Override
                    public void onSelect(int year, int mouth, int day, int hour) {
                        tvDatetime.setText(year + "/" + mouth + "/" + day + "," + hour + "时");
                    }
                });
                dateTimeSelectDialog.show();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        vpHr.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return HrFragment.values()[position].getFragment();
            }

            @Override
            public int getCount() {
                return HrFragment.values().length;
            }
        });
        ciHr.setViewPager(vpHr);
    }

    public void back(View view){
        finish();
    }

    public void toSetting(View view){
      SettingActivity.startAction(this);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, HrRecordActivity.class);
        context.startActivity(intent);
    }


    private enum HrFragment {

        hour(R.layout.fragment_hour_hr, HourHrFragment.class),
        day(R.layout.fragment_day_hr, DayHrFragment.class),
        mouth(R.layout.fragment_mouth_hr, MouthHrFragment.class);

        private Fragment fragment;
        private int resId;
        private final Class<? extends Fragment> clazz;

        HrFragment(int resId, Class<? extends Fragment> clazz) {
            this.resId = resId;
            this.clazz = clazz;
        }

        public Fragment getFragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static HrFragment form(int resId) {

            for (HrFragment fragment : values()) {
                if (fragment.resId == resId) {
                    return fragment;
                }
            }
            return mouth;
        }

        public void onDestory() {
            for (HrFragment fragment : values()) {
                fragment = null;
            }
        }

    }


}
