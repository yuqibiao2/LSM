package com.test.lsm.ui.dialog;

import android.content.Context;

import com.yyyu.baselibrary.template.BaseBottomDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/29
 */
public abstract class LsmBaseBottomDialog extends BaseBottomDialog{

    //private Unbinder mUnbind;

    public LsmBaseBottomDialog(Context context) {
        super(context);
    }

    public LsmBaseBottomDialog(Context context, int peekHeight, int maxHeight) {
        super(context, peekHeight, maxHeight);
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        //mUnbind = ButterKnife.bind(this, bottomView);
    }



}
