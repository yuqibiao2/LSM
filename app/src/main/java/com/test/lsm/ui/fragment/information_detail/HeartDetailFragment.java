package com.test.lsm.ui.fragment.information_detail;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.form.QueryHRVInfo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.HrRecordActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：心率详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/8
 */
public class HeartDetailFragment extends LsmBaseFragment {

    private static final String TAG = "HeartDetailFragment";

    @BindView(R.id.v_status1)
    View vStatus1;
    @BindView(R.id.v_status2)
    View vStatus2;
    @BindView(R.id.v_status3)
    View vStatus3;
    @BindView(R.id.v_status4)
    View vStatus4;
    @BindView(R.id.tv_status1)
    TextView tvStatus1;
    @BindView(R.id.tv_status2)
    TextView tvStatus2;
    @BindView(R.id.tv_status3)
    TextView tvStatus3;
    @BindView(R.id.tv_status4)
    TextView tvStatus4;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_avg_hr)
    TextView tvAvgHr;
    @BindView(R.id.tv_max_hr)
    TextView tvMaxHr;
    @BindView(R.id.ll_hr_value)
    LinearLayout llHrValue;

    private List<View> statusList;
    private APIMethodManager apiMethodManager;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_heart_detail;
    }

    @Override
    protected void initView() {
        statusList = new ArrayList<>(4);
        statusList.add(vStatus1);
        statusList.add(vStatus2);
        statusList.add(vStatus3);
        statusList.add(vStatus4);
    }

    @Override
    public void onResume() {
        super.onResume();
        CircularFifoQueue<Integer> hrBuffer = Constant.hrBuffer2;
        int maxHr = 0;
        int avgHr=0;
        int total = 0;
        for (Integer hrValue : hrBuffer) {
            if (hrValue > maxHr) {
                maxHr = hrValue;
            }
            total = total + hrValue;
        }
        if (hrBuffer.size()>0){
            avgHr = total / hrBuffer.size();
        }
        tvAvgHr.setText("平均心率                " + avgHr + " bpm");
        tvMaxHr.setText("最大心率                " + maxHr + " bpm");
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
    public void updateHeart(RefreshHearthInfoEvent heartChgEvent) {

        CircularFifoQueue<Long> rriBuffer = Constant.rriBuffer;
        StringBuffer rrlIntervalSb = new StringBuffer();
        for (int i = 0; i < rriBuffer.size(); i++) {
            Long rriValue = rriBuffer.get(i);
            if (i == rriBuffer.size() - 1) {
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
                    tvUpdateTime.setText("更新时间：" + MyTimeUtils.getCurrentDateTime());
                    GetHRVInfoReturn.HRVIndexBean hrvIndexBean = hrvIndex.get(0);
                    //---体力状态
                    Integer bodyFitness = Integer.parseInt(hrvIndexBean.getBodyFitness());
                    if (bodyFitness >= 30) {
                        chgStatus(vStatus1, 1);
                        tvStatus1.setText("过度暴动");
                    } else if (bodyFitness >= 10) {
                        chgStatus(vStatus1, 2);
                        tvStatus1.setText("拼劲十足");
                    } else if (bodyFitness >= -10) {
                        chgStatus(vStatus1, 3);
                        tvStatus1.setText("正常范围");
                    } else if (bodyFitness >= -30) {
                        chgStatus(vStatus1, 4);
                        tvStatus1.setText("疲劳");
                    } else if (bodyFitness >= -50) {
                        chgStatus(vStatus1, 5);
                        tvStatus1.setText("体力透支");
                    } else {
                        chgStatus(vStatus1, 5);
                        tvStatus1.setText("体力透支");
                    }
                    //---身体疲劳
                    Integer bodyFatigue = Integer.parseInt(hrvIndexBean.getBodyFatigue());
                    if (bodyFatigue <= 20) {
                        chgStatus(vStatus2, 1);
                        tvStatus2.setText("正常范围");
                    } else if (bodyFatigue <= 40) {
                        chgStatus(vStatus2, 2);
                        tvStatus2.setText("略疲劳");
                    } else if (bodyFatigue <= 60) {
                        chgStatus(vStatus2, 3);
                        tvStatus2.setText("疲劳");
                    } else if (bodyFatigue <= 80) {
                        chgStatus(vStatus2, 4);
                        tvStatus2.setText("太疲劳");
                    } else if (bodyFatigue <= 100) {
                        chgStatus(vStatus2, 5);
                        tvStatus2.setText("过度疲劳");
                    } else {
                        chgStatus(vStatus2, 5);
                        tvStatus2.setText("过度疲劳");
                    }
                    //---压力紧张
                    Integer stressTension = Integer.parseInt(hrvIndexBean.getStressTension());
                    if (stressTension <= -30) {//过渡松散
                        tvStatus3.setText("过度松散");
                        chgStatus(vStatus3, 3);
                    } else if (stressTension <= -10) {//松散
                        tvStatus3.setText("松散");
                        chgStatus(vStatus3, 2);
                    } else if (stressTension <= 10) {//正常
                        chgStatus(vStatus3, 1);
                        tvStatus3.setText("正常范围");
                    } else if (stressTension <= 30) {//紧张
                        tvStatus3.setText("紧张");
                        chgStatus(vStatus3, 4);
                    } else if (stressTension <= 50) {//过渡紧张
                        tvStatus3.setText("过度紧张");
                        chgStatus(vStatus3, 5);
                    } else {
                        chgStatus(vStatus3, 5);
                        tvStatus3.setText("过度紧张");
                    }
                    //---心情稳定
                    Integer moodStability = Integer.parseInt(hrvIndexBean.getMoodStability());
                    if (moodStability <= -30) {//过渡低落
                        chgStatus(vStatus4, 5);
                        tvStatus4.setText("过度低落");
                    } else if (moodStability <= -10) {//低落
                        chgStatus(vStatus4, 4);
                        tvStatus4.setText("低落");
                    } else if (moodStability <= 10) {//良好
                        tvStatus4.setText("良好");
                        chgStatus(vStatus4, 1);
                    } else if (moodStability <= 30) {//兴奋
                        chgStatus(vStatus4, 2);
                        tvStatus4.setText("兴奋");
                    } else if (moodStability <= 50) {//过渡兴奋
                        chgStatus(vStatus4, 3);
                        tvStatus4.setText("过度兴奋");
                    } else {
                        chgStatus(vStatus4, 3);
                        tvStatus4.setText("过度兴奋");
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "getHRVInfo异常：" + throwable.getMessage());
            }
        });

    }


    public void chgStatus(View view, int status) {
        switch (status) {
            case 0:
                view.setEnabled(false);
                break;
            case 1:
                view.setBackgroundResource(R.drawable.hrv_status1_bg);
                break;
            case 2:
                view.setBackgroundResource(R.drawable.hrv_status2_bg);
                break;
            case 3:
                view.setBackgroundResource(R.drawable.hrv_status3_bg);
                break;
            case 4:
                view.setBackgroundResource(R.drawable.hrv_status4_bg);
                break;
            case 5:
                view.setBackgroundResource(R.drawable.hrv_status5_bg);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
