package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.test.lsm.R;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;

import butterknife.BindView;

/**
 * 功能：AFib详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/25
 */
public class AFibDetailActivity extends LsmBaseActivity {

    @BindView(R.id.ib_nav_back)
    ImageButton ibNavBack;
    @BindView(R.id.sc_auto_scan)
    SwitchCompat scAutoScan;
    @BindView(R.id.all_afib)
    AdapterLinearLayout allAfib;

    @Override
    public int getLayoutId() {
        return R.layout.activity_afib_detail;
    }

    @Override
    protected void initView() {
        allAfib.setAdapter(new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                return 15;
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                return LayoutInflater.from(AFibDetailActivity.this).inflate(R.layout.all_afib_item, parent, false);
            }
        });
    }

    @Override
    protected void initListener() {
        ibNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context , AFibDetailActivity.class);
        context.startActivity(intent);
    }

}
