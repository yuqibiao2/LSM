package com.test.lsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.net.GlidUtils;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
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
public class ExpMemInfoAdapter extends AdapterLinearLayout.LinearAdapter{

    private Context mContext;
     private List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> mExpMemInfoList;

    public ExpMemInfoAdapter(Context mContext, List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> mExpMemInfoList) {
        this.mContext = mContext;
        this.mExpMemInfoList = mExpMemInfoList;
    }

    @Override
    public int getItemCount() {
        return mExpMemInfoList.size();
    }

    @Override
    public View getView(ViewGroup parent, int position) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.rv_cg_detail_exp_item, parent, false);
        TextView tvUserName = rootView.findViewById(R.id.tv_user_name);
        TextView tvHr = rootView.findViewById(R.id.tv_hr);
        TextView tvTime = rootView.findViewById(R.id.tv_time);
        GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean expMemInfo = mExpMemInfoList.get(position);
        tvUserName.setText(""+expMemInfo.getUserName());
        tvHr.setText(""+expMemInfo.getHeartNum()+" bpm");
        String expDateTimeStr = expMemInfo.getExpDateTime();
        Date expDateTime = MyTimeUtils.parseDate(expDateTimeStr);
        String formatDateTime = MyTimeUtils.formatDateTime("hh:mm", expDateTime);
        tvTime.setText(formatDateTime+" "+MyTimeUtils.getDuringDay(expDateTime));
        RoundImageView rivIcon = rootView.findViewById(R.id.riv_icon);
        GlidUtils.load(mContext ,rivIcon ,  expMemInfo.getUserImage());
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
        ImageView ivTag = rootView.findViewById(R.id.iv_tag);
        ivTag.setImageResource(icTag);
        return rootView;
    }
}
