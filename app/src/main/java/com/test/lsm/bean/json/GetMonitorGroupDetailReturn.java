package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/27
 */
public class GetMonitorGroupDetailReturn {

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
        private long groupId;
        private String groupName;
        private int groupUserId;
        private String status;
        private String createTime;
        private String updateTime;
        private List<MemInfoListBean> memInfoList;
        private List<ExpMemInfoListBean> expMemInfoList;

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getGroupUserId() {
            return groupUserId;
        }

        public void setGroupUserId(int groupUserId) {
            this.groupUserId = groupUserId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<MemInfoListBean> getMemInfoList() {
            return memInfoList;
        }

        public void setMemInfoList(List<MemInfoListBean> memInfoList) {
            this.memInfoList = memInfoList;
        }

        public List<ExpMemInfoListBean> getExpMemInfoList() {
            return expMemInfoList;
        }

        public void setExpMemInfoList(List<ExpMemInfoListBean> expMemInfoList) {
            this.expMemInfoList = expMemInfoList;
        }

        public static class MemInfoListBean {
            private int userId;
            private String userName;
            private String phone;
            private String userImage;
            private String heartNum;
            private String calorieValue;
            private String stepNum;
            private String currentLon;
            private String currentLat;
            private String watchingTag;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getHeartNum() {
                return heartNum;
            }

            public void setHeartNum(String heartNum) {
                this.heartNum = heartNum;
            }

            public String getCalorieValue() {
                return calorieValue;
            }

            public void setCalorieValue(String calorieValue) {
                this.calorieValue = calorieValue;
            }

            public String getStepNum() {
                return stepNum;
            }

            public void setStepNum(String stepNum) {
                this.stepNum = stepNum;
            }

            public String getCurrentLon() {
                return currentLon;
            }

            public void setCurrentLon(String currentLon) {
                this.currentLon = currentLon;
            }

            public String getCurrentLat() {
                return currentLat;
            }

            public void setCurrentLat(String currentLat) {
                this.currentLat = currentLat;
            }

            public String getWatchingTag() {
                return watchingTag;
            }

            public void setWatchingTag(String watchingTag) {
                this.watchingTag = watchingTag;
            }
        }

        public static class ExpMemInfoListBean extends MemInfoListBean{

            private String expTitle;
            private String expContent;
            private String expDateTime;

            public String getExpTitle() {
                return expTitle;
            }

            public void setExpTitle(String expTitle) {
                this.expTitle = expTitle;
            }

            public String getExpContent() {
                return expContent;
            }

            public void setExpContent(String expContent) {
                this.expContent = expContent;
            }

            public String getExpDateTime() {
                return expDateTime;
            }

            public void setExpDateTime(String expDateTime) {
                this.expDateTime = expDateTime;
            }
        }
    }
}
