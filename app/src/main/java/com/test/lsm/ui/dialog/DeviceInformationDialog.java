package com.test.lsm.ui.dialog;

import android.content.Context;
import android.view.WindowManager;

import com.test.lsm.R;
import com.yyyu.baselibrary.utils.WindowUtils;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/27
 */

public class DeviceInformationDialog extends LsmBaseDialog{


    public DeviceInformationDialog(Context context) {
        super(context);
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = WindowUtils.getSize(mContext)[0] / 3*2;
        lp.height =  WindowUtils.getSize(mContext)[1]/2;
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_device_information;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }
}
