package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/30
 */
public class UserCourseTimeReturn {



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
        private String START_TIME;
        private String END_TIME;
        private int COURSE_ID;
        private int COACH_ID;
        private int USER_ID;
        private String COURSE_LEVEL;
        private String COURSE_TYPE;

        public int getUC_ID() {
            return UC_ID;
        }

        public void setUC_ID(int UC_ID) {
            this.UC_ID = UC_ID;
        }

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

        public int getCOURSE_ID() {
            return COURSE_ID;
        }

        public void setCOURSE_ID(int COURSE_ID) {
            this.COURSE_ID = COURSE_ID;
        }

        public int getCOACH_ID() {
            return COACH_ID;
        }

        public void setCOACH_ID(int COACH_ID) {
            this.COACH_ID = COACH_ID;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getCOURSE_LEVEL() {
            return COURSE_LEVEL;
        }

        public void setCOURSE_LEVEL(String COURSE_LEVEL) {
            this.COURSE_LEVEL = COURSE_LEVEL;
        }

        public String getCOURSE_TYPE() {
            return COURSE_TYPE;
        }

        public void setCOURSE_TYPE(String COURSE_TYPE) {
            this.COURSE_TYPE = COURSE_TYPE;
        }
    }
}
