package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心人员
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/25
 */
public class SetCareGroupActivity extends LsmBaseActivity {

    @BindView(R.id.ib_nav_back)
    ImageButton ibNavBack;
    @BindView(R.id.sc_care_group)
    SwitchCompat scCareGroup;
    @BindView(R.id.rv_care_group)
    RecyclerView rvCareGroup;
    @BindView(R.id.iv_add_care_group)
    ImageView ivAddCareGroup;
    @BindView(R.id.sc_watch_all)
    SwitchCompat scWatchAll;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_care_group;
    }

    @Override
    protected void initView() {

        rvCareGroup.setLayoutManager(new LinearLayoutManager(this));

        List<String> fooList = new ArrayList<>();
        fooList.add("1");
        fooList.add("2");
        fooList.add("3");
        fooList.add("4");
        fooList.add("5");
        fooList.add("5");
        fooList.add("5");
        fooList.add("5");

        rvCareGroup.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_care_group_item, fooList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        });

    }

    @Override
    protected void initListener() {
        //全选
        scWatchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){//全选

                }else{//全不选

                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SetCareGroupActivity.class);
        context.startActivity(intent);
    }

}
