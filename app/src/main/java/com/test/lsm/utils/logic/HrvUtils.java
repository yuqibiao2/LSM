package com.test.lsm.utils.logic;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.R;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/29
 */
public class HrvUtils {

    /**
     * stressTension
     *
     * @param context
     * @param stressTension
     * @param ivIcon
     * @param tvTip
     */
    public static void inflateStressTension(Context context, int stressTension, ImageView ivIcon, TextView tvTip){
        if (stressTension <= -30) {//过渡低落
            chgStatus1(ivIcon, 1);
            tvTip.setText(context.getResources().getString(R.string.stress_level_1));
        } else if (stressTension <= -10) {//低落
            chgStatus1(ivIcon, 2);
            tvTip.setText(context.getResources().getString(R.string.stress_level_2));
        } else if (stressTension <= 10) {//良好
            chgStatus1(ivIcon, 3);
            tvTip.setText(context.getResources().getString(R.string.stress_level_3));
        } else if (stressTension <= 30) {//兴奋
            chgStatus1(ivIcon, 4);
            tvTip.setText(context.getResources().getString(R.string.stress_level_4));
        } else if (stressTension <= 50) {//过渡兴奋
            chgStatus1(ivIcon, 5);
            tvTip.setText(context.getResources().getString(R.string.stress_level_5));
        } else {
            chgStatus2(ivIcon, 5);
            tvTip.setText(context.getResources().getString(R.string.stress_level_5));
        }
    }


    /**
     * bodyFatigue
     *
     * @param context
     * @param bodyFatigue
     * @param ivIcon
     * @param tvTip
     */
    public static void inflateBodyFatigue(Context context, int bodyFatigue, ImageView ivIcon, TextView tvTip){
        if (bodyFatigue <= 20) {//过渡低落
            chgStatus2(ivIcon, 1);
            tvTip.setText(context.getResources().getString(R.string.fatigue_level1));
        } else if (bodyFatigue <= 40) {//低落
            chgStatus2(ivIcon, 2);
            tvTip.setText(context.getResources().getString(R.string.fatigue_level2));
        } else if (bodyFatigue <= 60) {//良好
            tvTip.setText(context.getResources().getString(R.string.fatigue_level3));
            chgStatus2(ivIcon, 3);
        } else if (bodyFatigue <= 80) {//兴奋
            chgStatus2(ivIcon, 4);
            tvTip.setText(context.getResources().getString(R.string.fatigue_level4));
        } else if (bodyFatigue <= 100) {//过渡兴奋
            chgStatus2(ivIcon, 5);
            tvTip.setText(context.getResources().getString(R.string.fatigue_level5));
        } else {
            chgStatus2(ivIcon, 5);
            tvTip.setText(context.getResources().getString(R.string.fatigue_level5));
        }
    }

    /**
     * moodStability
     *
     * @param context
     * @param moodStability
     * @param ivIcon
     * @param tvTip
     */
    public static  void inflateMoodStability(Context context, int moodStability, ImageView ivIcon, TextView tvTip){
        if (moodStability <= -30) {//过渡松散
            tvTip.setText(context.getResources().getString(R.string.mood_level1));
            chgStatus1(ivIcon, 1);
        } else if (moodStability <= -10) {//松散
            tvTip.setText(context.getResources().getString(R.string.mood_level2));
            chgStatus1(ivIcon, 2);
        } else if (moodStability <= 10) {//正常
            tvTip.setText(context.getResources().getString(R.string.mood_level3));
            chgStatus1(ivIcon, 3);
        } else if (moodStability <= 30) {//紧张
            tvTip.setText(context.getResources().getString(R.string.mood_level4));
            chgStatus1(ivIcon, 4);
        } else if (moodStability <= 50) {//过渡紧张
            tvTip.setText(context.getResources().getString(R.string.mood_level5));
            chgStatus1(ivIcon, 5);
        } else {
            tvTip.setText(context.getResources().getString(R.string.mood_level5));
            chgStatus1(ivIcon, 5);
        }
    }


    /**
     * mindFitness
     *
     * @param context
     * @param mindFitness
     * @param ivIcon
     * @param tvTip
     */
    public static void inflateMindFitness(Context context, int mindFitness, ImageView ivIcon, TextView tvTip){
        if (mindFitness >= 30) {
            chgStatus1(ivIcon, 5);
            tvTip.setText(context.getResources().getString(R.string.fitness_level5));
        } else if (mindFitness >= 10) {
            chgStatus1(ivIcon, 4);
            tvTip.setText(context.getResources().getString(R.string.fitness_level4));
        } else if (mindFitness >= -10) {
            chgStatus1(ivIcon, 3);
            tvTip.setText(context.getResources().getString(R.string.fitness_level3));
        } else if (mindFitness >= -30) {
            chgStatus1(ivIcon, 2);
            tvTip.setText(context.getResources().getString(R.string.fitness_level2));
        } else if (mindFitness >= -50) {
            chgStatus1(ivIcon, 1);
            tvTip.setText(context.getResources().getString(R.string.fitness_level1));
        } else {
            chgStatus1(ivIcon, 1);
            tvTip.setText(context.getResources().getString(R.string.fitness_level1));
        }
    }

    /**
     * bodyFitness
     *
     * @param context
     * @param bodyFitness
     * @param ivIcon
     * @param tvTip
     */
    public static void inflateBodyFitness(Context context, int bodyFitness, ImageView ivIcon, TextView tvTip) {
        if (bodyFitness >= 30) {
            chgStatus1(ivIcon, 5);
            tvTip.setText(context.getResources().getString(R.string.fitness_level5));
        } else if (bodyFitness >= 10) {
            chgStatus1(ivIcon, 4);
            tvTip.setText(context.getResources().getString(R.string.fitness_level4));
        } else if (bodyFitness >= -10) {
            chgStatus1(ivIcon, 3);
            tvTip.setText(context.getResources().getString(R.string.fitness_level3));
        } else if (bodyFitness >= -30) {
            chgStatus1(ivIcon, 2);
            tvTip.setText(context.getResources().getString(R.string.fitness_level2));
        } else if (bodyFitness >= -50) {
            chgStatus1(ivIcon, 1);
            tvTip.setText(context.getResources().getString(R.string.fitness_level1));
        } else {
            chgStatus1(ivIcon, 1);
            tvTip.setText(context.getResources().getString(R.string.fitness_level1));
        }
    }


    public static void chgStatus2(View view, int status) {
        switch (status) {
            case 0:
                view.setEnabled(false);
                break;
            case 1:
                view.setBackgroundResource(R.mipmap.ic_bar21);
                break;
            case 2:
                view.setBackgroundResource(R.mipmap.ic_bar22);
                break;
            case 3:
                view.setBackgroundResource(R.mipmap.ic_bar23);
                break;
            case 4:
                view.setBackgroundResource(R.mipmap.ic_bar24);
                break;
            case 5:
                view.setBackgroundResource(R.mipmap.ic_bar25);
                break;
        }
    }

    public static void chgStatus1(View view, int status) {
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
