package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/30
 */
public class GetUserMonitorsReturn {


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

        private boolean watchAll;
        private List<MonitorInfoListBean> monitorInfoList;

        public boolean isWatchAll() {
            return watchAll;
        }

        public void setWatchAll(boolean watchAll) {
            this.watchAll = watchAll;
        }

        public List<MonitorInfoListBean> getMonitorInfoList() {
            return monitorInfoList;
        }

        public void setMonitorInfoList(List<MonitorInfoListBean> monitorInfoList) {
            this.monitorInfoList = monitorInfoList;
        }

        public static class MonitorInfoListBean {

            private long attachId;
            private int userId;
            private String username;
            private String phone;
            private String userImage;
            private String isWatching;

            public long getAttachId() {
                return attachId;
            }

            public void setAttachId(long attachId) {
                this.attachId = attachId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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

            public String getIsWatching() {
                return isWatching;
            }

            public void setIsWatching(String isWatching) {
                this.isWatching = isWatching;
            }
        }
    }
}
