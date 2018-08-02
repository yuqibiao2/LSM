package com.test.lsm.ui.fragment.information_detail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.form.QueryHRVInfo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.global.SpConstant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.HrRecordActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.test.lsm.global.SpConstant.LASTED_HRV_UPDATE_TIME;

/**
 * 功能：心率详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/8
 */
public class HeartDetailFragment extends LsmBaseFragment {

    private static final String TAG = "HeartDetailFragment";
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_avg_hr)
    TextView tvAvgHr;
    @BindView(R.id.tv_max_hr)
    TextView tvMaxHr;
    @BindView(R.id.ll_hr_value)
    LinearLayout llHrValue;
    @BindView(R.id.tv_hrv_value_num)
    TextView tvHrvValueNum;
    @BindView(R.id.tv_physical)
    TextView tvPhysical;
    @BindView(R.id.iv_physical)
    ImageView ivPhysical;
    @BindView(R.id.tv_mental)
    TextView tvMental;
    @BindView(R.id.iv_mental)
    ImageView ivMental;
    @BindView(R.id.tv_emotion)
    TextView tvEmotion;
    @BindView(R.id.iv_emotion)
    ImageView ivEmotion;
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    @BindView(R.id.iv_pressure)
    ImageView ivPressure;
    @BindView(R.id.iv_pressure_icon)
    ImageView ivPressureIcon;

    private List<View> statusList;
    private APIMethodManager apiMethodManager;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        statusList = new ArrayList<>(4);
        EventBus.getDefault().register(this);
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_heart_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        String hrvIndexBeanJsonStr = (String) MySPUtils.get(getContext(), SpConstant.HRV_INFO, "");
        String lastedHrvUpdateTime = (String) MySPUtils.get(getContext(), SpConstant.LASTED_HRV_UPDATE_TIME, "");
        if (!TextUtils.isEmpty(hrvIndexBeanJsonStr)) {
            GetHRVInfoReturn.HRVIndexBean hrvIndexBean = mGson.fromJson(hrvIndexBeanJsonStr, GetHRVInfoReturn.HRVIndexBean.class);
            updateHrvValue(hrvIndexBean);
        }
        if (!TextUtils.isEmpty(lastedHrvUpdateTime)) {
            tvUpdateTime.setText("更新時間：" + lastedHrvUpdateTime);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateHrValue();
    }

    @Override
    protected void initListener() {
        llHrValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HrRecordActivity.startAction(getContext());
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateHearthInfo(RefreshHearthInfoEvent heartChgEvent) {
        List<Integer> rriList = heartChgEvent.getRriList();
        tvHrvValueNum.setText("" + rriList.size());
        updateHrValue();
        if (rriList.size() < 220) {
            return;
        }
        StringBuffer rrlIntervalSb = new StringBuffer();
        for (int i = 0; i < rriList.size(); i++) {
            Integer rriValue = rriList.get(i);
            if (i == rriList.size() - 1) {
                rrlIntervalSb.append(rriValue + "");
            } else {
                rrlIntervalSb.append(rriValue + ",");
            }
        }
        QueryHRVInfo hrvInfo = new QueryHRVInfo();
        hrvInfo.setRrInterval(rrlIntervalSb.toString());

        apiMethodManager.getHRVInfo(hrvInfo, new IRequestCallback<GetHRVInfoReturn>() {
            @Override
            public void onSuccess(GetHRVInfoReturn result) {
                List<GetHRVInfoReturn.HRVIndexBean> hrvIndex = result.getHRVIndex();
                if (hrvIndex != null && hrvIndex.size() > 0) {
                    String currentDateTime = MyTimeUtils.getCurrentDateTime();
                    tvUpdateTime.setText("更新时间：" + currentDateTime);
                    GetHRVInfoReturn.HRVIndexBean hrvIndexBean = hrvIndex.get(0);
                    //保存最新一次HRV值
                    String hrvIndexBeanJsonStr = new Gson().toJson(hrvIndexBean);
                    MySPUtils.put(getContext(), SpConstant.HRV_INFO, hrvIndexBeanJsonStr);
                    MySPUtils.put(getContext(), LASTED_HRV_UPDATE_TIME, currentDateTime);
                    updateHrvValue(hrvIndexBean);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "getHRVInfo异常：" + throwable.getMessage());
            }
        });

    }

    private void updateHrvValue(GetHRVInfoReturn.HRVIndexBean hrvIndexBean) {
        //---体力状态
        Integer bodyFitness = Integer.parseInt(hrvIndexBean.getBodyFitness());
        Constant.lastedBodyFitness = bodyFitness;
        if (bodyFitness >= 30) {
            chgStatus1(ivPhysical, 5);
            tvPhysical.setText(getStr(R.string.physical_level5));
        } else if (bodyFitness >= 10) {
            chgStatus1(ivPhysical, 4);
            tvPhysical.setText(getStr(R.string.physical_level4));
        } else if (bodyFitness >= -10) {
            chgStatus1(ivPhysical, 3);
            tvPhysical.setText(getStr(R.string.physical_level3));
        } else if (bodyFitness >= -30) {
            chgStatus1(ivPhysical, 2);
            tvPhysical.setText(getStr(R.string.physical_level2));
        } else if (bodyFitness >= -50) {
            chgStatus1(ivPhysical, 1);
            tvPhysical.setText(getStr(R.string.physical_level1));
        } else {
            chgStatus1(ivPhysical, 1);
            tvPhysical.setText(getStr(R.string.physical_level1));
        }

        //---脑力
        int mindFitness = Integer.parseInt(hrvIndexBean.getMindFitness());
        if (mindFitness >= 30) {
            chgStatus1(ivMental, 5);
            tvMental.setText(getStr(R.string.fitness_level5));
        } else if (mindFitness >= 10) {
            chgStatus1(ivMental, 4);
            tvMental.setText(getStr(R.string.fitness_level4));
        } else if (mindFitness >= -10) {
            chgStatus1(ivMental, 3);
            tvMental.setText(getStr(R.string.fitness_level3));
        } else if (mindFitness >= -30) {
            chgStatus1(ivMental, 2);
            tvMental.setText(getStr(R.string.fitness_level2));
        } else if (mindFitness >= -50) {
            chgStatus1(ivMental, 1);
            tvMental.setText(getStr(R.string.fitness_level1));
        } else {
            chgStatus1(ivMental, 1);
            tvMental.setText(getStr(R.string.fitness_level1));
        }

        //---压力
        Integer stress = Integer.parseInt(hrvIndexBean.getStressTension());
        if (stress <= -30) {//过渡低落
            chgStatus1(ivPressure, 1);
            tvPressure.setText(getStr(R.string.stress_level_1));
        } else if (stress <= -10) {//低落
            chgStatus1(ivPressure, 2);
            tvPressure.setText(getStr(R.string.stress_level_2));
        } else if (stress <= 10) {//良好
            chgStatus1(ivPressure, 3);
            tvPressure.setText(getStr(R.string.stress_level_3));
        } else if (stress <= 30) {//兴奋
            chgStatus1(ivPressure, 4);
            tvPressure.setText(getStr(R.string.stress_level_4));
        } else if (stress <= 50) {//过渡兴奋
            chgStatus1(ivPressure, 5);
            tvPressure.setText(getStr(R.string.stress_level_5));
        } else {
            chgStatus2(ivPressure, 5);
            tvPressure.setText(getStr(R.string.stress_level_5));
        }

        //---情绪
        Integer mood = Integer.parseInt(hrvIndexBean.getMoodStability());
        if (mood <= -30) {//过渡松散
            tvEmotion.setText(getStr(R.string.mood_level1));
            chgStatus1(ivEmotion, 1);
            ivPressureIcon.setImageResource(R.mipmap.ic_emotion1);
        } else if (mood <= -10) {//松散
            tvEmotion.setText(getStr(R.string.mood_level2));
            chgStatus1(ivEmotion, 2);
            ivPressureIcon.setImageResource(R.mipmap.ic_emotion2);
        } else if (mood <= 10) {//正常
            chgStatus1(ivEmotion, 3);
            tvEmotion.setText(getStr(R.string.mood_level3));
            ivPressureIcon.setImageResource(R.mipmap.ic_emotion3);
        } else if (mood <= 30) {//紧张
            tvEmotion.setText(getStr(R.string.mood_level4));
            chgStatus1(ivEmotion, 4);
            ivPressureIcon.setImageResource(R.mipmap.ic_emotion4);
        } else if (mood <= 50) {//过渡紧张
            tvEmotion.setText(getStr(R.string.mood_level5));
            chgStatus1(ivEmotion, 5);
            ivPressureIcon.setImageResource(R.mipmap.ic_emotion5);
        } else {
            chgStatus1(ivEmotion, 5);
            tvEmotion.setText(getStr(R.string.mood_level5));
            ivPressureIcon.setImageResource(R.mipmap.ic_emotion5);
        }
    }

    private void updateHrValue() {
        CircularFifoQueue<Integer> hrBuffer = Constant.hrBuffer2;
        int maxHr = 0;
        int avgHr = 0;
        int total = 0;
        for (Integer hrValue : hrBuffer) {
            if (hrValue > maxHr) {
                maxHr = hrValue;
            }
            total = total + hrValue;
        }
        if (hrBuffer.size() > 0) {
            avgHr = total / hrBuffer.size();
        }
        tvAvgHr.setText("平均心率                " + avgHr + " bpm");
        tvMaxHr.setText("最大心率                " + maxHr + " bpm");
    }

    public void chgStatus1(View view, int status) {
        switch (status) {
            case 0:
                view.setEnabled(false);
                break;
            case 1:
                view.setBackgroundResource(R.mipmap.ic_bar11);
                break;
            case 2:
                view.setBackgroundResource(R.mipmap.ic_bar12);
                break;
            case 3:
                view.setBackgroundResource(R.mipmap.ic_bar13);
                break;
            case 4:
                view.setBackgroundResource(R.mipmap.ic_bar14);
                break;
            case 5:
                view.setBackgroundResource(R.mipmap.ic_bar15);
                break;
        }
    }

    public void chgStatus2(View view, int status) {
        switch (status) {
            case 0:
                view.setEnabled(false);
                break;
            case 1:
                view.setBackgroundResource(R.mipmap.ic_bar21);
                break;
            case 2:
                view.setBackgroundResource(R.mipmap.ic_bar22);
                break;
            case 3:
                view.setBackgroundResource(R.mipmap.ic_bar23);
                break;
            case 4:
                view.setBackgroundResource(R.mipmap.ic_bar24);
                break;
            case 5:
                view.setBackgroundResource(R.mipmap.ic_bar25);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
