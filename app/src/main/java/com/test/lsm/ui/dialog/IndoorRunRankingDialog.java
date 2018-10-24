package com.test.lsm.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.ModifyScoreReturn;
import com.test.lsm.bean.json.QueryUserRakingReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.LsmBaseActivity;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.WindowUtils;

import java.util.Date;

import butterknife.BindView;

/**
 * 功能：室内运动结束后排名弹窗
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/7
 */
public class IndoorRunRankingDialog extends LsmBaseDialog {

    @BindView(R.id.tv_scoring)
    TextView tvScoring;
    @BindView(R.id.tv_raking)
    TextView tvRaking;
    @BindView(R.id.tv_total_score)
    TextView tvTotalScore;
    @BindView(R.id.iv_arrow_up)
    ImageView ivArrowUp;
    @BindView(R.id.iv_arrow_down)
    ImageView ivArrowDown;
    @BindView(R.id.tv_calorie_burned)
    TextView tvCalorieBurned;


    private Integer mCurrentScore;
    private Integer usId;
    private double calorieBurned;

    public IndoorRunRankingDialog(Context context, Integer currentScore, Integer usId, double calorieBurned) {
        super(context);
        this.mCurrentScore = currentScore;
        this.usId = usId;
        this.calorieBurned = calorieBurned;
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        int width = WindowUtils.getSize(mContext)[0];
        lp.width = width * 4 / 5;
        lp.dimAmount = 0f;
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_indoor_run_ranking;
    }

    @Override
    protected void initView() {
        if (mCurrentScore >= 100) {
            tvScoring.setTextSize(100);
        } else if (mCurrentScore >= 1000) {
            tvScoring.setTextSize(80);
        }
        tvScoring.setText("" + mCurrentScore);
        final String calorieStr = String.format("%.1f", calorieBurned);
        tvCalorieBurned.setText(calorieStr+"大卡");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        final LsmBaseActivity act = (LsmBaseActivity) this.mContext;
        final MyApplication application = (MyApplication) act.getApplication();
        final String currentDate = MyTimeUtils.formatDateTime("yyyy-MM-dd", new Date(System.currentTimeMillis()));

        APIMethodManager.getInstance().modifyUserScoreByMin(act.provider,
                "" + usId,
                "" + mCurrentScore,
                new IRequestCallback<ModifyScoreReturn>() {
                    @Override
                    public void onSuccess(ModifyScoreReturn result) {

                        APIMethodManager.getInstance().queryUserRankingByDate(act.provider, application.getUser().getUSER_ID(), currentDate, new IRequestCallback<QueryUserRakingReturn>() {
                            @Override
                            public void onSuccess(QueryUserRakingReturn result) {
                                String code = result.getResult();
                                if ("01".equals(code)) {
                                    QueryUserRakingReturn.PdBean.CurrentPdBean currentPd = result.getPd().getCurrentPd();
                                    int arrow = result.getPd().getArrow();
                                    if (arrow == 0) {//退步
                                        ivArrowUp.setVisibility(View.VISIBLE);
                                    } else if (arrow == 1) {//进步
                                        ivArrowDown.setVisibility(View.VISIBLE);
                                    } else {
                                        ivArrowUp.setVisibility(View.VISIBLE);
                                    }
                                    if (currentPd != null) {
                                        int ranking = currentPd.getUSER_SORT();
                                        tvRaking.setText("" + ranking);
                                        tvTotalScore.setText("" + currentPd.getTOTAL_VALUE());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });

    }
}
