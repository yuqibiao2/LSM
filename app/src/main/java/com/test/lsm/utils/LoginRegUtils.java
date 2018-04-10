package com.test.lsm.utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.yyyu.baselibrary.utils.FormValidationUtils;

/**
 * 功能：登录注册相关工具类
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class LoginRegUtils {

    public enum CheckType {
        tel, pwd,height,weight
    }

    /**
     * TextInputLayout 输入提示（检测）
     *
     * @param textInputLayout
     * @param checkType            0 ：手机号 1：密码
     */
    public static  void checkEdit(final TextInputLayout textInputLayout, final CheckType checkType) {
        EditText editText = textInputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (checkType) {
                    case tel: {
                        if (!FormValidationUtils.isMobileNO(s.toString())) {
                            textInputLayout.setError("请输入正确的手机号！！！");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                        break;
                    }
                    case pwd: {
                        if (s.length() < 6) {
                            textInputLayout.setError("密码在6-15位之间！！！");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                        break;
                    }
                    case height:{

                        break;
                    }
                    case weight:{

                        break;
                    }
                }
            }
        });
    }

}
