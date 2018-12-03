package com.test.lsm.bean.vo;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/19
 */
public class GetHeartChart {


    private String result;
    private List<PdBean> pd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PdBean> getPd() {
        return pd;
    }

    public void setPd(List<PdBean> pd) {
        this.pd = pd;
    }

    public static class PdBean {

        private String HEART_TIME;
        private String  HEART_VALUE;

        public String getHEART_TIME() {
            return HEART_TIME;
        }

        public void setHEART_TIME(String HEART_TIME) {
            this.HEART_TIME = HEART_TIME;
        }

        public String getHEART_VALUE() {
            return HEART_VALUE;
        }

        public void setHEART_VALUE(String HEART_VALUE) {
            this.HEART_VALUE = HEART_VALUE;
        }
    }
}
