package com.test.lsm.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/4
 */
public class BaiduMapUtils {

    /**
     * 计算一堆坐标的总距离
     *
     * @param points
     * @return
     */
    public static double calcDistance(List<LatLng> points){

        double result = 0;
        for(int i=0 ; i< points.size()-1 ; i++){
            LatLng start = points.get(i);//前面一个坐标点
            LatLng end = points.get(i+1);//后面的一个坐标点
            double distance = DistanceUtil.getDistance(start, end);
            result = result+distance;
        }

        return result;
    }

    /**
     * 计算两个坐标点的距离
     *
     * @param start
     * @param end
     * @return
     */
    public static  double getDistance(LatLng start, LatLng end) {

        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d;
    }

    public static String resolveDistance(double distance){
        if (distance>1000){
            double v = distance / 1000;
            double v1 = handleDouble(v);
            return v1+" km";
        }else{
            return handleDouble(distance)+" m";
        }
    }

    private static double handleDouble(double f){
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

}
