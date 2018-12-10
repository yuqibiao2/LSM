package com.test.lsm.ui.fragment.care_group;

import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupMemDetailReturn;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.logic.HrvUtils;

import butterknife.BindView;

/**
 * 功能：关心群组成员HRV
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/29
 */
public class CareGroupMemHRVFragment extends LsmBaseFragment {

    @BindView(R.id.tv_physical)
    TextView tvPhysical;
    @BindView(R.id.iv_physical)
    ImageView ivPhysical;
    @BindView(R.id.tv_mood)
    TextView tvMood;
    @BindView(R.id.iv_mood)
    ImageView ivMood;
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    @BindView(R.id.iv_pressure)
    ImageView ivPressure;
    @BindView(R.id.iv_pressure_icon)
    ImageView ivPressureIcon;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_care_group_mem_hrv;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void inflateHrv(GetMonitorGroupMemDetailReturn.DataBean.HrvInfoBean hrvInfo) {
        //---体力状态
        int bodyFitness = hrvInfo.getBodyFitness();
        HrvUtils.inflateBodyFitness(getContext(), bodyFitness, ivPhysical, tvPhysical);
        //---情绪
        int moodStability = hrvInfo.getMoodStability();
        int moodStatus = HrvUtils.inflateMoodStability(getContext(), moodStability, ivMood, tvMood);
        switch (moodStatus) {
            case 1:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion_w1);
                break;
            case 2:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion_w2);
                break;
            case 3:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion_w3);
                break;
            case 4:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion_w4);
                break;
            case 5:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion_w5);
                break;
        }
        //---压力
        int stressTension = hrvInfo.getStressTension();
        HrvUtils.inflateStressTension(getContext(), stressTension, ivPressure, tvPressure);
    }

}
