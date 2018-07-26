package com.test.lsm.utils;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/4
 */
public class TimeUtils {

    /**
     * 格式化计时器时间
     *
     * @param  time 秒
     * @return 返回格式 00:00:1
     */
    public static String countTimer(Long time) {
        int countTime = time.intValue();
        final int MINUTE = 60;
        final int HOUR = 60 * 60;
        return cpx(countTime / HOUR) + ":" +cpx(countTime % HOUR / MINUTE )+ ":" +cpx( countTime % MINUTE);
    }

    /**
     * 补全两位
     * @param i
     * @return
     */
    private static String cpx(int i){
        if (i<10){
            return "0"+i;
        }else{
            return ""+i;
        }
    }

}
