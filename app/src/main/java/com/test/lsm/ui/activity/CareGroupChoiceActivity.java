package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心群组选择
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class CareGroupChoiceActivity extends LsmBaseActivity {

    @BindView(R.id.ib_search_enter)
    ImageButton ibSearchEnter;
    @BindView(R.id.rv_care_group)
    RecyclerView rvCareGroup;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_choice;
    }

    @Override
    protected void initView() {

        rvCareGroup.setLayoutManager(new LinearLayoutManager(this));
        List<String> temp = new ArrayList<>();
        temp.add("1");
        temp.add("1");
        temp.add("1");
        temp.add("1");
        temp.add("1");
        temp.add("1");
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_care_group_show_item, temp) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        };
        rvCareGroup.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

        //---搜索
        ibSearchEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               CareGroupDetailActivity.startAction(CareGroupChoiceActivity.this);
           }
       });

    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CareGroupChoiceActivity.class);
        context.startActivity(intent);
    }


}
