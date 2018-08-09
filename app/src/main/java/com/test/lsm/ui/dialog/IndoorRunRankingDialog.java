package com.test.lsm.ui.dialog;

import android.content.Context;
import android.view.WindowManager;

import com.test.lsm.R;
import com.yyyu.baselibrary.template.BaseDialog;
import com.yyyu.baselibrary.utils.DimensChange;

/**
 * 功能：室内运动结束后排名弹窗
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/7
 */
public class IndoorRunRankingDialog extends LsmBaseDialog {

    public IndoorRunRankingDialog(Context context) {
        super(context);
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = DimensChange.dp2px(mContext , 350);
        lp.dimAmount = 0f;
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_indoor_run_ranking;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }
}
