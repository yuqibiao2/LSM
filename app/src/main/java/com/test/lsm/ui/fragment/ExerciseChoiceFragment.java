package com.test.lsm.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.ExerciseAdapter;
import com.test.lsm.adapter.LessonAdapter;
import com.test.lsm.bean.FooBean;
import com.test.lsm.bean.json.UserLoginReturn;
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

    private List<FooBean> fooBeanList;
    private UserLoginReturn.PdBean user;
    private ExerciseAdapter exerciseAdapter;

    @Override
    public int getLayoutId() {

        return R.layout.fragment_exerise_choice;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        fooBeanList = new ArrayList<>();
        fooBeanList.add(new FooBean());
        fooBeanList.add(new FooBean());
        fooBeanList.add(new FooBean());
        fooBeanList.add(new FooBean());
        fooBeanList.add(new FooBean());
        fooBeanList.add(new FooBean());
        MyApplication application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
    }

    @Override
    protected void initView() {
        tvWelcome.setText("Welcome back, "+user.getUSERNAME()+".");
        rvExerciseType.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        rvLesson.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));
        exerciseAdapter = new ExerciseAdapter(R.layout.rv_item_exercise, fooBeanList);
        rvExerciseType.setAdapter(exerciseAdapter);
        rvLesson.setAdapter(new LessonAdapter(R.layout.rv_item_lesson, fooBeanList));
    }

    @Override
    protected void initListener() {
        exerciseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ExeIntensiveChoiceActivity.startAction(getActivity() ,
                        "http://g.hiphotos.baidu.com/image/h%3D300/sign=0236ec87e7f81a4c3932eac9e72b6029/2e2eb9389b504fc2db67eef6e9dde71190ef6d0c.jpg"
                        ,0);
            }
        });
    }

}
