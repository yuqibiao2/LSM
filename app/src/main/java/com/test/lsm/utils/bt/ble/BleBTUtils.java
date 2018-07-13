package com.test.lsm.utils.bt.ble;

import android.content.Context;

import com.yyyu.baselibrary.utils.MySPUtils;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/20
 */
public class BleBTUtils {

    public static final String CONNECTED_DEVICE="CONNECTED_DEVICE";

    /**
     * 得到配对设备的mac地址
     *
     * @param context
     * @return
     */
    public static String getConnectDevice(Context context){

        return (String) MySPUtils.get(context , CONNECTED_DEVICE , "");
    }

    /**
     * 保存已配对的设备的mac地址
     *
     * @param context
     * @param mac
     */
    public static void saveConnectDevice(Context context , String mac){
        MySPUtils.put(context ,CONNECTED_DEVICE , mac);
    }


    /**
     * 得到数据转成short
     *
     * @param n     1、2、3、4、5
     * @param value
     * @return
     */
    public static int getECGValue(int n, byte[] value) {
        if (n <= 0 || n > 5) {
            throw new IndexOutOfBoundsException();
        }
        return (((value[(n - 1) * 2 + 1] & 0xFF) << 8) | (value[(n - 1) * 2] & 0xFF));
    }

}
