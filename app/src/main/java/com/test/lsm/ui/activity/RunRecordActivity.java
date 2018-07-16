package com.test.lsm.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.RunRecordAdapter;
import com.test.lsm.bean.form.QueryRunInfoVo;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RunRecordActivity extends LsmBaseActivity {

    @BindView(R.id.srl_run_record)
    SmartRefreshLayout srlRunRecord;
    @BindView(R.id.rv_run_record)
    RecyclerView rvRunRecord;
    private APIMethodManager apiMethodManager;
    private QueryRunInfoVo queryRunInfoVo;

    private int currentPage = 1;
    private List<QueryUserRunInfoReturn.PdBean> mData;
    private RunRecordAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_run_record;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        MyApplication application = (MyApplication) getApplication();
        int user_id = application.getUser().getUSER_ID();
        apiMethodManager = APIMethodManager.getInstance();
        queryRunInfoVo = new QueryRunInfoVo();
        queryRunInfoVo.setPage(currentPage);
        queryRunInfoVo.setPageSize(10);
        queryRunInfoVo.setUserId(user_id);
    }

    @Override
    protected void initView() {
        mData = new ArrayList<>();
        rvRunRecord.setLayoutManager(new LinearLayoutManager(RunRecordActivity.this));
        adapter = new RunRecordAdapter(RunRecordActivity.this, R.layout.rv_item_run_record, mData);
        rvRunRecord.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RunRecordDetailActivity.startAction(RunRecordActivity.this, mData.get(position).getID());
            }
        });

        srlRunRecord.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                queryRunInfoVo.setPage(currentPage);
                apiMethodManager.queryUserRunInfo(queryRunInfoVo, new IRequestCallback<QueryUserRunInfoReturn>() {
                    @Override
                    public void onSuccess(QueryUserRunInfoReturn result) {
                        String code = result.getResult();
                        if ("01".equals(code)) {
                            mData.clear();
                            mData.addAll(result.getPd());
                            adapter.notifyDataSetChanged();
                        }
                        srlRunRecord.finishRefresh();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        MyToast.showLong(RunRecordActivity.this, "数据加载失败：" + throwable.getMessage());
                        srlRunRecord.finishRefresh();
                    }
                });
            }
        });

        srlRunRecord.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                queryRunInfoVo.setPage(currentPage);
                apiMethodManager.queryUserRunInfo(queryRunInfoVo, new IRequestCallback<QueryUserRunInfoReturn>() {
                    @Override
                    public void onSuccess(QueryUserRunInfoReturn result) {
                        String code = result.getResult();
                        if ("01".equals(code)) {
                            mData.addAll(result.getPd());
                            adapter.notifyDataSetChanged();
                        }
                        srlRunRecord.finishLoadMore();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        currentPage--;
                        MyToast.showLong(RunRecordActivity.this, "数据加载失败：" + throwable.getMessage());
                        srlRunRecord.finishLoadMore();
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        apiMethodManager.queryUserRunInfo(queryRunInfoVo, new IRequestCallback<QueryUserRunInfoReturn>() {
            @Override
            public void onSuccess(QueryUserRunInfoReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    mData.clear();
                    mData.addAll(result.getPd());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(RunRecordActivity.this, "数据加载失败：" + throwable.getMessage());
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, RunRecordActivity.class);
        activity.startActivity(intent);
    }

}