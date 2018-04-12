package com.test.lsm.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.R;
import com.test.lsm.adapter.PushMsgAdapter;
import com.test.lsm.bean.PushMsgBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：今天的动态
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class TodayFragment extends LsmBaseFragment {

    @BindView(R.id.rv_today)
    RecyclerView rvToday;
    @BindView(R.id.srl_today)
    SmartRefreshLayout srlToday;
    private List<PushMsgBean> msgBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        msgBeanList = new ArrayList<>();
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/01.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/02.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/03.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/04.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/05.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/06.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/07.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/08.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/09.png" ,"" , "今天"));
        msgBeanList.add(new PushMsgBean("https://raw.githubusercontent.com/yuqibiao2/LSM/master/img/10.png" ,"" , "今天"));
    }

    @Override
    protected void initView() {
        PushMsgAdapter pushMsgAdapter = new PushMsgAdapter(getContext() ,R.layout.rv_item_today_test ,msgBeanList);
        rvToday.setLayoutManager(new LinearLayoutManager(getContext()));
        rvToday.setAdapter(pushMsgAdapter);
    }

    @Override
    protected void initListener() {

        srlToday.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                srlToday.finishRefresh(2000);
            }
        });

        srlToday.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                srlToday.finishLoadMore(2000);
            }
        });

    }
}
