package com.test.lsm.utils.bt.tradition;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import static com.test.lsm.utils.bt.tradition.Constant.MESSAGE_TAG;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/4
 */
public class LsmDeviceManager {

    private static Context mContext;
    private LsmDeviceHelper lsmDeviceHelper;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_TAG:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if (mOnReadResultListener!=null){
                        mOnReadResultListener.onRead(readMessage);
                    }
                    break;
            }
        }
    };

    private static class InstanceHolder{
        public static LsmDeviceManager INSTANCE = new LsmDeviceManager();
    }

    private LsmDeviceManager(){
        lsmDeviceHelper = new LsmDeviceHelper(mContext , mHandler);
    }

    public static LsmDeviceManager getInstance(Context context) {
        mContext = context;
        return InstanceHolder.INSTANCE;
    }

    public boolean isConnected(){
        return lsmDeviceHelper.isConnected();
    }

    /**
     * 连接设备
     *
     * @param device
     */
    public void connectDevice(BluetoothDevice device){
        if (lsmDeviceHelper.isConnected()){
            lsmDeviceHelper.connect(device);
        }
    }


    OnReadResultListener mOnReadResultListener;

    public  void setOnReadResultListener(OnReadResultListener onReadResultListener){
        this.mOnReadResultListener = onReadResultListener;
    }

    public  interface  OnReadResultListener{
        void onRead(String tagId);
    }

}
