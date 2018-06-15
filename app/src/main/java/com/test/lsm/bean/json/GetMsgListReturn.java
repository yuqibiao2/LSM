package com.test.lsm.bean.json;

import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/16
 */
public class GetMsgListReturn {


    private String result;
    private List<PdBean> pd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PdBean> getPd() {
        return pd;
    }

    public void setPd(List<PdBean> pd) {
        this.pd = pd;
    }

    public static class PdBean {
        private String PUSH_IMAGE_URL;
        private String ID;
        private String PUSH_TITLE;
        private String PUSH_TIME;

        public String getPUSH_IMAGE_URL() {
            return PUSH_IMAGE_URL;
        }

        public void setPUSH_IMAGE_URL(String PUSH_IMAGE_URL) {
            this.PUSH_IMAGE_URL = PUSH_IMAGE_URL;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getPUSH_TITLE() {
            return PUSH_TITLE;
        }

        public String getPUSH_TIME() {
            return PUSH_TIME;
        }

        public void setPUSH_TIME(String PUSH_TIME) {
            this.PUSH_TIME = PUSH_TIME;
        }

        public void setPUSH_TITLE(String PUSH_TITLE) {
            this.PUSH_TITLE = PUSH_TITLE;
        }
    }
}
