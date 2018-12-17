package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.RankingAdapter;
import com.test.lsm.adapter.TrophyAdapter;
import com.test.lsm.bean.json.QueryActivityGoodsReturn;
import com.test.lsm.bean.json.QueryAmongReturn;
import com.test.lsm.bean.json.QueryUserRakingReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.vp_ranking)
    ViewPager vpRanking;
    @BindView(R.id.ll_pre_user)
    LinearLayout llPreUser;
    @BindView(R.id.tv_pre_ranking)
    TextView tvPreRanking;
    @BindView(R.id.riv_pre_user_icon)
    RoundImageView rivPreUserIcon;
    @BindView(R.id.tv_pre_username)
    TextView tvPreUsername;
    @BindView(R.id.tv_pre_score)
    TextView tvPreScore;
    @BindView(R.id.tv_user_raking)
    TextView tvUserRaking;
    @BindView(R.id.riv_user_icon)
    RoundImageView rivUserIcon;
    @BindView(R.id.tv_user_score)
    TextView tvUserScore;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_arrow_up)
    ImageView ivArrowUp;
    @BindView(R.id.iv_arrow_down)
    ImageView ivArrowDown;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;

    @Override
    public int getLayoutId() {

        return R.layout.activity_exercise_ranking;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        MyApplication application = (MyApplication) getApplication();
        user = application.getUser();
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpTrophy.setCurrentItem(vpTrophy.getCurrentItem() - 1, true);
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpTrophy.setCurrentItem(vpTrophy.getCurrentItem() + 1, true);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        showLoadDialog();
        apiMethodManager.queryActivityGoods(provider, new IRequestCallback<QueryActivityGoodsReturn>() {
            @Override
            public void onSuccess(QueryActivityGoodsReturn result) {
                dismissLoadDialog();
                String code = result.getResult();
                if ("01".equals(code)) {
                    List<QueryActivityGoodsReturn.PdBean.PdBeanItem> pdBeanItems = new ArrayList<>();
                    pdBeanItems.add(result.getPd().getCurrentPd());
                    pdBeanItems.add(result.getPd().getNextPd());
                    vpTrophy.setAdapter(new TrophyAdapter(ExerciseRankingActivity.this, pdBeanItems));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoadDialog();
            }
        });

        String currentDate = MyTimeUtils.formatDateTime("yyyy-MM-dd", new Date(System.currentTimeMillis()));
        apiMethodManager.queryAmongByDate(provider, currentDate, new IRequestCallback<QueryAmongReturn>() {
            @Override
            public void onSuccess(QueryAmongReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    List<QueryAmongReturn.PdBean.PdBeanItem> pdBeanList = new ArrayList<>();
                    QueryAmongReturn.PdBean.PdBeanItem onePd = result.getPd().getOnePd();
                    if (onePd!=null && onePd.getUSER_ID()!=0){
                        pdBeanList.add(onePd);
                    }
                    QueryAmongReturn.PdBean.PdBeanItem twoPd = result.getPd().getTwoPd();
                    if (twoPd!=null && twoPd.getUSER_ID()!=0){
                        pdBeanList.add(twoPd);
                    }
                    vpRanking.setAdapter(new RankingAdapter(ExerciseRankingActivity.this, pdBeanList));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

        apiMethodManager.queryUserRankingByDate(provider, user.getUSER_ID(), currentDate, new IRequestCallback<QueryUserRakingReturn>() {
            @Override
            public void onSuccess(QueryUserRakingReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    QueryUserRakingReturn.PdBean.CurrentPdBean currentPd = result.getPd().getCurrentPd();
                    int arrow = result.getPd().getArrow();
                    if (arrow == 0) {//退步
                        ivArrowDown.setVisibility(View.VISIBLE);
                    } else if (arrow == 1) {//进步
                        ivArrowDown.setVisibility(View.VISIBLE);
                    } else {
                    }
                    if (currentPd == null || currentPd.getUSER_ID() == 0) {

                    } else {
                        tvUserRaking.setText("" + currentPd.getUSER_SORT());
                        GlidUtils.load(ExerciseRankingActivity.this, rivUserIcon, currentPd.getUSER_IMAGE());
                        tvUserScore.setText("" + currentPd.getTOTAL_VALUE());
                        tvUsername.setText("" + currentPd.getUSERNAME());
                    }
                    QueryUserRakingReturn.PdBean.PrePdBean prePd = result.getPd().getPrePd();
                    if (prePd == null || prePd.getUSER_ID() == 0) {
                        llPreUser.setVisibility(View.GONE);
                    } else {
                        tvPreRanking.setText("" + prePd.getUSER_SORT());
                        GlidUtils.load(ExerciseRankingActivity.this, rivPreUserIcon, prePd.getUSER_IMAGE());
                        tvPreUsername.setText("" + prePd.getUSERNAME());
                        tvPreScore.setText("" + prePd.getTOTAL_VALUE());
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ExerciseRankingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
