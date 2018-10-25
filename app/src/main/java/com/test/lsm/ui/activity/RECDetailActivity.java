package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：Rec詳情
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/25
 */
public class RECDetailActivity extends LsmBaseActivity {

    @BindView(R.id.ib_nav_back)
    ImageButton ibNavBack;
    @BindView(R.id.sp_time_interval)
    Spinner spTimeInterval;
    @BindView(R.id.iv_rec)
    ImageView ivRec;
    @BindView(R.id.tv_rec_tip)
    TextView tvRecTip;
    @BindView(R.id.iv_sny)
    ImageView ivSny;
    @BindView(R.id.iv_sny_tip)
    TextView ivSnyTip;
    @BindView(R.id.all_rec_undo)
    AdapterLinearLayout allRecUndo;
    @BindView(R.id.rv_rec_done)
    RecyclerView rvRecDone;

    private List<String> timeIntervalList;

    @Override
    public int getLayoutId() {
        return R.layout.acitivty_rec_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        timeIntervalList = new ArrayList<>();
        timeIntervalList.add("30分鐘");
        timeIntervalList.add("45分鐘");
        timeIntervalList.add("60分鐘");
        timeIntervalList.add("85分鐘");
    }

    @Override
    protected void initView() {
        allRecUndo.setAdapter(new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                return 5;
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                return LayoutInflater.from(RECDetailActivity.this).inflate(R.layout.all_rec_item , parent , false);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(RECDetailActivity.this, android.R.layout.simple_spinner_item, timeIntervalList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTimeInterval.setAdapter(adapter);

        List<String> doneList = new ArrayList<>();
        doneList.add("1");
        doneList.add("2");
        doneList.add("3");
        doneList.add("4");
        doneList.add("4");
        doneList.add("4");
        doneList.add("4");
        doneList.add("4");

        rvRecDone.setLayoutManager(new LinearLayoutManager(this));

        rvRecDone.setAdapter(new BaseQuickAdapter<String , BaseViewHolder>(R.layout.rv_rec_done_item , doneList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        });
    }

    @Override
    protected void initListener() {

    }

    public void back(View view){
        finish();
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context , RECDetailActivity.class);
        context.startActivity(intent);
    }

}
