package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/16
 */
public class GetHRVInfoReturn {


    private List<HRVIndexBean> HRVIndex;
    private List<MessageBean> Message;

    public List<HRVIndexBean> getHRVIndex() {
        return HRVIndex;
    }

    public void setHRVIndex(List<HRVIndexBean> HRVIndex) {
        this.HRVIndex = HRVIndex;
    }

    public List<MessageBean> getMessage() {
        return Message;
    }

    public void setMessage(List<MessageBean> Message) {
        this.Message = Message;
    }

    public static class HRVIndexBean {
        private String ServerId;
        private String mindFitness;
        private String bodyFitness;
        private String moodStability;
        private String stressTension;
        private String mindFatigue;
        private String bodyFatigue;
        private String heartRate;
        private String hrvAge;
        private String rrSpread;
        private String sdnn;
        private String sdnnRatio;
        private String lf;
        private String lfRatio;
        private String lfPercent;
        private String lfPercentRatio;
        private String lfhf;
        private String lfhfRatio;
        private String hf;
        private String hfRatio;

        public String getServerId() {
            return ServerId;
        }

        public void setServerId(String ServerId) {
            this.ServerId = ServerId;
        }

        public String getMindFitness() {
            return mindFitness;
        }

        public void setMindFitness(String mindFitness) {
            this.mindFitness = mindFitness;
        }

        public String getBodyFitness() {
            return bodyFitness;
        }

        public void setBodyFitness(String bodyFitness) {
            this.bodyFitness = bodyFitness;
        }

        public String getMoodStability() {
            return moodStability;
        }

        public void setMoodStability(String moodStability) {
            this.moodStability = moodStability;
        }

        public String getStressTension() {
            return stressTension;
        }

        public void setStressTension(String stressTension) {
            this.stressTension = stressTension;
        }

        public String getMindFatigue() {
            return mindFatigue;
        }

        public void setMindFatigue(String mindFatigue) {
            this.mindFatigue = mindFatigue;
        }

        public String getBodyFatigue() {
            return bodyFatigue;
        }

        public void setBodyFatigue(String bodyFatigue) {
            this.bodyFatigue = bodyFatigue;
        }

        public String getHeartRate() {
            return heartRate;
        }

        public void setHeartRate(String heartRate) {
            this.heartRate = heartRate;
        }

        public String getHrvAge() {
            return hrvAge;
        }

        public void setHrvAge(String hrvAge) {
            this.hrvAge = hrvAge;
        }

        public String getRrSpread() {
            return rrSpread;
        }

        public void setRrSpread(String rrSpread) {
            this.rrSpread = rrSpread;
        }

        public String getSdnn() {
            return sdnn;
        }

        public void setSdnn(String sdnn) {
            this.sdnn = sdnn;
        }

        public String getSdnnRatio() {
            return sdnnRatio;
        }

        public void setSdnnRatio(String sdnnRatio) {
            this.sdnnRatio = sdnnRatio;
        }

        public String getLf() {
            return lf;
        }

        public void setLf(String lf) {
            this.lf = lf;
        }

        public String getLfRatio() {
            return lfRatio;
        }

        public void setLfRatio(String lfRatio) {
            this.lfRatio = lfRatio;
        }

        public String getLfPercent() {
            return lfPercent;
        }

        public void setLfPercent(String lfPercent) {
            this.lfPercent = lfPercent;
        }

        public String getLfPercentRatio() {
            return lfPercentRatio;
        }

        public void setLfPercentRatio(String lfPercentRatio) {
            this.lfPercentRatio = lfPercentRatio;
        }

        public String getLfhf() {
            return lfhf;
        }

        public void setLfhf(String lfhf) {
            this.lfhf = lfhf;
        }

        public String getLfhfRatio() {
            return lfhfRatio;
        }

        public void setLfhfRatio(String lfhfRatio) {
            this.lfhfRatio = lfhfRatio;
        }

        public String getHf() {
            return hf;
        }

        public void setHf(String hf) {
            this.hf = hf;
        }

        public String getHfRatio() {
            return hfRatio;
        }

        public void setHfRatio(String hfRatio) {
            this.hfRatio = hfRatio;
        }
    }

    public static class MessageBean {
        private String Status;
        private String Message;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }
    }
}
