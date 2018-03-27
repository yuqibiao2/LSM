package com.test.lsm.net;

import com.test.lsm.net.api.LsmApi;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class APIMethodManager {

    private LsmApi lsmApi;

    private APIMethodManager() {
        APIFactory apiFactory = APIFactory.getInstance();
        lsmApi = apiFactory.createLsmApi();
    }

    private static class SingletonHolder {
        private static final APIMethodManager INSTANCE = new APIMethodManager();
    }

    public static APIMethodManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
