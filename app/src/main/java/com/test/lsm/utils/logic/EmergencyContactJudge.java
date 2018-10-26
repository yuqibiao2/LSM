package com.test.lsm.utils.logic;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * 功能：紧急联系人弹出条件
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class EmergencyContactJudge {

    private static CircularFifoQueue<Integer> judgeBuff = new CircularFifoQueue<>(6);


    public static boolean doJudge(Integer hrNum, Integer thresholdHrNum) {

        boolean result = false;
        judgeBuff.add(hrNum);

        if (judgeBuff.size() == 6) {
            result = true;
            for (int i = 0; i < judgeBuff.size(); i++) {
                Integer hr = judgeBuff.get(i);
                if (hr <= thresholdHrNum) {
                    result = false;
                }
            }
        }

        return result;
    }

}
