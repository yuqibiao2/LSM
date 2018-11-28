package com.test.lsm.ui.activity;

import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.example.jpushdemo.TagAliasOperatorHelper;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.utils.LoginRegUtils;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.List;

import butterknife.BindView;

import static com.example.jpushdemo.TagAliasOperatorHelper.ACTION_SET;

/**
 * 功能：启动页
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class SplashActivity extends LsmBaseActivity {

    private static final String TAG = "SplashActivity";

    @BindView(R.id.iv_splash)
    ImageView iv_splash;
    private MyApplication application;

    private boolean connected = false;

    private boolean finished = false;
    private BleManager bleManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getApplication();
        bleManager = BleManager.getInstance();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void afterInit() {
        super.afterInit();

        //resolveSkip();
        toConnectLsm();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anime);
        iv_splash.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              /*  if(LoginRegUtils.isLogin(SplashActivity.this)){//登录了
                    UserLoginReturn.PdBean loginUser = LoginRegUtils.getLoginUser(SplashActivity.this);
                    ((MyApplication)getApplication()).setUser(loginUser);
                    MainActivity.startAction(SplashActivity.this);
                }else{
                    LoginActivity.startAction(SplashActivity.this);
                }*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 连接ble设备
     *
     * @return
     */
    private void toConnectLsm() {
        final String connectDeviceMac = BleBTUtils.getConnectDevice(SplashActivity.this);
        if (TextUtils.isEmpty(connectDeviceMac)) {//没有已连接的设备
            resolveSkip();
            return;
        }
        if (bleManager.isConnected(connectDeviceMac)) {//设备已连接
            showToast("设备连接成功");
            resolveSkip();
            return;
        }
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceMac(connectDeviceMac)  // 只扫描指定mac的设备，可选
                .setAutoConnect(true)    // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(5000)              // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();
        bleManager.initScanRule(scanRuleConfig);

        bleManager.scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice result) {
                if (result.getMac().equals(connectDeviceMac)) {//扫描到了最后一次连接过的设备
                    //连接设备
                    bleManager.connectWrapper(result, new BleGattCallback() {
                        @Override
                        public void onStartConnect() {

                        }

                        @Override
                        public void onConnectFail(BleException exception) {
                            showToast("蓝牙设备连接失败，请在设置界面再次连接！");
                            resolveSkip();
                        }

                        @Override
                        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                            application.setCurrentBleDevice(bleDevice);
                            BleBTUtils.saveConnectDevice(SplashActivity.this, bleDevice.getMac());
                            showToast("蓝牙设备连接成功");
                            resolveSkip();
                        }

                        @Override
                        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                        }
                    });
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                resolveSkip();
                if (!application.isBleConnected()){
                    showToast("蓝牙设备连接失败，请在设置界面再次连接！");
                }
            }
        });

    }

    private void resolveSkip() {
        if (LoginRegUtils.isLogin(SplashActivity.this)) {//登录了
            UserLoginReturn.PdBean loginUser = LoginRegUtils.getLoginUser(SplashActivity.this);
            setJPushAlias("" + loginUser.getUSER_ID());
            ((MyApplication) getApplication()).setUser(loginUser);
            MainActivity.startAction(SplashActivity.this);
        } else {
            LoginActivity.startAction(SplashActivity.this);
        }
        finish();
    }

    private void setJPushAlias(String userId) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = userId;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), 1, tagAliasBean);
    }

    private void showToast(String msg) {
        if (!finished) {
            MyToast.showLong(this, msg);
        }
    }

    @Override
    protected void onDestroy() {
        finished = true;
        if (bleManager != null) {
            bleManager.cancelScan();
        }
        super.onDestroy();
    }
}
