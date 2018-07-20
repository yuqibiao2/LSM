package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/19
 */
public class GetCoachByCourseType {


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
        private List<ListBean> downLineList;
        private List<ListBean> onLineList;

        public List<ListBean> getDownLineList() {
            return downLineList;
        }

        public void setDownLineList(List<ListBean> downLineList) {
            this.downLineList = downLineList;
        }

        public List<ListBean> getOnLineList() {
            return onLineList;
        }

        public void setOnLineList(List<ListBean> onLineList) {
            this.onLineList = onLineList;
        }
    }


    public static class ListBean {

        private String COACH_NAME;
        private int COACH_ID;
        private String COACH_IMG;
        private String COURSE_IMG;
        private String COURSE_TYPE;
        private int WEEK_NUMBER;
        private String NAME;

        public String getCOURSE_IMG() {
            return COURSE_IMG;
        }

        public void setCOURSE_IMG(String COURSE_IMG) {
            this.COURSE_IMG = COURSE_IMG;
        }

        public String getCOACH_NAME() {
            return COACH_NAME;
        }

        public void setCOACH_NAME(String COACH_NAME) {
            this.COACH_NAME = COACH_NAME;
        }

        public int getCOACH_ID() {
            return COACH_ID;
        }

        public void setCOACH_ID(int COACH_ID) {
            this.COACH_ID = COACH_ID;
        }

        public String getCOACH_IMG() {
            return COACH_IMG;
        }

        public void setCOACH_IMG(String COACH_IMG) {
            this.COACH_IMG = COACH_IMG;
        }

        public String getCOURSE_TYPE() {
            return COURSE_TYPE;
        }

        public void setCOURSE_TYPE(String COURSE_TYPE) {
            this.COURSE_TYPE = COURSE_TYPE;
        }

        public int getWEEK_NUMBER() {
            return WEEK_NUMBER;
        }

        public void setWEEK_NUMBER(int WEEK_NUMBER) {
            this.WEEK_NUMBER = WEEK_NUMBER;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }
    }
}
