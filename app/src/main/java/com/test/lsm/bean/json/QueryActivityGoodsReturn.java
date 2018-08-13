package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/8/11
 */
public class QueryActivityGoodsReturn {

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

        private PdBeanItem nextPd;
        private PdBeanItem currentPd;

        public PdBeanItem getNextPd() {
            return nextPd;
        }

        public void setNextPd(PdBeanItem nextPd) {
            this.nextPd = nextPd;
        }

        public PdBeanItem getCurrentPd() {
            return currentPd;
        }

        public void setCurrentPd(PdBeanItem currentPd) {
            this.currentPd = currentPd;
        }

        public static class PdBeanItem {

            private String START_TIME;
            private String END_TIME;
            private double GOODS_PRICE;
            private String GOODS_NAME;
            private String GOODS_PIC;
            private int ACTIVITY_ID;

            public String getSTART_TIME() {
                return START_TIME;
            }

            public void setSTART_TIME(String START_TIME) {
                this.START_TIME = START_TIME;
            }

            public String getEND_TIME() {
                return END_TIME;
            }

            public void setEND_TIME(String END_TIME) {
                this.END_TIME = END_TIME;
            }

            public double getGOODS_PRICE() {
                return GOODS_PRICE;
            }

            public void setGOODS_PRICE(double GOODS_PRICE) {
                this.GOODS_PRICE = GOODS_PRICE;
            }

            public String getGOODS_NAME() {
                return GOODS_NAME;
            }

            public void setGOODS_NAME(String GOODS_NAME) {
                this.GOODS_NAME = GOODS_NAME;
            }

            public String getGOODS_PIC() {
                return GOODS_PIC;
            }

            public void setGOODS_PIC(String GOODS_PIC) {
                this.GOODS_PIC = GOODS_PIC;
            }

            public int getACTIVITY_ID() {
                return ACTIVITY_ID;
            }

            public void setACTIVITY_ID(int ACTIVITY_ID) {
                this.ACTIVITY_ID = ACTIVITY_ID;
            }
        }

    }
}
