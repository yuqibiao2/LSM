package com.test.lsm.net;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yyyu.baselibrary.utils.MyLog;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/12
 */
public class GlidUtils {
    private static final String TAG = "GlidUtils";

    //public static final String BASE_IMAGE_URL = "http://lsm.mycgb.cn/";

    public static void load(Context context , ImageView iv , String imgURL){
        if (!TextUtils.isEmpty(imgURL)){
            Glide.with(context ).load(imgURL.trim()).into(iv);
        }
    }

}
