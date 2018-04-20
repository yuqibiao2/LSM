package com.test.lsm.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.PushMsgBean;
import com.test.lsm.db.bean.PushMsg;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/10
 */
public class PushMsgAdapter extends BaseQuickAdapter<PushMsg , BaseViewHolder>{

    private Context mContext;

    public PushMsgAdapter(Context context , int layoutResId, @Nullable List<PushMsg> data) {
        super(layoutResId, data);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, PushMsg item) {
        Glide.with(mContext).load(item.getImgId()).crossFade().into((ImageView) helper.getView(R.id.iv_msg_icon));
        helper.setText(R.id.tv_msg_title , item.getTitle());
        helper.setText(R.id.tv_msg_datetime , item.getDatetime());
    }
}
