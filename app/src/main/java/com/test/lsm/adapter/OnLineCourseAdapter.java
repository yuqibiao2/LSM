package com.test.lsm.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetCoachByCourseType;
import com.test.lsm.net.GlidUtils;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/19
 */
public class OnLineCourseAdapter extends BaseQuickAdapter<GetCoachByCourseType.ListBean, BaseViewHolder> {


    public OnLineCourseAdapter(int layoutResId, @Nullable List<GetCoachByCourseType.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetCoachByCourseType.ListBean item) {
        ImageView rivBg = helper.getView(R.id.riv_bg);
        String courseImg = item.getCOURSE_IMG();
        GlidUtils.load(mContext, rivBg, courseImg);
        ImageView rivCoach = helper.getView(R.id.riv_coach);
        String coachImg = item.getCOACH_IMG();
        GlidUtils.load(mContext, rivCoach, coachImg);
        helper.setText(R.id.tv_type , "# "+item.getNAME());
        helper.setText(R.id.tv_coach , "教練 "+item.getCOACH_NAME());
        helper.setText(R.id.tv_people , item.getWEEK_NUMBER()+"  people this week");
    }

}
