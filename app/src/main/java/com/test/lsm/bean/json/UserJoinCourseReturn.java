package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/30
 */
public class UserJoinCourseReturn {


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
        private  int US_ID;
        private String COACH_ID;
        private int COURSE_ID;
        private String USER_ID;
        private String COURSE_LEVEL;
        private String COURSE_TYPE;

        public int getUC_ID() {
            return UC_ID;
        }

        public void setUC_ID(int UC_ID) {
            this.UC_ID = UC_ID;
        }

        public String getCOACH_ID() {
            return COACH_ID;
        }

        public int getUS_ID() {
            return US_ID;
        }

        public void setUS_ID(int US_ID) {
            this.US_ID = US_ID;
        }

        public void setCOACH_ID(String COACH_ID) {
            this.COACH_ID = COACH_ID;
        }

        public int getCOURSE_ID() {
            return COURSE_ID;
        }

        public void setCOURSE_ID(int COURSE_ID) {
            this.COURSE_ID = COURSE_ID;
        }

        public String getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(String USER_ID) {
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
