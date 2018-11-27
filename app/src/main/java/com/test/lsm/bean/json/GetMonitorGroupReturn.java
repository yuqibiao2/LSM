package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/27
 */
public class GetMonitorGroupReturn {

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private long groupId;
        private String groupName;
        private int groupUserId;
        private String status;
        private String createTime;
        private String updateTime;
        private List<MonitorGroupDetailListBean> monitorGroupDetailList;

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

        public List<MonitorGroupDetailListBean> getMonitorGroupDetailList() {
            return monitorGroupDetailList;
        }

        public void setMonitorGroupDetailList(List<MonitorGroupDetailListBean> monitorGroupDetailList) {
            this.monitorGroupDetailList = monitorGroupDetailList;
        }

        public static class MonitorGroupDetailListBean {

            private long detailId;
            private long groupId;
            private int memUserId;
            private String watchingTag;
            private String createTime;
            private String updateTime;

            public long getDetailId() {
                return detailId;
            }

            public void setDetailId(long detailId) {
                this.detailId = detailId;
            }

            public long getGroupId() {
                return groupId;
            }

            public void setGroupId(long groupId) {
                this.groupId = groupId;
            }

            public int getMemUserId() {
                return memUserId;
            }

            public void setMemUserId(int memUserId) {
                this.memUserId = memUserId;
            }

            public String getWatchingTag() {
                return watchingTag;
            }

            public void setWatchingTag(String watchingTag) {
                this.watchingTag = watchingTag;
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
        }
    }
}
