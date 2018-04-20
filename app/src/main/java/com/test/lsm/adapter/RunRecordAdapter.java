package com.test.lsm.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/20
 */
public class RunRecordAdapter extends BaseQuickAdapter<QueryUserRunInfoReturn.PdBean, BaseViewHolder> {

    private Context mContext;

    public RunRecordAdapter(Context context, int layoutResId, @Nullable List<QueryUserRunInfoReturn.PdBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryUserRunInfoReturn.PdBean item) {
        helper.setText(R.id.tv_run_distance, "" + item.getDISTANCE());
        helper.setText(R.id.tv_run_time, "" + item.getRUN_TIME());
        helper.setText(R.id.tv_record_time, "" + item.getCREATE_TIME());
    }

}
