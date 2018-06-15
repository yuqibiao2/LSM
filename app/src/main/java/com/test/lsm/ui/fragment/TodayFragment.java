package com.test.lsm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.PushMsgAdapter;
import com.test.lsm.bean.PushMsgBean;
import com.test.lsm.bean.json.GetMsgListReturn;
import com.test.lsm.bean.json.PushExtra;
import com.test.lsm.db.bean.PushMsg;
import com.test.lsm.db.service.inter.IPushMsgService;
import com.test.lsm.db.service.PushMsgService;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.MsgDetailActivity;
import com.test.lsm.utils.LoginRegUtils;
import com.today.step.lib.SportStepJsonUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：今天的动态
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class TodayFragment extends LsmBaseFragment {

    private static final String TAG = "TodayFragment";

    @BindView(R.id.rv_today)
    RecyclerView rvToday;
    @BindView(R.id.srl_today)
    SmartRefreshLayout srlToday;
    private PushMsgAdapter pushMsgAdapter;
    private MyApplication application;
    private LinkedList<GetMsgListReturn.PdBean> mData = new LinkedList<>();
    private APIMethodManager apiMethodManager;

    private int page = 1;

    private int pageSize = 10;
    private Gson mGson;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        mGson = new Gson();
        application = (MyApplication) getActivity().getApplication();
        apiMethodManager = APIMethodManager.getInstance();

    }

    @Override
    protected void initView() {
        pushMsgAdapter = new PushMsgAdapter(getContext(), R.layout.rv_item_today, mData);
        rvToday.setLayoutManager(new LinearLayoutManager(getContext()));
        rvToday.setAdapter(pushMsgAdapter);
    }

    @Override
    protected void initListener() {

        srlToday.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                mData.clear();
                PushMsgAdapter.tipHolder.clear();
                initData();
            }
        });

        srlToday.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        pushMsgAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, "ttttt");
                bundle.putString(JPushInterface.EXTRA_ALERT, mData.get(position).getID());
                PushExtra extra = new PushExtra();
                extra.setMsgId(mData.get(position).getID());
                bundle.putString( JPushInterface.EXTRA_EXTRA, mGson.toJson(extra));
                MsgDetailActivity.startAction(getContext(), bundle);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        if (!LoginRegUtils.isLogin(getActivity())) {
            return;
        }
        int userId = application.getUser().getUSER_ID();
        apiMethodManager.getMsgList(userId, page, pageSize, new IRequestCallback<GetMsgListReturn>() {
            @Override
            public void onSuccess(GetMsgListReturn result) {
                List<GetMsgListReturn.PdBean> pd = result.getPd();
                if (pd==null|| pd.size()==0){
                    MyToast.showLong(getActivity() , "没有更多数据了！");
                }else {
                    pushMsgAdapter.addData(pd);
                    page++;
                }
                srlToday.finishRefresh();
                srlToday.finishLoadMore();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "getMsgList：" + throwable.getMessage());
                srlToday.finishRefresh();
                srlToday.finishLoadMore();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
