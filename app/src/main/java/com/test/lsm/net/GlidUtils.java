package com.test.lsm.net;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/12
 */
public class GlidUtils {

    public static void load(Context context , ImageView iv , String imgURL){

        Glide.with(context ).load(APIFactory.BASE_URL+imgURL).into(iv);
    }

}
