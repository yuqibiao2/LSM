package com.test.lsm.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetAFibExpRecordReturn;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.Date;
import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/29
 */
public class AFibExpRecordAdapter extends BaseQuickAdapter<GetAFibExpRecordReturn.DataBean.ListBean , BaseViewHolder> {

    public AFibExpRecordAdapter(int layoutResId, @Nullable List<GetAFibExpRecordReturn.DataBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetAFibExpRecordReturn.DataBean.ListBean item) {
        Date createTime = MyTimeUtils.parseDate(item.getCreateTime());
        String date = MyTimeUtils.formatDateTime("yyyy/MM/dd", createTime);
        String time = MyTimeUtils.formatDateTime("HH:mm:ss", createTime);
        helper.setText(R.id.tv_date , date);
        helper.setText(R.id.tv_time , time);
    }
}
