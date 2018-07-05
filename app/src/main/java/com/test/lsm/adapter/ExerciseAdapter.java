package com.test.lsm.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.bean.FooBean;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class ExerciseAdapter extends BaseQuickAdapter<FooBean, BaseViewHolder> {

    public ExerciseAdapter(int layoutResId, @Nullable List<FooBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FooBean item) {

    }
}
