package com.test.lsm.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.net.GlidUtils;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.Date;
import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/27
 */
public class ExpMemAdapter extends BaseQuickAdapter<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean, BaseViewHolder>{


    public ExpMemAdapter(int layoutResId, @Nullable List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean expMemInfo) {
        TextView tvUserName = helper.getView(R.id.tv_user_name);
        TextView tvContent = helper.getView(R.id.tv_hr);
        TextView tvDate = helper.getView(R.id.tv_date);
        TextView tvTime =helper.getView(R.id.tv_time);
        ImageView ivExpIcon = helper.getView(R.id.iv_exp_icon);
        tvUserName.setText(Html.fromHtml(expMemInfo.getUserName()));
        tvContent.setText(expMemInfo.getExpContent());
        String expDateTimeStr = expMemInfo.getExpDateTime();
        Date expDateTime = MyTimeUtils.parseDate(expDateTimeStr);
        String formatDate = MyTimeUtils.formatDateTime("MM/dd", expDateTime);
        tvDate.setText(formatDate);
        String formatTime = MyTimeUtils.formatDateTime("hh:mm", expDateTime);
        tvTime.setText(formatTime+" "+MyTimeUtils.getDuringDay(expDateTime));
        RoundImageView rivIcon =  helper.getView(R.id.riv_icon);
        GlidUtils.load(mContext ,rivIcon ,  expMemInfo.getUserImage());
        GlidUtils.load(mContext , ivExpIcon , expMemInfo.getMsgIcon());
        int icTag = R.mipmap.ic_mon_mark_blue;
        switch (expMemInfo.getWatchingTag()){
            case "1"://blue
                icTag = R.mipmap.ic_mon_mark_blue;
                break;
            case "2"://green
                icTag = R.mipmap.ic_mon_mark_green;
                break;
            case "3"://yellow
                icTag = R.mipmap.ic_mon_mark_yellow;
                break;
            case "4"://purple
                icTag = R.mipmap.ic_mon_mark_purple;
                break;
            case "5"://red
                icTag =R.mipmap.ic_mon_mark_red;
                break;
        }
        ImageView ivTag =  helper.getView(R.id.iv_tag);
        ivTag.setImageResource(icTag);
    }

}
