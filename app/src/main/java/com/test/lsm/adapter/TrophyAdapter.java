package com.test.lsm.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.json.QueryActivityGoodsReturn;
import com.test.lsm.net.GlidUtils;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.Date;
import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/7
 */
public class TrophyAdapter extends PagerAdapter {

    private Context mContext;
    private List<QueryActivityGoodsReturn.PdBean.PdBeanItem> mData;

    public TrophyAdapter(Context context, List<QueryActivityGoodsReturn.PdBean.PdBeanItem> data) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.vp_item_trophy, container, false);
        ImageView ivTrophy = (ImageView) view.findViewById(R.id.iv_trophy);
        TextView tvUpper = (TextView) view.findViewById(R.id.tv_upper);
        TextView tvLower = (TextView) view.findViewById(R.id.tv_lower);
        TextView tvGoodsName = (TextView) view.findViewById(R.id.tv_goods_name);

        QueryActivityGoodsReturn.PdBean.PdBeanItem item = mData.get(position);
        Date startTime = MyTimeUtils.parseDate("yyyy-MM-dd HH:mm", item.getSTART_TIME());
        Date endTime = MyTimeUtils.parseDate("yyyy-MM-dd HH:mm", item.getEND_TIME());
        String upperTime = MyTimeUtils.formatDateTime("MM/dd", startTime);
        String lowerTime = MyTimeUtils.formatDateTime("MM/dd", endTime);
        GlidUtils.load(mContext, ivTrophy, item.getGOODS_PIC());
        tvUpper.setText("" + upperTime);
        tvLower.setText("" + lowerTime);
        tvGoodsName.setText("" + item.getGOODS_NAME());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
