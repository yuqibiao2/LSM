package com.test.lsm.ui.activity;

import com.yyyu.baselibrary.template.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 功能：LSM项目的BaseActivity
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public abstract class LsmBaseActivity extends BaseActivity{

    private Unbinder mUnbind;

    @Override
    public void beforeInit() {
        super.beforeInit();
        mUnbind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbind!=null){
            mUnbind.unbind();
        }
    }

}
