package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.lsm.R;
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
    private String imgUrl;
    private Integer bodyFitness;
    private Integer intensiveSelected = 1; // 0：简单 1：一般  2：困难

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
        bodyFitness = intent.getIntExtra("bodyFitness", 0);
    }

    @Override
    protected void initView() {

        Glide.with(this).load(imgUrl).into(ivIcon);

        if (bodyFitness >= 30) {
            chgStatus1(ivPhysical, 1);
            tvPhysical.setText("过度暴动");
        } else if (bodyFitness >= 10) {
            chgStatus1(ivPhysical, 2);
            tvPhysical.setText("拼劲十足");
        } else if (bodyFitness >= -10) {
            chgStatus1(ivPhysical, 3);
            tvPhysical.setText("正常范围");
        } else if (bodyFitness >= -30) {
            chgStatus1(ivPhysical, 4);
            tvPhysical.setText("疲劳");
        } else if (bodyFitness >= -50) {
            chgStatus1(ivPhysical, 5);
            tvPhysical.setText("体力透支");
        } else {
            chgStatus1(ivPhysical, 5);
            tvPhysical.setText("体力透支");
        }

    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.rl_easy , R.id.rl_normal , R.id.rl_hard})
    public void chgIntensive(View view){
        ivEasy.setImageResource(R.mipmap.ic_easy);
        ivEasySelected.setVisibility(View.GONE);
        ivNormal.setImageResource(R.mipmap.ic_normal);
        ivNormalSelected.setVisibility(View.GONE);
        ivHard.setImageResource(R.mipmap.ic_hard);
        ivHardSelected.setVisibility(View.GONE);
        switch (view.getId()){
            case R.id.rl_easy:
                intensiveSelected = 0;
                ivEasy.setImageResource(R.mipmap.ic_easy_selected);
                ivEasySelected.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_normal:
                intensiveSelected = 1;
                ivNormal.setImageResource(R.mipmap.ic_normal_selected);
                ivNormalSelected.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_hard:
                intensiveSelected = 2;
                ivHard.setImageResource(R.mipmap.ic_hard_selected);
                ivHardSelected.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void toNext(View view) {
        IndoorExerciseActivity.startAction(this , intensiveSelected);
    }

    public static void startAction(Context context, String imgUrl, Integer bodyFitness) {
        Intent intent = new Intent(context, ExeIntensiveChoiceActivity.class);
        intent.putExtra("imgUrl", imgUrl);
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
