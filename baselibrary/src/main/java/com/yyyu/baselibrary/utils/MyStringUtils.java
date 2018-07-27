package com.yyyu.baselibrary.utils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/20
 */
public class MyStringUtils {

    /**
     * 百分比转换为小数
     *
     * @param percentStr
     * @return
     */
    public static float percentToDecimals(String percentStr) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        try {
            Number number = nf.parse(percentStr);
            return number.floatValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String decimalsToPercent(float decimals) {
        return decimalsToPercent(decimals , 1);
    }

    /**
     * 小数转百分比
     *
     * @param decimals
     * @return
     */
    public static String decimalsToPercent(float decimals , int decimalPlace) {

        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(decimalPlace);

        return nf.format(decimals);
    }

}
