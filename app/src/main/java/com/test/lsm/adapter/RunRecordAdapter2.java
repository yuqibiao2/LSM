package com.test.lsm.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/16
 */
public class RunRecordAdapter2 extends BaseQuickAdapter<QueryUserRunInfoReturn.PdBean, BaseViewHolder> {

    private FragmentActivity mAct;




    public RunRecordAdapter2(FragmentActivity act , int layoutResId, @Nullable List<QueryUserRunInfoReturn.PdBean> data) {
        super(layoutResId, data);
        this.mAct = act;
    }

    @Override
    protected void convert(final BaseViewHolder helper, QueryUserRunInfoReturn.PdBean item) {
        helper.setText(R.id.tv_city , ""+item.getRUN_ADDRESS());
        helper.setText(R.id.tv_datetime ,""+item.getCREATE_TIME() );
        helper.setText(R.id.tv_distance , ""+item.getDISTANCE()+"km");
        helper.setText(R.id.tv_time_spent , ""+item.getRUN_TIME());
        helper.setText(R.id.tv_avg_hr , "平均心率    "+item.getAVG_HEART()+" bpm");
        helper.setText(R.id.tv_max_hr , "最大心率    "+item.getMAX_HEART()+" bpm");
        helper.setText(R.id.tv_calorie , "卡路里消耗     "+item.getCALORIE_VALUE()+" 大卡");
        String startTime = item.getSTART_TIME();
        String stopTime = item.getSTOP_TIME();
        long spentTime = MyTimeUtils.pareDate(stopTime) - MyTimeUtils.pareDate(startTime);
        double distance = item.getDISTANCE();
        double speed = 0.00;

        int position = helper.getAdapterPosition();
        if (position>0){
            helper.getView(R.id.ll_more_info).setVisibility(View.GONE);
        }

        if (spentTime>0&&distance>0){
            speed= distance*1000*60*60/(spentTime);
        }
        speed = (double) Math.round(speed * 100) / 100;
        helper.setText(R.id.tv_item_speed,  speed+" km/Hrs");
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

    }
}
