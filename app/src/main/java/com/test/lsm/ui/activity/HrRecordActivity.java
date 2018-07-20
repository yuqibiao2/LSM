package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.event.UpdateDayHrRecordEvent;
import com.test.lsm.bean.event.UpdateHourHrRecordEvent;
import com.test.lsm.bean.event.UpdateHrRecordEvent;
import com.test.lsm.bean.event.UpdateMouthHrRecordEvent;
import com.test.lsm.ui.dialog.DateTimeSelectDialog;
import com.test.lsm.ui.fragment.hr_record.DayHrFragment;
import com.test.lsm.ui.fragment.hr_record.HourHrFragment;
import com.test.lsm.ui.fragment.hr_record.HrBaseFragment;
import com.test.lsm.ui.fragment.hr_record.MouthHrFragment;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import me.relex.circleindicator.CircleIndicator;

/**
 * 功能：心率记录界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class HrRecordActivity extends LsmBaseActivity {

    private static final String TAG = "HrRecordActivity";

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

    private int currentYear;
    private int currentMouth;
    private int currentDay;
    private  int currentHour;

    private HrType hrType = HrType.HOUR;
    private Calendar calendar;
    private List<Fragment> fragmentList;
    private HourHrFragment hourHrFragment;
    private DayHrFragment dayHrFragment;
    private MouthHrFragment mouthHrFragment;

    private enum HrType{
        Mouth, DAY, HOUR
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hr_record;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        fragmentList = new ArrayList<>();
        hourHrFragment = new HourHrFragment();
        dayHrFragment = new DayHrFragment();
        mouthHrFragment = new MouthHrFragment();
        fragmentList.add(hourHrFragment);
        fragmentList.add(dayHrFragment);
        fragmentList.add(mouthHrFragment);
    }

    @Override
    protected void initView() {
        vpHr.setOffscreenPageLimit(3);
        calendar = Calendar.getInstance();
        setDateTimeView();
    }

    private void setDateTimeView(){
        currentYear = calendar.get(Calendar.YEAR);
        currentMouth = calendar.get(Calendar.MONTH)+1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        tvDatetime.setText(currentYear + "/" + currentMouth + "/" + currentDay + "," + currentHour + "時");
    }

    @Override
    protected void initListener() {

        hourHrFragment.setOnLoadDataSuccess(new HrBaseFragment.OnLoadDataSuccess() {
            @Override
            public void onSuccess(int maxHr, int minHr, int avgHr) {
                switch (hrType){
                    case HOUR:{
                        updateStatsHrValue(maxHr , minHr , avgHr);
                        break;
                    }
                    case DAY:{
                        break;
                    }
                    case Mouth:{
                        break;
                    }
                }
            }
        });

        dayHrFragment.setOnLoadDataSuccess(new HrBaseFragment.OnLoadDataSuccess() {
            @Override
            public void onSuccess(int maxHr, int minHr, int avgHr) {
                switch (hrType){
                    case HOUR:{
                        break;
                    }
                    case DAY:{
                        updateStatsHrValue(maxHr , minHr , avgHr);
                        break;
                    }
                    case Mouth:{
                        break;
                    }
                }
            }
        });

        mouthHrFragment.setOnLoadDataSuccess(new HrBaseFragment.OnLoadDataSuccess() {
            @Override
            public void onSuccess(int maxHr, int minHr, int avgHr) {
                switch (hrType){
                    case HOUR:{
                        break;
                    }
                    case DAY:{
                        break;
                    }
                    case Mouth:{
                        updateStatsHrValue(maxHr , minHr , avgHr);
                        break;
                    }
                }
            }
        });

        tvDatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeSelectDialog dateTimeSelectDialog = new DateTimeSelectDialog(HrRecordActivity.this);
                dateTimeSelectDialog.setmOnDateTimeSelectListener(new DateTimeSelectDialog.OnDateTimeSelectListener() {
                    @Override
                    public void onSelect(int year, int mouth, int day, int hour) {
                        currentYear= year;
                        currentMouth = mouth;
                        currentDay = day;
                        currentHour = hour;
                        tvDatetime.setText(currentYear + "/" + currentMouth + "/" + currentDay + "," + currentHour + "時");
                        calendar.set(currentYear , currentMouth-1 , currentDay , currentHour,0);
                        // 更新图表数据
                        String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                        EventBus.getDefault().post(new UpdateHrRecordEvent(dateTime));
                    }
                });
                dateTimeSelectDialog.show();
            }
        });

        vpHr.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        hrType = HrType.HOUR;
                        break;
                    case 1:
                        hrType = HrType.DAY;
                        break;
                    case 2:
                        hrType = HrType.Mouth;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void updateStatsHrValue(int maxHr, int minHr, int avgHr) {
      tvMax.setText(""+maxHr);
      tvMin.setText(""+minHr);
      tvAvg.setText(""+avgHr);
    }

    @Override
    protected void initData() {
        super.initData();
        vpHr.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position) ;
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        ciHr.setViewPager(vpHr);
    }

    /**
     * 之前
     * @param view
     */
    public void backward(View view){
        switch (hrType){
            case HOUR:{
                calendar.add(Calendar.HOUR, -1);
                String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                EventBus.getDefault().post(new UpdateHourHrRecordEvent(dateTime));
                break;
            }
            case DAY:{
                calendar.add(Calendar.DATE, -1);
                String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                EventBus.getDefault().post(new UpdateDayHrRecordEvent(dateTime));
                break;
            }

            case Mouth:{
                calendar.add(Calendar.MONTH, -1);
                String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                EventBus.getDefault().post(new UpdateMouthHrRecordEvent(dateTime));
                break;
            }
        }
        setDateTimeView();
    }

    /**
     * 之后
     * @param view
     */
    public void forward(View view){
        switch (hrType){
            case HOUR:{
                calendar.add(Calendar.HOUR, 1);
                String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                EventBus.getDefault().post(new UpdateHourHrRecordEvent(dateTime));
                break;
            }
            case DAY:{
                calendar.add(Calendar.DATE, 1);
                String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                EventBus.getDefault().post(new UpdateDayHrRecordEvent(dateTime));
                break;
            }

            case Mouth:{
                calendar.add(Calendar.MONTH, 1);
                String dateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd HH:mm", calendar.getTime());
                EventBus.getDefault().post(new UpdateMouthHrRecordEvent(dateTime));
                break;
            }
        }
        setDateTimeView();
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


   /* private enum HrFragment {

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

    }*/


}
