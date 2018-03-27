package com.test.lsm.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.MenuItem;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/26
 */

public class MenuAdapter extends BaseQuickAdapter<MenuItem , BaseViewHolder>{

    public MenuAdapter(@LayoutRes int layoutResId, @Nullable List<MenuItem> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MenuItem item) {
        helper.setText(R.id.menu_title , item.getTitle());
    }
}
