package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.form.UserJoinCourseVo;
import com.test.lsm.bean.json.GetHRVInfoReturn;
import com.test.lsm.bean.json.UserJoinCourseReturn;
import com.test.lsm.global.SpConstant;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：训练难度选择
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class ExeIntensiveChoiceActivity extends LsmBaseActivity {

    private static final String TAG = "ExeIntensiveChoiceActiv";

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_physical)
    TextView tvPhysical;
    @BindView(R.id.iv_physical)
    ImageView ivPhysical;
    @BindView(R.id.iv_easy)
    ImageView ivEasy;
    @BindView(R.id.iv_easy_selected)
    ImageView ivEasySelected;
    @BindView(R.id.rl_easy)
    RelativeLayout rlEasy;
    @BindView(R.id.iv_normal)
    ImageView ivNormal;
    @BindView(R.id.iv_normal_selected)
    ImageView ivNormalSelected;
    @BindView(R.id.rl_normal)
    RelativeLayout rlNormal;
    @BindView(R.id.iv_hard)
    ImageView ivHard;
    @BindView(R.id.iv_hard_selected)
    ImageView ivHardSelected;
    @BindView(R.id.rl_hard)
    RelativeLayout rlHard;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.iv_suggestion_easy)
    ImageView ivSuggestionEasy;
    @BindView(R.id.iv_suggestion_normal)
    ImageView ivSuggestionNormal;
    @BindView(R.id.iv_suggestion_hard)
    ImageView ivSuggestionHard;
    private String imgUrl;
    private Integer bodyFitness = 0;
    private Integer courseLevel = 1; // 0：简单 1：一般  2：困难
    private String courseName;
    private String courseType;
    private APIMethodManager apiMethodManager;
    private int userId;
    private int ccType;
    private int coachId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exe_intensive_choice;
    }

    @Override
    protected boolean setDefaultStatusBarCompat() {
        StatusBarCompat.setTrans(this);
        return false;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
        courseName = intent.getStringExtra("courseName");
        courseType = intent.getStringExtra("courseType");
        coachId = intent.getIntExtra("coachId", -1);
        ccType = intent.getIntExtra("ccType", -1);
        String hrvIndexBeanJsonStr = (String) MySPUtils.get(this, SpConstant.HRV_INFO, "");
        if (!TextUtils.isEmpty(hrvIndexBeanJsonStr)) {
            GetHRVInfoReturn.HRVIndexBean hrvIndexBean = mGson.fromJson(hrvIndexBeanJsonStr, GetHRVInfoReturn.HRVIndexBean.class);
            bodyFitness =Integer.parseInt( hrvIndexBean.getBodyFitness());
        }
        apiMethodManager = APIMethodManager.getInstance();
        MyApplication application = (MyApplication) getApplication();
        userId = application.getUser().getUSER_ID();
    }

    @Override
    protected void initView() {
        ivEasy.setImageResource(R.mipmap.ic_easy);
        ivEasySelected.setVisibility(View.GONE);
        ivNormal.setImageResource(R.mipmap.ic_normal);
        ivNormalSelected.setVisibility(View.GONE);
        ivHard.setImageResource(R.mipmap.ic_hard);
        ivHardSelected.setVisibility(View.GONE);

        GlidUtils.load(this, ivIcon, imgUrl);
        tvCourseName.setText("# " + courseName);

        if (bodyFitness >= 30) {
            ivSuggestionHard.setVisibility(View.VISIBLE);
            courseLevel = 2;
            ivHard.setImageResource(R.mipmap.ic_hard_selected);
            ivHardSelected.setVisibility(View.VISIBLE);

            chgStatus1(ivPhysical, 5);
            tvPhysical.setText("过度暴动");
        } else if (bodyFitness >= 10) {

            ivSuggestionNormal.setVisibility(View.VISIBLE);
            courseLevel = 1;
            ivNormal.setImageResource(R.mipmap.ic_normal_selected);
            ivNormalSelected.setVisibility(View.VISIBLE);

            chgStatus1(ivPhysical, 4);
            tvPhysical.setText("拼劲十足");
        } else if (bodyFitness >= -10) {
            ivSuggestionNormal.setVisibility(View.VISIBLE);
            courseLevel = 1;
            ivNormal.setImageResource(R.mipmap.ic_normal_selected);
            ivNormalSelected.setVisibility(View.VISIBLE);

            chgStatus1(ivPhysical, 3);
            tvPhysical.setText("正常范围");
        } else if (bodyFitness >= -30) {
            ivSuggestionHard.setVisibility(View.VISIBLE);
            courseLevel = 0;
            ivEasy.setImageResource(R.mipmap.ic_easy_selected);
            ivEasySelected.setVisibility(View.VISIBLE);

            chgStatus1(ivPhysical, 2);
            tvPhysical.setText("疲劳");
        } else if (bodyFitness >= -50) {
            ivSuggestionHard.setVisibility(View.VISIBLE);
            courseLevel = 0;
            ivEasy.setImageResource(R.mipmap.ic_easy_selected);
            ivEasySelected.setVisibility(View.VISIBLE);

            chgStatus1(ivPhysical, 1);
            tvPhysical.setText("体力透支");
        } else {
            ivSuggestionHard.setVisibility(View.VISIBLE);
            courseLevel = 0;
            ivEasy.setImageResource(R.mipmap.ic_easy_selected);
            ivEasySelected.setVisibility(View.VISIBLE);

            chgStatus1(ivPhysical, 1);
            tvPhysical.setText("体力透支");
        }

    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.rl_easy, R.id.rl_normal, R.id.rl_hard})
    public void chgIntensive(View view) {
        ivEasy.setImageResource(R.mipmap.ic_easy);
        ivEasySelected.setVisibility(View.GONE);
        ivNormal.setImageResource(R.mipmap.ic_normal);
        ivNormalSelected.setVisibility(View.GONE);
        ivHard.setImageResource(R.mipmap.ic_hard);
        ivHardSelected.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.rl_easy:
                courseLevel = 0;
                ivEasy.setImageResource(R.mipmap.ic_easy_selected);
                ivEasySelected.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_normal:
                courseLevel = 1;
                ivNormal.setImageResource(R.mipmap.ic_normal_selected);
                ivNormalSelected.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_hard:
                courseLevel = 2;
                ivHard.setImageResource(R.mipmap.ic_hard_selected);
                ivHardSelected.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void toNext(View view) {
        showLoadDialog();
        UserJoinCourseVo userJoinCourseVo = new UserJoinCourseVo();
        userJoinCourseVo.setUSER_ID(userId);
        userJoinCourseVo.setCOURSE_LEVEL(courseLevel);
        userJoinCourseVo.setCOACH_ID(coachId);
        userJoinCourseVo.setCC_TYPE(ccType);
        userJoinCourseVo.setCOURSE_TYPE(courseType);
        apiMethodManager.userJoinCourse(userJoinCourseVo, new IRequestCallback<UserJoinCourseReturn>() {
            @Override
            public void onSuccess(UserJoinCourseReturn result) {
                dismissLoadDialog();
                if ("01".equals(result.getResult())){
                    int ucId = result.getPd().getUC_ID();
                    IndoorExerciseActivity.startAction(ExeIntensiveChoiceActivity.this, courseLevel, courseType, courseName , ucId);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoadDialog();
                MyToast.showLong(getApplicationContext(), "网络异常");
            }
        });
    }

    public static void startAction(Context context,
                                   String imgUrl,
                                   String courseName,
                                   String courseType,
                                   Integer coachId,
                                   Integer ccType,
                                   Integer bodyFitness) {
        Intent intent = new Intent(context, ExeIntensiveChoiceActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("courseName", courseName);
        intent.putExtra("courseType", courseType);
        intent.putExtra("coachId", coachId);
        intent.putExtra("ccType", ccType);
        intent.putExtra("bodyFitness", bodyFitness);
        context.startActivity(intent);
    }
    public void chgStatus1(View view, int status) {
        switch (status) {
            case 0:
                view.setEnabled(false);
                break;
            case 1:
                view.setBackgroundResource(R.mipmap.ic_bar11);
                break;
            case 2:
                view.setBackgroundResource(R.mipmap.ic_bar12);
                break;
            case 3:
                view.setBackgroundResource(R.mipmap.ic_bar13);
                break;
            case 4:
                view.setBackgroundResource(R.mipmap.ic_bar14);
                break;
            case 5:
                view.setBackgroundResource(R.mipmap.ic_bar15);
                break;
        }
    }

}
