package com.test.lsm.adapter;

import android.support.annotation.Nullable;

import com.test.lsm.bean.json.GetCoachByCourseType;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/19
 */
public class DownLineCourseAdapter extends OnLineCourseAdapter {

    public DownLineCourseAdapter(int layoutResId, @Nullable List<GetCoachByCourseType.ListBean> data) {
        super(layoutResId, data);
    }
}
