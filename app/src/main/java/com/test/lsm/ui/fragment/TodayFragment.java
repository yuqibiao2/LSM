package com.test.lsm.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baidu.platform.comapi.map.N;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.PushMsgAdapter;
import com.test.lsm.bean.PushMsgBean;
import com.test.lsm.db.bean.PushMsg;
import com.test.lsm.db.service.IPushMsgService;
import com.test.lsm.db.service.PushMsgService;
import com.today.step.lib.SportStepJsonUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
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
    private IPushMsgService pushMsgService;
    private LinkedList<PushMsg> mData = new LinkedList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getActivity().getApplication();
        EventBus.getDefault().register(this);
        pushMsgService = new PushMsgService();
        List<PushMsg> allMsg = pushMsgService.getAllMsg();
        for (PushMsg pushMsg : allMsg) {
            mData.add(pushMsg);
        }
    }

    @Override
    protected void initView() {
        pushMsgAdapter = new PushMsgAdapter(getContext(), R.layout.rv_item_today_test, mData);
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

    @Override
    protected void initData() {
        super.initData();
        addMsgOnce(R.mipmap.msg1);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onPushMsg(PushMsgBean pushMsgBean) {
        int heartNum = application.getHeartNum();
        int stepNum = application.getStepNum();
        double stepDistance = application.getStepDistance();
        double calories = Double.parseDouble(SportStepJsonUtils.getCalorieByStep(stepNum));
        if (heartNum>150){
            addMsgDayByDay(R.mipmap.msg7);
        }
        if (heartNum>180){
            addMsgDayByDay(R.mipmap.msg5);
        }
        if (calories>200){
            addMsgDayByDay(R.mipmap.msg9);
        }
        if (stepNum>140 && heartNum>120){
            addMsgDayByDay(R.mipmap.msg3);
        }
        if (stepDistance >= 30) {//行走距离大于30KM
            addMsgDayByDay(R.mipmap.msg11);
        }
        if (stepDistance >= 10) {//行走距离大于10KM（一天推送一次）
            addMsgDayByDay(R.mipmap.msg4);
        }
        //时间判断
        String currentDate = MyTimeUtils.formatDateTime(new Date(System.currentTimeMillis()));
     /*   if (MyTimeUtils.timeCompare("2018-04-02 06:00:00", currentDate) < 0) {
            addMsgOnce(R.mipmap.msg13);
        }*/
        if (MyTimeUtils.timeCompare("2018-05-02 06:00:00", currentDate) < 0) {
            addMsgOnce(R.mipmap.msg13);
        }
        if (MyTimeUtils.timeCompare("2018-05-02 14:00:00", currentDate) < 0) {
            addMsgOnce(R.mipmap.msg14);
        }
        if (MyTimeUtils.timeCompare("2018-05-03 10:00:00", currentDate) < 0) {
            addMsgOnce(R.mipmap.msg15);
        }
        if (MyTimeUtils.timeCompare("2018-05-04 10:00:00", currentDate) < 0) {
            addMsgOnce(R.mipmap.msg16);
        }
        if (MyTimeUtils.timeCompare("2018-05-05 10:00:00", currentDate) < 0) {
            addMsgOnce(R.mipmap.msg17);
        }

    }

    private void addMsgDayByDay(int imgId) {
        PushMsg lastMsg = pushMsgService.getLastMsgByImgId(imgId);
        if (lastMsg != null) {
            String lastMsgDateStr= lastMsg.getDatetime();
            Date lastMsgDate = MyTimeUtils.parseDate(lastMsgDateStr);
            lastMsgDateStr = MyTimeUtils.formatDateTime("yyyy-MM-dd", lastMsgDate);
            Date date = new Date(System.currentTimeMillis());
            String currentDateTime = MyTimeUtils.formatDateTime("yyyy-MM-dd", date);
            //---比较是否为同一天
            if(!MyTimeUtils.isSameDay(lastMsgDateStr , currentDateTime)){
                addMsg(imgId);
            }
            //MyLog.e(TAG, "lastMsgDatetime：" + lastMsgDateStr);
            //MyLog.e(TAG, "currentDateTime：" + currentDateTime);
        }else{
            addMsg(imgId);
        }
    }

    private void addMsgOnce(int imgId) {
        if (!pushMsgService.isMsgExists(imgId)) {
            addMsg(imgId);
        }
    }

    private void addMsg(int imgId) {
        PushMsg pushMsg = new PushMsg();
        pushMsg.setImgId(imgId);
        pushMsg.setDatetime(MyTimeUtils.formatDateTime(new Date(System.currentTimeMillis())));
        pushMsgService.insertMsg(pushMsg);
        mData.addFirst(pushMsg);
        pushMsgAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
