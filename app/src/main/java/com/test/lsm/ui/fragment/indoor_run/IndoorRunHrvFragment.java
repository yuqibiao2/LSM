package com.test.lsm.ui.fragment.indoor_run;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.form.QueryHRVInfo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyLog;

import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：室内运动结束后HRV
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/20
 */
public class IndoorRunHrvFragment extends LsmBaseFragment {

    private static final String TAG = "IndoorRunHrvFragment";

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
    @BindView(R.id.tv_down1)
    TextView tvDown1;
    @BindView(R.id.iv_down1)
    ImageView ivDown1;
    @BindView(R.id.tv_down2)
    TextView tvDown2;
    @BindView(R.id.iv_down2)
    ImageView ivDown2;
    private APIMethodManager apiMethodManager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_indoor_run_hrv;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        List<Integer> rriList = Constant.lastedUsefulRriList;
        refreshHrv(rriList);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void refreshData(RefreshHearthInfoEvent RefreshHearthInfoEvent) {
        List<Integer> rriList = RefreshHearthInfoEvent.getRriList();
        refreshHrv(rriList);
    }

    private void refreshHrv(List<Integer> rriList){
        if (rriList.size() <220) {
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
        //MyLog.e(TAG , "rrlIntervalSb："+rrlIntervalSb.toString());
        apiMethodManager.getHRVInfo(hrvInfo, new IRequestCallback<GetHRVInfoReturn>() {
            @Override
            public void onSuccess(GetHRVInfoReturn result) {
                List<GetHRVInfoReturn.HRVIndexBean> hrvIndex = result.getHRVIndex();
                if (hrvIndex != null && hrvIndex.size() > 0) {
                    GetHRVInfoReturn.HRVIndexBean hrvIndexBean = hrvIndex.get(0);
                    //---体力状态
                    Integer bodyFitness = Integer.parseInt(hrvIndexBean.getBodyFitness());
                    if (bodyFitness >= 30) {
                        chgStatus1(ivPhysical, 1);
                        tvPhysical.setText("过度暴动");
                    } else if (bodyFitness >= 10) {
                        chgStatus1(ivPhysical, 2);
                        tvPhysical.setText("拼劲十足");
                    } else if (bodyFitness >= -10) {
                        chgStatus1(ivPhysical, 3);
                        tvPhysical.setText("正常范围");
                    } else if (bodyFitness >= -30) {
                        chgStatus1(ivPhysical, 4);
                        tvPhysical.setText("疲劳");
                    } else if (bodyFitness >= -50) {
                        chgStatus1(ivPhysical, 5);
                        tvPhysical.setText("体力透支");
                    } else {
                        chgStatus1(ivPhysical, 5);
                        tvPhysical.setText("体力透支");
                    }
                    //---脑力
                    Integer bodyFatigue = Integer.parseInt(hrvIndexBean.getBodyFatigue());
                    if (bodyFatigue <= 20) {
                        chgStatus2(ivMental, 1);
                        tvMental.setText("正常范围");
                    } else if (bodyFatigue <= 40) {
                        chgStatus2(ivMental, 2);
                        tvMental.setText("略疲劳");
                    } else if (bodyFatigue <= 60) {
                        chgStatus2(ivMental, 3);
                        tvMental.setText("疲劳");
                    } else if (bodyFatigue <= 80) {
                        chgStatus2(ivMental, 4);
                        tvMental.setText("太疲劳");
                    } else if (bodyFatigue <= 100) {
                        chgStatus2(ivMental, 5);
                        tvMental.setText("过度疲劳");
                    } else {
                        chgStatus2(ivMental, 5);
                        tvMental.setText("过度疲劳");
                    }
                    //---压力紧张
                    Integer stressTension = Integer.parseInt(hrvIndexBean.getStressTension());
                    if (stressTension <= -30) {//过渡松散
                        tvEmotion.setText("过度松散");
                        chgStatus2(ivEmotion, 3);
                    } else if (stressTension <= -10) {//松散
                        tvEmotion.setText("松散");
                        chgStatus2(ivEmotion, 2);
                    } else if (stressTension <= 10) {//正常
                        chgStatus2(ivEmotion, 1);
                        tvEmotion.setText("正常范围");
                    } else if (stressTension <= 30) {//紧张
                        tvEmotion.setText("紧张");
                        chgStatus2(ivEmotion, 4);
                    } else if (stressTension <= 50) {//过渡紧张
                        tvEmotion.setText("过度紧张");
                        chgStatus2(ivEmotion, 5);
                    } else {
                        chgStatus2(ivEmotion, 5);
                        tvEmotion.setText("过度紧张");
                    }
                    //---心情稳定
                    Integer moodStability = Integer.parseInt(hrvIndexBean.getMoodStability());
                    if (moodStability <= -30) {//过渡低落
                        chgStatus2(ivDown1, 5);
                        tvDown1.setText("过度低落");
                        chgStatus2(ivDown2, 5);
                        tvDown2.setText("过度低落");
                    } else if (moodStability <= -10) {//低落
                        chgStatus2(ivDown1, 4);
                        tvDown1.setText("低落");
                        chgStatus2(ivDown2, 4);
                        tvDown2.setText("低落");
                    } else if (moodStability <= 10) {//良好
                        tvDown1.setText("良好");
                        chgStatus2(ivDown1, 1);
                        tvDown2.setText("良好");
                        chgStatus2(ivDown2, 1);
                    } else if (moodStability <= 30) {//兴奋
                        chgStatus2(ivDown1, 2);
                        tvDown1.setText("兴奋");
                        chgStatus2(ivDown2, 2);
                        tvDown2.setText("兴奋");
                    } else if (moodStability <= 50) {//过渡兴奋
                        chgStatus2(ivDown1, 3);
                        tvDown1.setText("过度兴奋");
                        chgStatus2(ivDown2, 3);
                        tvDown2.setText("过度兴奋");
                    } else {
                        chgStatus2(ivDown1, 3);
                        tvDown1.setText("过度兴奋");
                        chgStatus2(ivDown2, 3);
                        tvDown2.setText("过度兴奋");
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "getHRVInfo异常：" + throwable.getMessage());
            }
        });
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

}
