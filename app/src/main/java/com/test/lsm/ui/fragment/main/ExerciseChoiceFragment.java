package com.test.lsm.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.DownLineCourseAdapter;
import com.test.lsm.adapter.OnLineCourseAdapter;
import com.test.lsm.bean.json.GetCoachByCourseType;
import com.test.lsm.bean.json.QueryUserRakingReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.global.Constant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.ExeIntensiveChoiceActivity;
import com.test.lsm.ui.activity.ExerciseRankingActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：室内运动选择
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class ExerciseChoiceFragment extends LsmBaseFragment {


    @BindView(R.id.tv_welcome)
    TextView tvWelcome;
    @BindView(R.id.rv_exercise_type)
    RecyclerView rvExerciseType;
    @BindView(R.id.rv_lesson)
    RecyclerView rvLesson;
    @BindView(R.id.ib_list)
    ImageButton ibList;
    @BindView(R.id.tv_raking)
    TextView tvRaking;
    @BindView(R.id.iv_arrow_up)
    ImageView ivArrowUp;
    @BindView(R.id.iv_arrow_down)
    ImageView ivArrowDown;
    @BindView(R.id.ll_ranking)
    LinearLayout llRanking;
    @BindView(R.id.srl_exercise)
    SmartRefreshLayout srlExercise;

    private UserLoginReturn.PdBean user;
    private OnLineCourseAdapter onLineCourseAdapter;
    private APIMethodManager apiMethodManager;
    private List<GetCoachByCourseType.ListBean> onLineData;
    List<GetCoachByCourseType.ListBean> downLineData;
    private DownLineCourseAdapter downLineCourseAdapter;

    @Override
    public int getLayoutId() {

        return R.layout.fragment_exerise_choice;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        MyApplication application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
        apiMethodManager = APIMethodManager.getInstance();
        onLineData = new ArrayList<>();
        downLineData = new ArrayList<>();
    }

    @Override
    protected void initView() {
        tvWelcome.setText("Welcome back, " + user.getUSERNAME() + ".");
        rvExerciseType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        onLineCourseAdapter = new OnLineCourseAdapter(R.layout.rv_item_online, onLineData);
        rvExerciseType.setAdapter(onLineCourseAdapter);

        rvLesson.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        downLineCourseAdapter = new DownLineCourseAdapter(R.layout.rv_item_offline, downLineData);
        rvLesson.setAdapter(downLineCourseAdapter);

        //---禁用下拉加载
        srlExercise.setEnableLoadMore(false);
    }

    @Override
    protected void initData() {
        super.initData();

        apiMethodManager.getCoachByCourseType(new IRequestCallback<GetCoachByCourseType>() {
            @Override
            public void onSuccess(GetCoachByCourseType result) {
                GetCoachByCourseType.PdBean pd = result.getPd();
                List<GetCoachByCourseType.ListBean> onLineList = pd.getOnLineList();
                onLineData.addAll(onLineList);
                onLineCourseAdapter.notifyDataSetChanged();

                List<GetCoachByCourseType.ListBean> downLineList = pd.getDownLineList();
                downLineData.addAll(downLineList);
                downLineCourseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private long enterTime;

    @Override
    public void onStart() {
        super.onStart();
        enterTime = System.currentTimeMillis() / 1000;
    }

    @Override
    public void onStop() {
        super.onStop();
        //--统计
        long stopTime = System.currentTimeMillis() / 1000;
        long duration = stopTime - enterTime;
        Bundle bundle = new Bundle();
        bundle.putString("during", "" + duration);
        FirebaseAnalytics.getInstance(getActivity()).logEvent("lsm01_enter_class_intensity", bundle);
    }


    @Override
    protected void initListener() {
        onLineCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExeIntensiveChoiceActivity.startAction(getActivity(),
                        onLineData.get(position).getCOURSE_IMG(),
                        onLineData.get(position).getNAME(),
                        onLineData.get(position).getCOURSE_TYPE(),
                        onLineData.get(position).getCOACH_ID(),
                        0,
                        Constant.lastedBodyFitness);

                //--统计
                long stopTime = System.currentTimeMillis() / 1000;
                long duration = stopTime - enterTime;
                Bundle bundle = new Bundle();
                bundle.putString("classId", "" + onLineData.get(position).getCOACH_ID());
                bundle.putString("during", "" + duration);
                FirebaseAnalytics.getInstance(getActivity()).logEvent("lsm01_leave_class_list", bundle);

            }
        });

        downLineCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExeIntensiveChoiceActivity.startAction(getActivity(),
                        downLineData.get(position).getCOURSE_IMG(),
                        downLineData.get(position).getNAME(),
                        downLineData.get(position).getCOURSE_TYPE(),
                        downLineData.get(position).getCOACH_ID(),
                        1,
                        Constant.lastedBodyFitness);
                //--统计
                long stopTime = System.currentTimeMillis() / 1000;
                long duration = stopTime - enterTime;
                Bundle bundle = new Bundle();
                bundle.putString("classId", "" + downLineData.get(position).getCOACH_ID());
                bundle.putString("during", "" + duration);
                FirebaseAnalytics.getInstance(getActivity()).logEvent("lsm01_leave_class_list", bundle);
            }
        });

        srlExercise.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
                updateRaking();
            }
        });

        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseRankingActivity.startAction(getActivity());
            }
        });

        llRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseRankingActivity.startAction(getActivity());
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            updateRaking();
            //---统计
            FirebaseAnalytics.getInstance(getActivity())
                    .setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
        }
    }

    private void updateRaking() {
        String currentDate = MyTimeUtils.formatDateTime("yyyy-MM-dd", new Date(System.currentTimeMillis()));
        apiMethodManager.queryUserRankingByDate(provider, user.getUSER_ID(), currentDate, new IRequestCallback<QueryUserRakingReturn>() {
            @Override
            public void onSuccess(QueryUserRakingReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    QueryUserRakingReturn.PdBean.CurrentPdBean currentPd = result.getPd().getCurrentPd();
                    int ranking = currentPd.getUSER_SORT();
                    tvRaking.setText("" + ranking);
                    int arrow = result.getPd().getArrow();
                    if (arrow == 0) {//退步
                        ivArrowDown.setVisibility(View.VISIBLE);
                    } else if (arrow == 1) {//进步
                        ivArrowDown.setVisibility(View.VISIBLE);
                    } else {
                    }
                }
                srlExercise.finishRefresh();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

}
