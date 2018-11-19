package com.test.lsm.ui.fragment.information_detail;

import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singularwings.rrislib.RRIsVerifier;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.LsmBleData;
import com.test.lsm.db.bean.AFibExpRecord;
import com.test.lsm.db.service.AFibExpRecordService;
import com.test.lsm.db.service.inter.IAFibExpRecordService;
import com.test.lsm.ui.activity.AFibDetailActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.test.lsm.global.SpConstant.SC_AUTO_SCAN;

/**
 * 功能：AFib詳情頁面
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/24
 */
public class AFibDetailFragment extends LsmBaseFragment {

    @BindView(R.id.sc_auto_scan)
    SwitchCompat scAutoScan;
    @BindView(R.id.all_afib)
    AdapterLinearLayout allAfib;
    @BindView(R.id.ll_afib)
    LinearLayout llAfib;
    private MyApplication application;
    private RRIsVerifier mVerifier;
    private IAFibExpRecordService iAFibExpRecordService;
    private AdapterLinearLayout.LinearAdapter adapter;
    private List<AFibExpRecord> aFibExpRecords = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_afib_detail;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getActivity().getApplication();
        mVerifier = new RRIsVerifier();
        iAFibExpRecordService = new AFibExpRecordService();
    }

    @Override
    protected void initView() {

        boolean isRecord = (boolean) MySPUtils.get(getContext() , SC_AUTO_SCAN , false);
        scAutoScan.setChecked(isRecord);

        adapter = new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                aFibExpRecords = iAFibExpRecordService.getALL();
                if (aFibExpRecords.size()>5){
                    aFibExpRecords = aFibExpRecords.subList(0, 5);
                }
                return aFibExpRecords.size();
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.all_afib_item, parent, false);
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
                MySPUtils.put(getContext() , SC_AUTO_SCAN , b);
            }
        });

        application.setOnGetBleDataValueListener(new MyApplication.OnGetBleDataValueListener() {
            @Override
            public void onGet(LsmBleData lsmBleData) {

                boolean isRecord = (boolean) MySPUtils.get(getContext() , SC_AUTO_SCAN , false);

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

        llAfib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AFibDetailActivity.startAction(getContext());
            }
        });

        allAfib.setOnItemClickListener(new AdapterLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AFibDetailActivity.startAction(getContext());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isRecord = (boolean) MySPUtils.get(getContext() , SC_AUTO_SCAN , false);
        scAutoScan.setChecked(isRecord);
    }
}
