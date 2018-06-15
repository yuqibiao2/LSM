package com.test.lsm.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.os.TraceCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.PushMsgBean;
import com.test.lsm.bean.json.GetMsgListReturn;
import com.test.lsm.db.bean.PushMsg;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/10
 */
public class PushMsgAdapter extends BaseQuickAdapter<GetMsgListReturn.PdBean , BaseViewHolder>{

    public static HashMap<Integer , Long> tipHolder = new HashMap<>();

    private Context mContext;
    private List<GetMsgListReturn.PdBean> mData;

    public PushMsgAdapter(Context context , int layoutResId, @Nullable List<GetMsgListReturn.PdBean> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.mData = data;
    }


    @Override
    protected void convert(BaseViewHolder helper, GetMsgListReturn.PdBean item) {

        int position = helper.getAdapterPosition();
        Glide.with(mContext).load(item.getPUSH_IMAGE_URL()).crossFade().into((ImageView) helper.getView(R.id.iv_msg_icon));
        helper.setText(R.id.tv_msg_title , item.getPUSH_TITLE());
        String push_time = item.getPUSH_TIME();
        if (!TextUtils.isEmpty(push_time)){
            long date = MyTimeUtils.parseDateTimeToDate(push_time);
            if (!tipHolder.keySet().contains(position) && !tipHolder.values().contains(date)){
                tipHolder.put(position , date);
            }
            if (tipHolder.keySet().contains(position)){
                helper.getView(R.id.ll_tip).setVisibility(View.VISIBLE);
                String dateTime = MyTimeUtils.formatDateTime("MM月dd日", new Date(date));
                helper.setText(R.id.tv_date , dateTime);
                String week = MyTimeUtils.getWeek(date);
                helper.setText(R.id.tv_week , week);
            }else{
                helper.getView(R.id.ll_tip).setVisibility(View.GONE);
            }

        }
        helper.addOnClickListener(R.id.cv_item);
    }

    public void addData(List<GetMsgListReturn.PdBean> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }

}
