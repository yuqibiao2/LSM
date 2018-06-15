package com.test.lsm.ui.fragment.information_detail;

import android.view.View;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.bean.event.RefreshHearthInfoEvent;
import com.test.lsm.bean.form.QueryHRVInfo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.lsmalgorithm.MyLib;

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
    protected void initListener() {

    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateHeart(RefreshHearthInfoEvent heartChgEvent) {

       /* CircularFifoQueue<Integer> queue = MyLib.queue;
        List<Integer> rriList = new ArrayList<>();
        for (Integer num: queue) {
            if (num>0){
                int rri = 1000 / num;
                for (int i=0 ; i<num;i++){
                    rriList.add(rri);
                }
            }
        }

        StringBuffer rrlIntervalSb = new StringBuffer();
        for (int i=0 ; i<rriList.size() ; i++){
            Integer rriValue = rriList.get(i);
            if (i==rriList.size()-1){
                rrlIntervalSb.append(rriValue+"");
            }else{
                rrlIntervalSb.append(rriValue+",");
            }
        }*/

        CircularFifoQueue<Long> rriBuffer = Constant.rriBuffer;
        StringBuffer rrlIntervalSb = new StringBuffer();
        for (int i=0 ; i<rriBuffer.size() ; i++){
            Long rriValue = rriBuffer.get(i);
            if (i==rriBuffer.size()-1){
                rrlIntervalSb.append(rriValue+"");
            }else{
                rrlIntervalSb.append(rriValue+",");
            }
        }
        QueryHRVInfo hrvInfo = new QueryHRVInfo();
        hrvInfo.setRrInterval(rrlIntervalSb.toString());
        //hrvInfo.setRrInterval("644,667,652,641,642,664,726,816,816,781,746,714,695,683,687,718,816,1246,921,878,781,742,710,699,664,644,656,605,648,753,941,863,804,777,714,687,675,671,667,671,726,746,753,781,820,792,757,722,726,710,695,691,738,746,714,707,734,738,726,703,687,726,738,730,695,703,753,769,730,695,687,683,679,660,660,714,808,1039,960,863,800,773,726,699,691,726,761,820,914,980,832,777,738,703,687,667,660,667,722,750,777,855,996,875,773,757,730,691,671,671,691,765,816,761,742,746,726,707,679,667,675,687,707,730,765,761,726,726,734,707,675,671,675,679,671,667,683,707,699,691,695,726,718,691,675,687,687,683,671,667,664,691,691,671,667,675,710,726,710,699,707,714,722,660,671,683,691,687,683,699,714,691,691,703,675,652,675,625,656,675,687,675,664,671,679,699,660,636,671,699,726,667,703,742,769,738,730,730,718,710,648,671,691,703,691,699,753,785,742,753,773,761,710,695,695,699,687,671,671,699,726,703,730,781,753,722,699,703,699,699,648,667,687,695,683,687,730,746,738,695,683,691,695,710,656,671,691,679");

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
