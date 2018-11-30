package com.test.lsm.ui.fragment.run_bottom;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.form.QueryHRVInfo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.global.SpConstant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.logic.HrvUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;

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
 * @date 2018/5/30
 */
public class RunBottomFragment1 extends LsmBaseFragment {

    private static final String TAG = "RunBottomFragment1";
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initData();
            mHandler.sendEmptyMessageDelayed(0, 5*60 * 1000);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_run_bottom1;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        apiMethodManager = APIMethodManager.getInstance();
        EventBus.getDefault().register(this);
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
        String  hrvIndexBeanJsonStr = (String) MySPUtils.get(getContext(), SpConstant.HRV_INFO, "");
        if (!TextUtils.isEmpty(hrvIndexBeanJsonStr)){
            GetHRVInfoReturn.HRVIndexBean hrvIndexBean = mGson.fromJson(hrvIndexBeanJsonStr, GetHRVInfoReturn.HRVIndexBean.class);
            updateHrvValue(hrvIndexBean);
        }
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
        HrvUtils.inflateBodyFitness(getContext() ,bodyFitness ,  ivPhysical , tvPhysical);
        //---脑力
        int mindFitness = Integer.parseInt(hrvIndexBean.getMindFitness());
        HrvUtils.inflateMindFitness(getContext() ,mindFitness , ivMental , tvMental);
        //---心情稳定
        Integer mood = Integer.parseInt(hrvIndexBean.getMoodStability());
        HrvUtils.inflateMoodStability(getContext() , mood ,ivEmotion ,  tvEmotion);
        //---身体疲劳
        Integer bodyFatigue = Integer.parseInt(hrvIndexBean.getBodyFatigue());
        HrvUtils.inflateBodyFatigue(getContext() , bodyFatigue , ivDown1 , tvDown1);
        HrvUtils.inflateBodyFatigue(getContext() , bodyFatigue , ivDown2 , tvDown2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
