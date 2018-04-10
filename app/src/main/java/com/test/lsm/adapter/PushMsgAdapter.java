package com.test.lsm.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.PushMsgBean;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/10
 */
public class PushMsgAdapter extends BaseQuickAdapter<PushMsgBean , BaseViewHolder>{

    private Context mContext;

    public PushMsgAdapter(Context context , int layoutResId, @Nullable List<PushMsgBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, PushMsgBean item) {
        Glide.with(mContext).load(item.getImgUrl()).crossFade().into((ImageView) helper.getView(R.id.iv_msg_icon));
        helper.setText(R.id.tv_msg_title , item.getTitle());
        helper.setText(R.id.tv_msg_datetime , item.getDatetime());
    }
}
