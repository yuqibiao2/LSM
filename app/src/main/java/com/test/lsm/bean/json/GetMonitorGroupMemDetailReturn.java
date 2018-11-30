package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/27
 */
public class GetMonitorGroupMemDetailReturn {

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private HrvInfoBean hrvInfo;
        private TraceInfoBean traceInfo;
        private List<HeartInfoListBean> heartInfoList;

        public HrvInfoBean getHrvInfo() {
            return hrvInfo;
        }

        public void setHrvInfo(HrvInfoBean hrvInfo) {
            this.hrvInfo = hrvInfo;
        }

        public TraceInfoBean getTraceInfo() {
            return traceInfo;
        }

        public void setTraceInfo(TraceInfoBean traceInfo) {
            this.traceInfo = traceInfo;
        }

        public List<HeartInfoListBean> getHeartInfoList() {
            return heartInfoList;
        }

        public void setHeartInfoList(List<HeartInfoListBean> heartInfoList) {
            this.heartInfoList = heartInfoList;
        }

        public static class HrvInfoBean {
            private int mindFitness;
            private int bodyFitness;
            private int moodStability;
            private int stressTension;
            private int mindFatigue;
            private int bodyFatigue;

            public int getMindFitness() {
                return mindFitness;
            }

            public void setMindFitness(int mindFitness) {
                this.mindFitness = mindFitness;
            }

            public int getBodyFitness() {
                return bodyFitness;
            }

            public void setBodyFitness(int bodyFitness) {
                this.bodyFitness = bodyFitness;
            }

            public int getMoodStability() {
                return moodStability;
            }

            public void setMoodStability(int moodStability) {
                this.moodStability = moodStability;
            }

            public int getStressTension() {
                return stressTension;
            }

            public void setStressTension(int stressTension) {
                this.stressTension = stressTension;
            }

            public int getMindFatigue() {
                return mindFatigue;
            }

            public void setMindFatigue(int mindFatigue) {
                this.mindFatigue = mindFatigue;
            }

            public int getBodyFatigue() {
                return bodyFatigue;
            }

            public void setBodyFatigue(int bodyFatigue) {
                this.bodyFatigue = bodyFatigue;
            }
        }

        public static class TraceInfoBean {
            private double distance;
            private double stepSpeed;
            private int maxHeart;
            private int avgHeart;
            private String runAddress;
            private String coordinateInfo;

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public double getStepSpeed() {
                return stepSpeed;
            }

            public void setStepSpeed(double stepSpeed) {
                this.stepSpeed = stepSpeed;
            }

            public int getMaxHeart() {
                return maxHeart;
            }

            public void setMaxHeart(int maxHeart) {
                this.maxHeart = maxHeart;
            }

            public int getAvgHeart() {
                return avgHeart;
            }

            public void setAvgHeart(int avgHeart) {
                this.avgHeart = avgHeart;
            }

            public String getRunAddress() {
                return runAddress;
            }

            public void setRunAddress(String runAddress) {
                this.runAddress = runAddress;
            }

            public String getCoordinateInfo() {
                return coordinateInfo;
            }

            public void setCoordinateInfo(String coordinateInfo) {
                this.coordinateInfo = coordinateInfo;
            }
        }

        public static class HeartInfoListBean {
            private int heartNum;

            public int getHeartNum() {
                return heartNum;
            }

            public void setHeartNum(int heartNum) {
                this.heartNum = heartNum;
            }
        }
    }
}
