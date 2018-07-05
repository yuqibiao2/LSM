package com.test.lsm.ui.fragment.information_detail;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.test.lsm.R;
import com.test.lsm.bean.event.CalorieChgEvent;
import com.test.lsm.db.bean.Step;
import com.test.lsm.db.service.StepService;
import com.test.lsm.db.service.inter.IStepService;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.today.step.lib.SportStepJsonUtils;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：卡路里详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/8
 */
public class CalorieDetailFragment extends LsmBaseFragment {


    @BindView(R.id.iv_calorie_status)
    ImageView ivCalorieStatus;
    @BindView(R.id.bc_calorie)
    BarChart bcCalorie;
    @BindView(R.id.tv_calorie_tip)
    TextView tvCalorieTip;

    private IStepService stepService;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        EventBus.getDefault().register(this);
        stepService = new StepService();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_calorie_detail;
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
        setData();
    }

    private void setData() {

        List<Step> currentDatStep = stepService.getCurrentDateStep();

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < 24; i++) {
            int val = 0;
            yVals.add(new BarEntry(i, (int) val));
        }

        for (int i = 0; i < currentDatStep.size(); i++) {
            Step step = currentDatStep.get(i);
            int hour = step.getHour();
            BarEntry barEntry = yVals.get(hour);
            int stepNum = step.getStepNum();
            String calorieByStep = SportStepJsonUtils.getCalorieByStep(stepNum);
            barEntry.setY(Float.parseFloat(calorieByStep));
        }


        BarDataSet set = new BarDataSet(yVals, "卡路里");
        set.setColors(getResources().getColor(R.color.chart_bar));
        set.setDrawValues(false);//不显示value
        BarData data = new BarData(set);

        bcCalorie.setData(data);
        bcCalorie.invalidate();
        bcCalorie.animateY(800);
        bcCalorie.setScaleEnabled(false);
        bcCalorie.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
        bcCalorie.getXAxis().setDrawGridLines(false);//不显示网格
        bcCalorie.getAxisRight().setEnabled(false);//右侧不显示Y轴
        bcCalorie.getDescription().setEnabled(false);//不设置描述
        bcCalorie.setDrawValueAboveBar(true);
        bcCalorie.getAxisLeft().setDrawGridLines(true);//不设置Y轴网格
        bcCalorie.getAxisLeft().setAxisMinValue(0.0f);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateCalorie(CalorieChgEvent calorieChgEvent) {
        Step step = stepService.getStepByHourOnCurrentDay(MyTimeUtils.getCurrentHour());
        if(step==null){
            return;
        }
        String calorieByStep = SportStepJsonUtils.getCalorieByStep(step.getStepNum());
        float calorieNum = Float.parseFloat(calorieByStep);
        //float calorieNum = calorieChgEvent.getCalorieNum();
        if (calorieNum < 100) {
            ivCalorieStatus.setImageResource(R.mipmap.ic_caloria1);
            tvCalorieTip.setText("上个小时你消耗了一份番茄的热量");
        } else if (calorieNum < 200) {
            ivCalorieStatus.setImageResource(R.mipmap.ic_caloria2);
            tvCalorieTip.setText("上个小时你消耗了一份沙拉的热量");
        } else if (calorieNum < 300) {
            ivCalorieStatus.setImageResource(R.mipmap.ic_caloria3);
            tvCalorieTip.setText("上个小时你消耗了一份三明治的热量");
        } else if (calorieNum < 400) {
            ivCalorieStatus.setImageResource(R.mipmap.ic_caloria4);
            tvCalorieTip.setText("上个小时你消耗了一份薯条的热量");
        } else if (calorieNum < 500) {
            ivCalorieStatus.setImageResource(R.mipmap.ic_caloria5);
            tvCalorieTip.setText("上个小时你消耗了一份汉堡的热量");
        } else {
            ivCalorieStatus.setImageResource(R.mipmap.ic_caloria6);
            tvCalorieTip.setText("上个小时你消耗了一份烧鸡的热量");
        }
        setData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
