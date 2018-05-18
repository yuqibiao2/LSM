package com.swm.core.temp;

/**
 * Created by yangzhenyu on 2017/5/8.
 */

public interface SwmFilter {
    public void filter(EcgData ecgData);
    public void reset();
}
