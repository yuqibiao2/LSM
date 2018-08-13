package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/11
 */
public class ModifyScoreReturn {


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

        private int UC_ID;
        private String CREATE_DATE;
        private String SCORE_VALUE;
        private int US_ID;
        private int USER_ID;

        public int getUC_ID() {
            return UC_ID;
        }

        public void setUC_ID(int UC_ID) {
            this.UC_ID = UC_ID;
        }

        public String getCREATE_DATE() {
            return CREATE_DATE;
        }

        public void setCREATE_DATE(String CREATE_DATE) {
            this.CREATE_DATE = CREATE_DATE;
        }

        public String getSCORE_VALUE() {
            return SCORE_VALUE;
        }

        public void setSCORE_VALUE(String SCORE_VALUE) {
            this.SCORE_VALUE = SCORE_VALUE;
        }

        public int getUS_ID() {
            return US_ID;
        }

        public void setUS_ID(int US_ID) {
            this.US_ID = US_ID;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }
    }
}
