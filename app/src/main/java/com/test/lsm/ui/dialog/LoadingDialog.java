package com.test.lsm.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.lsm.R;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.WindowUtils;

/**
 * 功能：加载框
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/4
 */
public class LoadingDialog extends LsmBaseDialog {


    public LoadingDialog(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);//默认点击外面可消失
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = DimensChange.dp2px(getContext(), 100);
        lp.height = DimensChange.dp2px(getContext(), 100);
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public void show(String tipStr) {
        setTip(tipStr);
        show();
    }

    private void setTip(String tipStr) {
        TextView tv_tip = getView(R.id.tv_tip);
        tv_tip.setText(tipStr);
        tv_tip.setVisibility(View.VISIBLE);
    }

}
