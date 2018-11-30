package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.singularwings.rrislib.RRIsVerifier;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.AFibExpRecordAdapter;
import com.test.lsm.bean.LsmBleData;
import com.test.lsm.bean.event.SCAutoScanChgEvent;
import com.test.lsm.bean.json.GetAFibExpRecordReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

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
    @BindView(R.id.rv_afib)
    RecyclerView rvAfib;
    @BindView(R.id.rl_afib)
    SmartRefreshLayout rlAfib;
    @BindView(R.id.tv_scan_status)
    TextView tvScanStatus;

    private MyApplication application;
    private RRIsVerifier mVerifier;
    private List<GetAFibExpRecordReturn.DataBean.ListBean> aFibExpRecords = new ArrayList<>();
    private boolean isDestroy;
    private int userId;
    private int pageNum = 1;
    private AFibExpRecordAdapter aFibExpRecordAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_afib_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        isDestroy = false;
        application = (MyApplication) getApplication();
        userId = getIntent().getIntExtra("userId", -1);
        mVerifier = new RRIsVerifier();
    }

    @Override
    protected void initView() {
        boolean isRecord = (boolean) MySPUtils.get(AFibDetailActivity.this, SC_AUTO_SCAN, false);
        scAutoScan.setChecked(isRecord);
        if (isRecord){
            tvScanStatus.setTextColor(getResources().getColor(R.color.colorAccent));
            tvScanStatus.setText("Detected");
        }else{
            tvScanStatus.setTextColor(Color.parseColor("#9B9B9B"));
            tvScanStatus.setText("未開啓");
        }
        rlAfib.setRefreshHeader(new MaterialHeader(this));
        rlAfib.setRefreshFooter(new ClassicsFooter(this));
        rvAfib.setLayoutManager(new LinearLayoutManager(this));
        aFibExpRecordAdapter = new AFibExpRecordAdapter(R.layout.all_afib_item, aFibExpRecords);
        rvAfib.setAdapter(aFibExpRecordAdapter);
        View header = LayoutInflater.from(this).inflate(R.layout.all_afib_item_top, null);
        View footer = LayoutInflater.from(this).inflate(R.layout.all_afib_item_bottom, null);
        aFibExpRecordAdapter.addHeaderView(header);
        aFibExpRecordAdapter.addFooterView(footer);
    }

    @Override
    protected void initListener() {

        //AFIb異常監聽
        application.setOnGetBleDataValueListener(new MyApplication.OnGetBleDataValueListener() {
            @Override
            public void onGet(LsmBleData lsmBleData) {
                boolean isRecord = (boolean) MySPUtils.get(AFibDetailActivity.this, SC_AUTO_SCAN, false);
                double rriValue = lsmBleData.getRriValue();
                boolean isExp = mVerifier.feedOneRRI(Double.valueOf(rriValue).intValue());
                if (isExp && isRecord) {
                    requestData(0);
                }
            }
        });

        //---刷新
        rlAfib.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestData(0);
            }
        });

        //---加载更多
        rlAfib.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestData(1);
            }
        });

        //是否監聽切換
        scAutoScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EventBus.getDefault().post(new SCAutoScanChgEvent(b));
                MySPUtils.put(AFibDetailActivity.this, SC_AUTO_SCAN, b);
                if (b){
                    tvScanStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvScanStatus.setText("Detected");
                }else{
                    tvScanStatus.setTextColor(Color.parseColor("#9B9B9B"));
                    tvScanStatus.setText("未開啓");
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        requestData(0);
    }

    /**
     * 获取数据
     *
     * @param type 0：刷新 1：加载更多
     */
    private void requestData(final int type) {
        if (type == 0) {
            pageNum = 1;
        }
        APIMethodManager.getInstance().getAfibExpRecords(provider, userId, pageNum, 20, new IRequestCallback<GetAFibExpRecordReturn>() {
            @Override
            public void onSuccess(GetAFibExpRecordReturn result) {
                int code = result.getCode();
                if (code == 200) {
                    List<GetAFibExpRecordReturn.DataBean.ListBean> records = result.getData().getList();
                    if (type == 0) {//刷新
                        aFibExpRecords.clear();
                        aFibExpRecords.addAll(records);
                    } else if (type == 1) {//加载下一页
                        if (records != null && records.size() > 0) {
                            pageNum++;
                            aFibExpRecords.addAll(records);
                        } else {
                            MyToast.showLong(AFibDetailActivity.this, "沒有更多數據了");
                            //rlAfib.setNoMoreData(true);
                        }
                    }
                    aFibExpRecordAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showLong(AFibDetailActivity.this, "异常：" + result.getMsg());
                }
                rlAfib.finishRefresh();
                rlAfib.finishLoadMore();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(AFibDetailActivity.this, "异常：" + throwable.getMessage());
                rlAfib.finishRefresh();
                rlAfib.finishLoadMore();
            }
        });

    }

    public void back(View view) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    public static void startAction(Context context, Integer userId) {
        Intent intent = new Intent(context, AFibDetailActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

}
