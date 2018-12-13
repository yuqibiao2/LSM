package com.test.lsm.utils.logic;

import com.test.lsm.bean.vo.MonitorExpMsgVo;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/12/13
 */
public class MonitorExpMsgFactory {

    private MonitorExpMsgFactory(){

    }

    private static class SingletonHolder {
        private static final MonitorExpMsgFactory INSTANCE = new MonitorExpMsgFactory();
    }

    public static MonitorExpMsgFactory getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 啟動緊急聯絡功能
     *
     * @param userId
     * @return
     */
    public MonitorExpMsgVo createExpMsg5(Integer userId){
        Builder builder = new Builder();
        builder.setUserId(userId)
                .setMsgType(5)
                .setExpTitle("啟動緊急聯絡功能")
                .setExpContent("⽤⼾啟動緊急聯絡功能");
        return  builder.create();
    }

    /**
     * 疲勞度極⼤
     *
     * @param userId
     * @return
     */
    public MonitorExpMsgVo createExpMsg4(Integer userId){
        Builder builder = new Builder();
        builder.setUserId(userId)
                .setMsgType(4)
                .setExpTitle("疲勞度極⼤")
                .setExpContent("⾝體非常疲憊, 需要確認休息");
        return  builder.create();
    }

    /**
     * 體⼒值低,
     *
     * @param userId
     * @return
     */
    public MonitorExpMsgVo createExpMsg3(Integer userId){
        Builder builder = new Builder();
        builder.setUserId(userId)
                .setMsgType(3)
                .setExpTitle("體⼒值低")
                .setExpContent("體⼒值低, 請確認是否需要休息");
        return  builder.create();
    }

    /**
     * 設備佩戴异常
     *
     * @param userId
     * @return
     */
    public MonitorExpMsgVo createExpMsg2(Integer userId){
        Builder builder = new Builder();
        builder.setUserId(userId)
                .setMsgType(2)
                .setExpTitle("設備佩戴异常")
                .setExpContent("需要確認是否裝置配戴正確");
        return  builder.create();
    }

    /**
     * ⼼率異常訊息
     *
     * @return
     */
    public MonitorExpMsgVo createExpMsg1(Integer userId , Integer hrNUm){
        Builder builder = new Builder();
        builder.setUserId(userId)
                .setMsgType(1)
                .setExpTitle("心率异常")
                .setExpContent(hrNUm+" bpm");
        return  builder.create();
    }

    public static  class Builder{

        private final MonitorExpMsgVo monitorExpMsgVo;

        public Builder(){
            monitorExpMsgVo = new MonitorExpMsgVo();
        }

        public Builder setUserId(Integer userId){
            monitorExpMsgVo.setUserId(userId);
            return this;
        }

        public  Builder setMsgType(Integer msgType){
            monitorExpMsgVo.setMsgType(msgType);
            return this;
        }

        public  Builder setExpTitle(String expTitle){
            monitorExpMsgVo.setExpTitle(expTitle);
            return this;
        }

        public Builder setExpContent(String expContent){
            monitorExpMsgVo.setExpContent(expContent);
            return this;
        }

        public MonitorExpMsgVo create(){
            if (monitorExpMsgVo.getUserId()==0){
                throw new UnsupportedOperationException("userId未传入");
            }
            return monitorExpMsgVo;
        }

    }

}
