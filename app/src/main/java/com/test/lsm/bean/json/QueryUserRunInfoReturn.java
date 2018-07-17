package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/20
 */
public class QueryUserRunInfoReturn {


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

        private double DISTANCE;
        private String START_TIME;
        private String CREATE_TIME;
        private int ID;
        private String STOP_TIME;
        private String RUN_TIME;
        private  String AVG_HEART;
        private String MAX_HEART;
        private String CALORIE_VALUE;
        private String RUN_ADDRESS;


        public String getAVG_HEART() {
            return AVG_HEART;
        }

        public void setAVG_HEART(String AVG_HEART) {
            this.AVG_HEART = AVG_HEART;
        }

        public String getMAX_HEART() {
            return MAX_HEART;
        }

        public void setMAX_HEART(String MAX_HEART) {
            this.MAX_HEART = MAX_HEART;
        }

        public String getCALORIE_VALUE() {
            return CALORIE_VALUE;
        }

        public void setCALORIE_VALUE(String CALORIE_VALUE) {
            this.CALORIE_VALUE = CALORIE_VALUE;
        }

        public String getRUN_ADDRESS() {
            return RUN_ADDRESS;
        }

        public void setRUN_ADDRESS(String RUN_ADDRESS) {
            this.RUN_ADDRESS = RUN_ADDRESS;
        }

        public double getDISTANCE() {
            return DISTANCE;
        }

        public void setDISTANCE(double DISTANCE) {
            this.DISTANCE = DISTANCE;
        }

        public String getSTART_TIME() {
            return START_TIME;
        }

        public void setSTART_TIME(String START_TIME) {
            this.START_TIME = START_TIME;
        }

        public String getCREATE_TIME() {
            return CREATE_TIME;
        }

        public void setCREATE_TIME(String CREATE_TIME) {
            this.CREATE_TIME = CREATE_TIME;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getSTOP_TIME() {
            return STOP_TIME;
        }

        public void setSTOP_TIME(String STOP_TIME) {
            this.STOP_TIME = STOP_TIME;
        }

        public String getRUN_TIME() {
            return RUN_TIME;
        }

        public void setRUN_TIME(String RUN_TIME) {
            this.RUN_TIME = RUN_TIME;
        }
    }
}
