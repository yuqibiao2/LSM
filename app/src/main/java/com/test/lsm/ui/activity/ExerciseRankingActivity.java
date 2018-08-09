package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.test.lsm.R;
import com.test.lsm.adapter.TrophyAdapter;

import butterknife.BindView;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/7
 */
public class ExerciseRankingActivity extends LsmBaseActivity {

    @BindView(R.id.vp_trophy)
    ViewPager vpTrophy;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;

    @Override
    public int getLayoutId() {

        return R.layout.activity_exercise_ranking;
    }

    @Override
    protected void initView() {
        vpTrophy.setAdapter(new TrophyAdapter(this));
    }

    @Override
    protected void initListener() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpTrophy.setCurrentItem(vpTrophy.getCurrentItem() + 1, true);
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpTrophy.setCurrentItem(vpTrophy.getCurrentItem() - 1, true);
            }
        });
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ExerciseRankingActivity.class);
        context.startActivity(intent);
    }

}
