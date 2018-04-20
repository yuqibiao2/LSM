package com.test.lsm.ui.dialog;

import android.content.Context;

import com.yyyu.baselibrary.template.BaseDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/27
 */

public abstract class LsmBaseDialog extends BaseDialog{

    private Unbinder mUnbind;

    public LsmBaseDialog(Context context) {
        super(context);
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        mUnbind = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbind.unbind();
    }
}
