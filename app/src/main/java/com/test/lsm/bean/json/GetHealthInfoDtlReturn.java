package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/20
 */
public class GetHealthInfoDtlReturn {

    private String result;
    private PdBean pd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PdBean getPd() {
        return pd;
    }

    public void setPd(PdBean pd) {
        this.pd = pd;
    }

    public static class PdBean {

        private String record;

        public String getRecord() {
            return record;
        }

        public void setRecord(String record) {
            this.record = record;
        }
    }
}
