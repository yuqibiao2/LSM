package com.test.lsm;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.test.lsm.global.Const;
import com.test.lsm.utils.BaiduMapUtils;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/11
 */
public class CalcTest{

    private static final String TAG = "CalcTest";

    @Test
    public void testCalc(){

        String s = resolveDistance(12.3);
        System.out.println("===s："+s);

    }

    public String resolveDistance(double distance){
        if (distance>1000){
            double v = distance / 1000;
            double v1 = handleDouble(v);
            return v1+" KM";
        }else{
            return handleDouble(distance)+" M";
        }
    }

    private double handleDouble(double f){
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    @Test
    public void test(){
        Log.e(TAG, "test: ======" );
    }

}
