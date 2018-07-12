package com.test.lsm.bean.json;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/17
 */
public class UserRegReturn {

    private String result;
    private UserLoginReturn.PdBean pd;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UserLoginReturn.PdBean getPd() {
        return pd;
    }

    public void setPd(UserLoginReturn.PdBean pd) {
        this.pd = pd;
    }
}
