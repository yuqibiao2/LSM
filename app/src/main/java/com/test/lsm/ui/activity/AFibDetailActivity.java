package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.singularwings.rrislib.RRIsVerifier;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.LsmBleData;
import com.test.lsm.db.bean.AFibExpRecord;
import com.test.lsm.db.service.AFibExpRecordService;
import com.test.lsm.db.service.inter.IAFibExpRecordService;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.test.lsm.global.SpConstant.SC_AUTO_SCAN;

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

    private MyApplication application;
    private RRIsVerifier mVerifier;
    private IAFibExpRecordService iAFibExpRecordService;
    private AdapterLinearLayout.LinearAdapter adapter;
    private List<AFibExpRecord> aFibExpRecords = new ArrayList<>();
    private boolean isDestory;

    @Override
    public int getLayoutId() {
        return R.layout.activity_afib_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        isDestory = false;
        application = (MyApplication) getApplication();
        mVerifier = new RRIsVerifier();
        iAFibExpRecordService = new AFibExpRecordService();
    }

    @Override
    protected void initView() {

        boolean isRecord = (boolean) MySPUtils.get(AFibDetailActivity.this , SC_AUTO_SCAN , false);
        scAutoScan.setChecked(isRecord);

        adapter = new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                aFibExpRecords = iAFibExpRecordService.getALL();
                return aFibExpRecords.size();
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                View item = LayoutInflater.from(AFibDetailActivity.this).inflate(R.layout.all_afib_item, parent, false);
                AFibExpRecord aFibExpRecord = aFibExpRecords.get(position);
                long createTime = aFibExpRecord.getCreateTime();
                String date = MyTimeUtils.formatDateTime("YYYY/MM/dd", new Date(createTime));
                String time = MyTimeUtils.formatDateTime("HH:mm:ss", new Date(createTime));
                TextView tvDate = item.findViewById(R.id.tv_date);
                TextView tvTime = item.findViewById(R.id.tv_time);
                tvDate.setText("" + date);
                tvTime.setText("" + time);
                return item;
            }
        };
        allAfib.setAdapter(adapter);
    }

    @Override
    protected void initListener() {


        scAutoScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MySPUtils.put(AFibDetailActivity.this , SC_AUTO_SCAN , b);
            }
        });

        application.setOnGetBleDataValueListener(new MyApplication.OnGetBleDataValueListener() {
            @Override
            public void onGet(LsmBleData lsmBleData) {
                if (isDestory) return;
                boolean isRecord = (boolean) MySPUtils.get(AFibDetailActivity.this , SC_AUTO_SCAN , false);

                double rriValue = lsmBleData.getRriValue();
                boolean isExp = mVerifier.feedOneRRI(Double.valueOf(rriValue).intValue());
                if (isExp && isRecord) {
                    AFibExpRecord aFibExpRecord = new AFibExpRecord();
                    aFibExpRecord.setCreateTime(new Date().getTime());
                    iAFibExpRecordService.add(aFibExpRecord);
                    allAfib.setAdapter(adapter);
                }
            }
        });

    }

    public void back(View view){
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestory = true;
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context , AFibDetailActivity.class);
        context.startActivity(intent);
    }

}
