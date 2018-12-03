package com.test.lsm.ui.fragment.run_bottom;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.HeartChgEvent;
import com.test.lsm.bean.event.RunStartEvent;
import com.test.lsm.bean.event.RunStopEvent;
import com.test.lsm.bean.event.StepChgEvent;
import com.test.lsm.bean.vo.RunRecord;
import com.test.lsm.bean.json.SaveRunRecordReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.TimeUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import org.apache.commons.collections4.queue.CircularFifoQueue;

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
public class RunBottomFragment2 extends LsmBaseFragment {

    private static final String TAG = "RunBottomFragment2";

    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_per_distance)
    TextView tvPerDistance;
    @BindView(R.id.tv_avg_hr)
    TextView tvAvgHr;
    @BindView(R.id.tv_calorie_goal)
    TextView tvCalorieGoal;
    @BindView(R.id.tv_distance_goal)
    TextView tvDistanceGoal;
    @BindView(R.id.pb_calories)
    ProgressBar pbCalories;
    @BindView(R.id.pb_distance)
    ProgressBar pbDistance;
    private MyApplication application;
    private double startDistance;
    private double stopDistance;

    private  boolean isStartRun = false;
    private CircularFifoQueue<Integer> hrBuffer = new CircularFifoQueue(50000);
    private double startCalorieValue;
    private UserLoginReturn.PdBean user;

    private double distance;
    private String startTime;
    private String stopTime;
    private double startCalorie;
    private double stopCalorie;
    private List<LatLng> points;
    private APIMethodManager apiMethodManager;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_run_bottom2;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void runStart(RunStartEvent runStartEvent) {
        isStartRun = true;
        startTimer();
        startTime = MyTimeUtils.getCurrentDateTime();
        startCalorie = application.getCalorieValue();
        startDistance = application.getStepDistance();
        startCalorieValue = application.getCalorieValue();
        tvTime.setText(TimeUtils.countTimer(0L));
        tvDistance.setText("0.0 KM");
        tvPerDistance.setText("0.0 min/km");
        tvAvgHr.setText("0 bpm");
        pbCalories.setProgress(0);
        pbDistance.setProgress(0);
        hrBuffer.clear();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void runStop(RunStopEvent runStopEvent) {
        isStartRun = false;
        stopTime = MyTimeUtils.getCurrentDateTime();
        stopDistance = application.getStepDistance();
        stopCalorie = application.getCalorieValue();
        points = runStopEvent.getPoints();
        //保存跑步记录
        saveRunRecord();
        stopTimer();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onHeartChg(HeartChgEvent heartChgEvent) {
        if (isStartRun){
            //MyToast.showLong(getContext() , "hrBuffer=="+hrBuffer.size());
            hrBuffer.add(heartChgEvent.getHeartNUm());
            int total = 0;
            for (Integer hr: hrBuffer) {
                total += hr;
            }
            int avgHr = total/hrBuffer.size();
            tvAvgHr.setText(avgHr+" bpm");
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onStepChg(StepChgEvent stepChgEvent) {
        if (isStartRun){
            double stopDistance = application.getStepDistance();
            double distance = stopDistance - startDistance;
            double stopCalorieValue = application.getCalorieValue();
            double calories = stopCalorieValue - startCalorieValue;
            tvDistance.setText((double)Math.round(distance*100)/100+" KM");
            double space = distance/second*60;
            tvPerDistance.setText((double)Math.round(space*100)/100+" min/km");
            tvDistanceGoal.setText(""+(double)Math.round(distance*100)/100);
            tvCalorieGoal.setText(""+(double)Math.round(calories*100)/100);
            if (calories<450){
                pbCalories.setProgress(new Double(calories*1000).intValue());
            }else{
                pbCalories.setProgress(450000);
            }
            if (distance<12){
                pbDistance.setProgress(new Double(distance*1000).intValue());
            }else {
                pbDistance.setProgress(1200);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStartTimer = false;
        EventBus.getDefault().unregister(this);
    }

    private boolean isStartTimer = false;
    private long second = 0;
    /**
     * 开始计时
     */
    private void startTimer() {
        isStartTimer = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStartTimer) {
                    try {
                        second++;
                        tvTime.post(new Runnable() {
                            @Override
                            public void run() {
                                if (tvTime != null) {
                                    tvTime.setText(TimeUtils.countTimer(second));
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 结束计时
     */
    private void stopTimer() {
        second = 0;
        isStartTimer = false;
        //tvTime.setText(TimeUtils.countTimer(0L));
    }

    /**
     * 保存跑步记录
     */
    private void saveRunRecord() {
       /* if (points.size() < 2) {
            MyToast.showLong(getContext(), "您移动的距离太小，记录数据失败！");
            return;
        }*/
        RunRecord runRecord = new RunRecord();
        runRecord.setUserId(user.getUSER_ID());
        runRecord.setStartTime(startTime);
        runRecord.setStopTime(stopTime);
        double distance = stopDistance - startDistance;
        runRecord.setDistance(((double)Math.round(distance*100)/100));
        runRecord.setCoordinateInfo(mGson.toJson(points));
        runRecord.setRunTime(""+TimeUtils.countTimer(second));
        double calorie = stopCalorie - startCalorie;
        runRecord.setCalorieValue("" + ((double)Math.round(calorie*100)/100));
        int maxHr = 0;
        int hrTotal = 0;
        for (Integer hr : hrBuffer) {
            if (hr > maxHr) {
                maxHr = hr;
            }
            hrTotal += hr;
        }
        int avgHr = 0;
        if (hrBuffer.size()>0){
            avgHr = hrTotal / hrBuffer.size();
        }
        runRecord.setAvgHeart("" + avgHr);
        runRecord.setMaxHeart("" + maxHr);
        apiMethodManager.saveRunRecord(runRecord, new IRequestCallback<SaveRunRecordReturn>() {
            @Override
            public void onSuccess(SaveRunRecordReturn result) {
                MyLog.d(TAG, "saveRunRecord==成功==" + result);
                hrBuffer.clear();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "saveRunRecord==异常==" + throwable.getMessage());
            }
        });
    }

}
