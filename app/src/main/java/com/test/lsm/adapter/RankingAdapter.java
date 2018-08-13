package com.test.lsm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.json.QueryAmongReturn;
import com.test.lsm.net.GlidUtils;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/7
 */
public class RankingAdapter extends PagerAdapter {

    private Context mContext;
    private List<QueryAmongReturn.PdBean> mData;

    public RankingAdapter(Context context , List<QueryAmongReturn.PdBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.vp_item_ranking, container, false);
        RelativeLayout rlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
        ImageView ivUserIcon = (ImageView) view.findViewById(R.id.iv_ranking_user_icon);
        TextView tvUsername = (TextView) view.findViewById(R.id.tv_user_name);
        TextView tvRanking = (TextView) view.findViewById(R.id.tv_ranking);
        TextView tvUserPoint = (TextView) view.findViewById(R.id.tv_user_point);

        QueryAmongReturn.PdBean item = mData.get(position);

        GlidUtils.load(mContext , ivUserIcon,item.getUSER_IMAGE());
        tvUsername.setText(""+item.getUSERNAME());
        tvRanking.setText("NO."+new Double(item.getRownum()).intValue());
        tvUserPoint.setText(""+item.getTOTAL_VALUE());
        if (item.getRownum()==1){
            rlItem.setBackground(mContext.getDrawable(R.drawable.bg_ranking1));
        }else{
            rlItem.setBackground(mContext.getDrawable(R.drawable.bg_ranking2));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
