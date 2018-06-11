package com.yyyu.baselibrary.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yyyu.baselibrary.ui.widget.BottomSheetDialogWrapper;
import com.yyyu.baselibrary.utils.WindowUtils;

/**
 * 功能：底部弹出Dialog
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/3/23
 */

public abstract class BaseBottomDialog {

    protected Context mContext;
    protected final BottomSheetDialogWrapper bottomSheetDialog;
    protected final View bottomView;

    public BaseBottomDialog(Context context ){
        this(context ,WindowUtils.getSize(context)[1] / 2, WindowUtils.getSize(context)[1]);
    }

    public BaseBottomDialog(Context context, int peekHeight, int maxHeight) {
        this.mContext = context;
        bottomSheetDialog = new BottomSheetDialogWrapper(mContext, peekHeight, maxHeight){
            @Override
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
            }
        };
        bottomView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WindowUtils.getSize(mContext)[0], maxHeight);
        bottomView.setLayoutParams(lp);
        bottomSheetDialog.setContentView(bottomView);
        init();
    }

    private void init() {
        beforeInit();
        initView();
        initListener();
        initData();
        afterInit();
    }


    public abstract int getLayoutId();

    protected void beforeInit() {

    }

    protected abstract void initView();

    protected void initListener() {

    }

    protected void initData() {

    }

    protected void afterInit() {

    }

    protected <T extends  View> T getView(int resId){

        return (T) bottomView.findViewById(resId);
    }


    public void show() {
        bottomSheetDialog.show();
    }

    public View getBottomView() {
        return bottomView;
    }
}