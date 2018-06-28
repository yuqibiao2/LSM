package com.test.lsm.utils;

import com.swm.algorithm.Algorithm;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/6/26
 */
public class AlgorithmWrapper {

    private static boolean isRRIStart = false;

    public static void startRRI() {
        if (isRRIStart){
            return;
        }
        Algorithm.initialForModeChange(1);
        isRRIStart = true;
    }

    public static void stopRRI() {
        if (!isRRIStart){
            return;
        }
        Algorithm.initialForModeChange(0);
        isRRIStart = false;
    }

}
