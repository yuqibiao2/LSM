package com.test.lsm.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.PushMsgAdapter;
import com.test.lsm.bean.event.OnUserInfoChg;
import com.test.lsm.bean.event.RefreshTodayMsg;
import com.test.lsm.bean.json.GetMsgListReturn;
import com.test.lsm.bean.json.PushExtra;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.ExerciseRankingActivity;
import com.test.lsm.ui.activity.MsgDetailActivity;
import com.test.lsm.ui.activity.SettingActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
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
    @BindView(R.id.tv_datetime)
    TextView tvDatetime;
    @BindView(R.id.rv_user_icon)
    RoundImageView rvUserIcon;
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        String userImage = application.getUser().getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)){
            GlidUtils.load(getContext() , rvUserIcon , userImage);
        }
        pushMsgAdapter = new PushMsgAdapter(getContext(), R.layout.rv_item_today, mData);
        rvToday.setLayoutManager(new LinearLayoutManager(getContext()));
        rvToday.setAdapter(pushMsgAdapter);
        tvDatetime.setText("" + MyTimeUtils.formatDateTime("MM月dd日", new Date(System.currentTimeMillis()))
                + " " + MyTimeUtils.getCurrentWeek());
    }

    @Override
    protected void initListener() {

        rvUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SettingActivity.startAction(getActivity());
            }
        });

        srlToday.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                toRefreshData();
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

                String pushTitle = mData.get(position).getPUSH_TITLE();
                if ("心力挑戰名人賽".trim().equals(pushTitle)){//挑战赛
                    ExerciseRankingActivity.startAction(getActivity());
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, "ttttt");
                    bundle.putString(JPushInterface.EXTRA_ALERT, mData.get(position).getID());
                    PushExtra extra = new PushExtra();
                    extra.setMsgId(mData.get(position).getID());
                    bundle.putString(JPushInterface.EXTRA_EXTRA, mGson.toJson(extra));
                    MsgDetailActivity.startAction(getContext(), bundle);

                }

            }
        });

    }

    /**
     * 刷新消息
     */
    private void toRefreshData() {
        page = 1;
        mData.clear();
        PushMsgAdapter.tipHolder.clear();
        initData();
    }

    @Override
    protected void initData() {
        super.initData();
        if (!LoginRegUtils.isLogin(getActivity())) {
            return;
        }
        int userId = application.getUser().getUSER_ID();
        apiMethodManager.getMsgList(provider , userId, page, pageSize, new IRequestCallback<GetMsgListReturn>() {
            @Override
            public void onSuccess(GetMsgListReturn result) {
                List<GetMsgListReturn.PdBean> pd = result.getPd();
                if (pd == null || pd.size() == 0) {
                    MyToast.showLong(getActivity(), "没有更多数据了！");
                } else {
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()){
            //---统计
            FirebaseAnalytics.getInstance(getActivity())
                    .setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //---统计
        FirebaseAnalytics.getInstance(getActivity())
                .setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUserInfoChanged(OnUserInfoChg onUserInfoChg){
        String userImage = application.getUser().getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)){
            GlidUtils.load(getContext() , rvUserIcon , userImage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onRefreshData(RefreshTodayMsg refreshTodayMsg){
        toRefreshData();
    }


}
