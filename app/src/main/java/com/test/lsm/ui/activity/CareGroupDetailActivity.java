package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.WindowUtils;

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
    @BindView(R.id.rl_bottom_sheet)
    RelativeLayout rlBottomSheet;
    @BindView(R.id.ll_top)
    LinearLayout llTop;


    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private LinearLayoutManager norLayoutManager;
    private View header1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_detail;
    }

    @Override
    protected void initView() {

        header1 = LayoutInflater.from(this).inflate(R.layout.pt_gc_detail_rv_header1, null);
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
        adapter.addHeaderView(header1);
        adapter.addHeaderView(header2);
        rvCgDetailNor.setAdapter(adapter);

        AdapterLinearLayout all_cg_detail_exp = header1.findViewById(R.id.all_cg_detail_exp);

        LinearLayoutManager expLayoutManager = new LinearLayoutManager(this);
        expLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        all_cg_detail_exp.setAdapter(new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                return 8;
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                return LayoutInflater.from(CareGroupDetailActivity.this).inflate(R.layout.rv_cg_detail_exp_item, parent, false);
            }
        });

        int[] size = WindowUtils.getSize(this);
        llTop.measure(0, 0);
        int maxHeight = size[1] - llTop.getMeasuredHeight()- DimensChange.dp2px(this , 28);
        rlBottomSheet.getLayoutParams().height = maxHeight;

    }

    @Override
    protected void initListener() {
        rvCgDetailNor.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int scrollY = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY += dy;
                if (scrollY >= header1.getMeasuredHeight()) {
                    tvMask.setVisibility(View.VISIBLE);
                } else {
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
