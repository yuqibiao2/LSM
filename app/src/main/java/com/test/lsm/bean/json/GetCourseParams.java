package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/20
 */
public class GetCourseParams {


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
        private String START_TIME;
        private String END_TIME;
        private String PARAM_LEVEL;
        private int CP_ID;
        private int COURSE_ID;
        private String PARAM_NUMBER;
        private String COURSE_NAME;

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

        public String getPARAM_LEVEL() {
            return PARAM_LEVEL;
        }

        public void setPARAM_LEVEL(String PARAM_LEVEL) {
            this.PARAM_LEVEL = PARAM_LEVEL;
        }

        public int getCP_ID() {
            return CP_ID;
        }

        public void setCP_ID(int CP_ID) {
            this.CP_ID = CP_ID;
        }

        public int getCOURSE_ID() {
            return COURSE_ID;
        }

        public void setCOURSE_ID(int COURSE_ID) {
            this.COURSE_ID = COURSE_ID;
        }

        public String getPARAM_NUMBER() {
            return PARAM_NUMBER;
        }

        public void setPARAM_NUMBER(String PARAM_NUMBER) {
            this.PARAM_NUMBER = PARAM_NUMBER;
        }

        public String getCOURSE_NAME() {
            return COURSE_NAME;
        }

        public void setCOURSE_NAME(String COURSE_NAME) {
            this.COURSE_NAME = COURSE_NAME;
        }
    }
}
