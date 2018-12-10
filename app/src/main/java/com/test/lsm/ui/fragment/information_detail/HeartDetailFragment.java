package com.test.lsm.ui.fragment.information_detail;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.json.SaveUserHRV;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.vo.QueryHRVInfo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.bean.vo.SaveUserHRVVo;
import com.test.lsm.global.Constant;
import com.test.lsm.global.SpConstant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.HrRecordActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.logic.HrvUtils;
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
    @BindView(R.id.tv_hrv_value_num2)
    TextView tvHrvValueNum2;
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
    private MyApplication application;

    private boolean isDestroy;
    private UserLoginReturn.PdBean user;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        isDestroy = false;
        statusList = new ArrayList<>(4);
        EventBus.getDefault().register(this);
        apiMethodManager = APIMethodManager.getInstance();
        application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
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

        application.setOnGetRriValueListener(new MyApplication.OnGetRriValueListener() {
            @Override
            public void onGet(int rriValue) {
                if (!isDestroy){
                    tvHrvValueNum2.setText(rriValue+"   ---size: "+Constant.rriCounter.size());
                }
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
                //--上传HRV值
                uploadHrv(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "getHRVInfo异常：" + throwable.getMessage());
            }
        });

    }

    /**
     * 上传HRV值到服务器
     *
     * @param result
     */
    private void uploadHrv(GetHRVInfoReturn result) {
        List<GetHRVInfoReturn.HRVIndexBean> hrvIndex = result.getHRVIndex();
        if (hrvIndex != null && hrvIndex.size() > 0) {
            GetHRVInfoReturn.HRVIndexBean hrvIndexBean = hrvIndex.get(0);
            SaveUserHRVVo saveUserHRVVo = new SaveUserHRVVo();
            saveUserHRVVo.setUserId(user.getUSER_ID());
            saveUserHRVVo.setBODYFITNESS(""+hrvIndexBean.getBodyFitness());
            saveUserHRVVo.setBODYFATIGUE(""+hrvIndexBean.getBodyFatigue());
            saveUserHRVVo.setSTRESSTENSION(""+hrvIndexBean.getStressTension());
            saveUserHRVVo.setMOODSTABILITY(""+hrvIndexBean.getMoodStability());
            saveUserHRVVo.setMINDFITNESS(""+hrvIndexBean.getMindFitness());
            saveUserHRVVo.setMINDFATIGUE(""+hrvIndexBean.getMindFatigue());

            apiMethodManager.saveUserHRV(saveUserHRVVo, new IRequestCallback<SaveUserHRV>() {
                @Override
                public void onSuccess(SaveUserHRV result) {
                    String code = result.getResult();
                    MyLog.e(TAG , "==code="+code);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    MyLog.e(TAG , ""+throwable.getMessage());
                }
            });

        }
    }

    private void updateHrvValue(GetHRVInfoReturn.HRVIndexBean hrvIndexBean) {
        //---体力状态
        Integer bodyFitness = Integer.parseInt(hrvIndexBean.getBodyFitness());
        Constant.lastedBodyFitness = bodyFitness;
        HrvUtils.inflateBodyFitness(getContext() , bodyFitness , ivPhysical , tvPhysical);
        //---脑力
        int mindFitness = Integer.parseInt(hrvIndexBean.getMindFitness());
        HrvUtils.inflateMindFitness(getContext() , mindFitness , ivMental , tvMental);
        //---压力
        Integer stress = Integer.parseInt(hrvIndexBean.getStressTension());
        HrvUtils.inflateStressTension(getContext() , stress , ivPressure , tvPressure);
        //---情绪
        Integer mood = Integer.parseInt(hrvIndexBean.getMoodStability());
        int moodStatus = HrvUtils.inflateMoodStability(getContext(), mood, ivEmotion, tvEmotion);
        switch (moodStatus){
            case 1:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion1);
                break;
            case 2:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion2);
                break;
            case 3:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion3);
                break;
            case 4:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion4);
                break;
            case 5:
                ivPressureIcon.setImageResource(R.mipmap.ic_emotion5);
                break;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        EventBus.getDefault().unregister(this);
    }

}
