package com.test.lsm.utils.logic;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * 功能：判斷心跳是否異常
 *
 * @author yu
 * @version 1.0
 * @date 2018/12/13
 */
public class JudgeHrExpUtils {

    private static CircularFifoQueue<Integer> hrContainer = new CircularFifoQueue<>(30);

    public static boolean  isExp(Integer hrNum){
        boolean result = true;

        hrContainer.add(hrNum);
        if (hrContainer.size()>=25){
            for (int i = 0; i <hrContainer.size() ; i++) {
                Integer hr = hrContainer.get(i);
                if (hr<180){
                    result = false;
                }
            }
        }else {
            result =false;
        }

        return result;
    }

}
