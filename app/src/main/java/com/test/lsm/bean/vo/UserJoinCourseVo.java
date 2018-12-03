package com.test.lsm.bean.vo;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/30
 */
public class UserJoinCourseVo {

    private Integer USER_ID;
    private String COURSE_TYPE;
    private Integer COURSE_LEVEL;//课程级别：0-轻度；1-中度；2-重度
    private Integer COACH_ID;
    private Integer CC_TYPE;//学习类型：0-线上；1-线下

    public Integer getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(Integer USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCOURSE_TYPE() {
        return COURSE_TYPE;
    }

    public void setCOURSE_TYPE(String COURSE_TYPE) {
        this.COURSE_TYPE = COURSE_TYPE;
    }

    public Integer getCOURSE_LEVEL() {
        return COURSE_LEVEL;
    }

    public void setCOURSE_LEVEL(Integer COURSE_LEVEL) {
        this.COURSE_LEVEL = COURSE_LEVEL;
    }

    public Integer getCOACH_ID() {
        return COACH_ID;
    }

    public void setCOACH_ID(Integer COACH_ID) {
        this.COACH_ID = COACH_ID;
    }

    public Integer getCC_TYPE() {
        return CC_TYPE;
    }

    public void setCC_TYPE(Integer CC_TYPE) {
        this.CC_TYPE = CC_TYPE;
    }
}
