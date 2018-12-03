package com.yyyu.baselibrary.ui.widget;

import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.yyyu.baselibrary.utils.MyLog;

/**
 * 功能：SwitchCompat扩展
 *
 * @author yu
 * @version 1.0
 * @date 2018/12/3
 */
public class SwitchCompatWrapper {

    private static final String TAG = "SwitchCompatWrapper";
    private SwitchCompat switchCompat;
    private boolean isCallbackChgEvent = true;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchCompatWrapper(SwitchCompat switchCompat) {
        this.switchCompat = switchCompat;
    }

    public void setOnCheckedChangeListener(final CompoundButton.OnCheckedChangeListener onCheckedChangeListener){
        mOnCheckedChangeListener = onCheckedChangeListener;
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isCallbackChgEvent && mOnCheckedChangeListener!=null){
                    mOnCheckedChangeListener.onCheckedChanged(buttonView , isChecked);
                }
                isCallbackChgEvent = true;
                MyLog.e(TAG , "setOnCheckedChangeListener==isCallbackChgEvent："+isCallbackChgEvent);
            }
        });
    }

    /**
     * 改变SwitchCompat状态且不触发OnCheckedChangeListener事件
     *
     * @param isChecked
     */
    public void setCheckedNotCallbackChgEvent(boolean isChecked){
        if (mOnCheckedChangeListener!=null
                && isChecked != switchCompat.isChecked()){//checked状态变化时才会触发事件
            isCallbackChgEvent = false;
        }
        switchCompat.setChecked(isChecked);
        MyLog.e(TAG , "setCheckedNotCallbackChgEvent==isCallbackChgEvent："+isCallbackChgEvent);
    }
    public void setChecked(boolean isChecked){
        isCallbackChgEvent  = true;
        switchCompat.setChecked(isChecked);
    }

    public SwitchCompat getSwitchCompat() {
        return switchCompat;
    }

}
