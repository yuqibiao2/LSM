package com.test.lsm.ui.fragment;

import com.yyyu.baselibrary.template.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 功能：LSM对应的BaseFragment
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public abstract class LsmBaseFragment extends BaseFragment{

    private Unbinder mUnbind;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        mUnbind = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

}
