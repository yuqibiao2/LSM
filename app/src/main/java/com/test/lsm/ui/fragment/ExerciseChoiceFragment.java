package com.test.lsm.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.DownLineCourseAdapter;
import com.test.lsm.adapter.OnLineCourseAdapter;
import com.test.lsm.bean.json.GetCoachByCourseType;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.ExeIntensiveChoiceActivity;

import java.util.ArrayList;
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

    @Override
    protected void initListener() {
        onLineCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExeIntensiveChoiceActivity.startAction(getActivity(),
                        onLineData.get(position).getCOURSE_IMG(),
                        onLineData.get(position).getNAME(),
                        onLineData.get(position).getCOURSE_TYPE(),
                        0);
            }
        });
        downLineCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExeIntensiveChoiceActivity.startAction(getActivity(),
                        downLineData.get(position).getCOURSE_IMG(),
                        downLineData.get(position).getNAME(),
                        downLineData.get(position).getCOURSE_TYPE(),
                        0);
            }
        });
    }

}
