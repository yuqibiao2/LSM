package com.test.lsm.adapter;

import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.net.GlidUtils;
import com.yyyu.baselibrary.ui.widget.RoundImageView;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/27
 */
public class NorMemAdapter  extends BaseQuickAdapter<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean , BaseViewHolder> {

    public NorMemAdapter(int layoutResId, @Nullable List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetMonitorGroupDetailReturn.DataBean.MemInfoListBean item) {
        helper.setText(R.id.tv_mem_user_name , ""+item.getUserName());
        helper.setText(R.id.tv_mem_tel , ""+item.getPhone());
        helper.setText(R.id.tv_mem_hr , ""+item.getHeartNum()+" bpm");
        helper.setText(R.id.tv_mem_calorie , ""+item.getCalorieValue()+" 千卡");
        helper.setText(R.id.tv_mem_step , ""+item.getHeartNum()+" 步");
        RoundImageView rivIcon = helper.getView(R.id.riv_icon);
        GlidUtils.load(mContext ,rivIcon ,  item.getUserImage());
        int icTag = R.mipmap.ic_mon_mark_blue;
        switch (item.getWatchingTag()){
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
        helper.setImageResource(R.id.iv_tag , icTag);

    }

}
