package com.test.lsm.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.QueryUserRakingReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.LsmBaseActivity;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyTimeUtils;

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


    private Integer mCurrentScore;

    public IndoorRunRankingDialog(Context context, Integer currentScore) {
        super(context);
        this.mCurrentScore = currentScore;
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = DimensChange.dp2px(mContext, 350);
        lp.dimAmount = 0f;
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_indoor_run_ranking;
    }

    @Override
    protected void initView() {
        if (mCurrentScore>=100){
            tvScoring.setTextSize(100);
        }else if (mCurrentScore>=1000){
            tvScoring.setTextSize(80);
        }
        tvScoring.setText("" + mCurrentScore);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        LsmBaseActivity act = (LsmBaseActivity) this.mContext;
        MyApplication application = (MyApplication) act.getApplication();
        String currentDate = MyTimeUtils.formatDateTime("yyyy-MM-dd", new Date(System.currentTimeMillis()));
        APIMethodManager.getInstance().queryUserRankingByDate(act.provider, application.getUser().getUSER_ID(), currentDate, new IRequestCallback<QueryUserRakingReturn>() {
            @Override
            public void onSuccess(QueryUserRakingReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    if (result.getPd() != null) {
                        int ranking = result.getPd().getRownum();
                        tvRaking.setText("" + ranking);
                        tvTotalScore.setText("" + result.getPd().getTOTAL_VALUE());
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
