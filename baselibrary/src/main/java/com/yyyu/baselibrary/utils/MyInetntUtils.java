package com.yyyu.baselibrary.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * 功能：系统Intent跳转
 *
 * @author yu
 * @version 1.0
 * @date 2018/6/11
 */
public class MyInetntUtils {


    public static void toCall(Context context , String tel ){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+tel));
        context.startActivity(intent);
    }

}
