package com.test.lsm.global;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class Constant {

    public static StringBuffer sbHeartData = new StringBuffer();
    //public static StringBuffer sbHeartData2 = new StringBuffer();
    public static CircularFifoQueue<Short> egcDataCon = new CircularFifoQueue(1500);

    public static CircularFifoQueue<Long> rriBuffer = new CircularFifoQueue(250);

}
