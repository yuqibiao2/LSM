package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/16
 */
public class GetMsgDetail {

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

        private RecordBean record;

        public RecordBean getRecord() {
            return record;
        }

        public void setRecord(RecordBean record) {
            this.record = record;
        }

        public static class RecordBean {

            private String PUSH_DESC;
            private String PUSH_IMAGE_URL;
            private String FILE_ICON_URL;
            private String PUSH_TIME;
            private String USER_ID;
            private int ID;
            private String PUSH_TITLE;

            public String getPUSH_DESC() {
                return PUSH_DESC;
            }

            public void setPUSH_DESC(String PUSH_DESC) {
                this.PUSH_DESC = PUSH_DESC;
            }

            public String getPUSH_IMAGE_URL() {
                return PUSH_IMAGE_URL;
            }

            public void setPUSH_IMAGE_URL(String PUSH_IMAGE_URL) {
                this.PUSH_IMAGE_URL = PUSH_IMAGE_URL;
            }

            public String getFILE_ICON_URL() {
                return FILE_ICON_URL;
            }

            public void setFILE_ICON_URL(String FILE_ICON_URL) {
                this.FILE_ICON_URL = FILE_ICON_URL;
            }

            public String getPUSH_TIME() {
                return PUSH_TIME;
            }

            public void setPUSH_TIME(String PUSH_TIME) {
                this.PUSH_TIME = PUSH_TIME;
            }

            public String getUSER_ID() {
                return USER_ID;
            }

            public void setUSER_ID(String USER_ID) {
                this.USER_ID = USER_ID;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getPUSH_TITLE() {
                return PUSH_TITLE;
            }

            public void setPUSH_TITLE(String PUSH_TITLE) {
                this.PUSH_TITLE = PUSH_TITLE;
            }
        }
    }
}
