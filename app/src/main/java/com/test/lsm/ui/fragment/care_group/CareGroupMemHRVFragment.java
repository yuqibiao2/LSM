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
    @BindView(R.id.tv_fatigue)
    TextView tvFatigue;
    @BindView(R.id.iv_fatigue)
    ImageView ivFatigue;
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    @BindView(R.id.iv_pressure)
    ImageView ivPressure;

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
        int bodyFatigue = hrvInfo.getBodyFatigue();
        HrvUtils.inflateBodyFitness(getContext() , bodyFatigue , ivPhysical , tvPhysical);
        int moodStability = hrvInfo.getMoodStability();
        HrvUtils.inflateMoodStability(getContext() , moodStability , ivFatigue ,tvFatigue );
        int mindFitness = hrvInfo.getMindFitness();
        HrvUtils.inflateMindFitness(getContext(), mindFitness, ivPressure , tvPressure);
    }

}
