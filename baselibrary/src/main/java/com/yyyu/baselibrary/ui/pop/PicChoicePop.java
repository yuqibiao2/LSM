package com.yyyu.baselibrary.ui.pop;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;

import com.yyyu.baselibrary.R;
import com.yyyu.baselibrary.utils.MediaUtils;


/**
 * 功能：图片选择pop
 * <p/>
 * Created by yyyu on 2016/7/27.
 */
public class PicChoicePop extends BasePopupWindow {

    public PicChoicePop(Activity act, int width, int height, View popView) {
        super(act, width, height, popView);
        setAnimationStyle(R.style.pic_choice);
    }

    public PicChoicePop(Fragment fragment, int width, int height, View popView) {
        super(fragment, width, height, popView);
        setAnimationStyle(R.style.pic_choice);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.btn_to_camera,
                R.id.btn_to_photo, R.id.btn_to_cancel);
    }

    public void show(View popLocView) {
        showAtLocation(popLocView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_to_camera) {//拍照
            if (mFragment != null) {
                MediaUtils.toCamera(mFragment);
            } else {
                MediaUtils.toCamera(mAct);
            }
            dismiss();
        } else if (v.getId() == R.id.btn_to_photo) {//相册
            if (mFragment != null) {
                MediaUtils.toGallery(mFragment);
            } else {
                MediaUtils.toGallery(mAct);
            }
            dismiss();
        } else if (v.getId() == R.id.btn_to_cancel) {//取消
            dismiss();
        }
    }
}
