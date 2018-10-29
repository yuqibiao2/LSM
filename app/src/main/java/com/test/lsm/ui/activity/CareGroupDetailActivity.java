package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.yyyu.baselibrary.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心群组详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class CareGroupDetailActivity extends LsmBaseActivity {

    private static final String TAG = "CareGroupDetailActivity";

    @BindView(R.id.ib_search_enter)
    ImageButton ibSearchEnter;
    @BindView(R.id.rl_search_show)
    RelativeLayout rlSearchShow;
    @BindView(R.id.rv_cg_detail_nor)
    RecyclerView rvCgDetailNor;
    @BindView(R.id.tv_mask)
    TextView tvMask;


    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private LinearLayoutManager layout;
    private LinearLayoutManager norLayoutManager;
    private View header;

    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_detail;
    }

    @Override
    protected void initView() {

        header = LayoutInflater.from(this).inflate(R.layout.pt_gc_detail_rv_header, null);
        View header2 = LayoutInflater.from(this).inflate(R.layout.pt_gc_detail_rv_header2, null);

        List<String> temp = new ArrayList<>();
        temp.add("1");
        temp.add("1");
        temp.add("1");
        temp.add("1");
        temp.add("1");
        norLayoutManager = new LinearLayoutManager(this);
        rvCgDetailNor.setLayoutManager(norLayoutManager);
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_cg_detail_nor_item, temp) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        };
        adapter.addHeaderView(header,0);
        adapter.addHeaderView(header2 , 1);
        rvCgDetailNor.setAdapter(adapter);

        RecyclerView rvCgDetailExp = header.findViewById(R.id.rv_cg_detail_exp);
        LinearLayoutManager expLayoutManager = new LinearLayoutManager(this);
        expLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCgDetailExp.setLayoutManager(expLayoutManager);
        rvCgDetailExp.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.rv_cg_detail_exp_item, temp) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        });

    }

    @Override
    protected void initListener() {
        rvCgDetailNor.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int scrollY=0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY+=dy;
                if (scrollY >= header.getMeasuredHeight()) {
                    tvMask.setVisibility(View.VISIBLE);
                }else{
                    tvMask.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CareGroupDetailActivity.class);
        context.startActivity(intent);
    }


}
