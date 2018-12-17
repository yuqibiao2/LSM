package com.test.lsm.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.jpushdemo.ExampleUtil;
import com.example.jpushdemo.LocalBroadcastManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.MenuAdapter;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.MenuItem;
import com.test.lsm.global.Config;
import com.test.lsm.service.UploadHealthInfoService;
import com.test.lsm.ui.dialog.BleBTDeviceScanDialog;
import com.test.lsm.ui.dialog.DeviceInformationDialog;
import com.test.lsm.ui.fragment.main.ExerciseChoiceFragment;
import com.test.lsm.ui.fragment.main.InformationFragment;
import com.test.lsm.ui.fragment.main.RunFragment;
import com.test.lsm.ui.fragment.main.TodayFragment;
import com.test.lsm.utils.LoginRegUtils;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.yyyu.baselibrary.ui.widget.CommonPopupWindow;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyActUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 功能：首页
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */
public class MainActivity extends LsmBaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.ll_top_bar)
    RelativeLayout ll_top_bar;
    @BindView(R.id.ib_menu)
    ImageButton ib_menu;
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.ll_today)
    LinearLayout llToday;
    @BindView(R.id.tv_tb_time)
    TextView tvTime;
    @BindView(R.id.rg_bottom)
    RadioGroup rgBottom;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.rl_run)
    RelativeLayout rlRun;
    @BindView(R.id.tv_tb_today)
    TextView tvTbToday;
    @BindView(R.id.rb_information)
    RadioButton rbInformation;
    @BindView(R.id.rb_today)
    RadioButton rbToday;
    @BindView(R.id.rb_run)
    RadioButton rbRun;
    @BindView(R.id.rb_more)
    RadioButton rbMore;
    private CommonPopupWindow menuPop;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuList;
    private Intent uploadHealthInfoIntent;
    private MyApplication application;
    private BleBTDeviceScanDialog bleBTDeviceScanDialog;
    private BleManager.OnConnectDismiss onConnectDismiss;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            retryConnect();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeSetContentView() {
        super.beforeSetContentView();
        toRequestPermission();
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getApplication();

        //--Firebase统计
        int currentHour = MyTimeUtils.getCurrentHour();
        Bundle bundle = new Bundle();
        if (currentHour < 6) {
            bundle.putString("time", "midnight");
        } else if (currentHour < 12) {
            bundle.putString("time", "morning");
        } else if (currentHour < 18) {
            bundle.putString("time", "afternoon");
        } else {
            bundle.putString("time", "evening");
        }
        FirebaseAnalytics.getInstance(this).logEvent("lsm01_open", bundle);

    }

    @Override
    protected void initView() {


        tvTime.setText("" + MyTimeUtils.formatDateTime("MM月dd日", new Date(System.currentTimeMillis()))
                + " " + MyTimeUtils.getCurrentWeek());

        vp_content.setOffscreenPageLimit(4);

        menuPop = new CommonPopupWindow(this,
                R.layout.menu_pop_list, DimensChange.dp2px(this, 120),
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                menuList = new ArrayList<>();
                menuList.add(new MenuItem(0, "设备连接"));
                menuList.add(new MenuItem(1, "设备信息"));
                menuList.add(new MenuItem(2, "退出登录"));
                View view = getContentView();
                RecyclerView rvMenu = (RecyclerView) view.findViewById(R.id.rv_menu);
                menuAdapter = new MenuAdapter(R.layout.menu_pop_item, menuList);
                rvMenu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rvMenu.setAdapter(menuAdapter);
            }

            @Override
            protected void initEvent() {
                menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        switch (position) {
                            case 0://设备连接
                                bleBTDeviceScanDialog = new BleBTDeviceScanDialog(MainActivity.this);
                                bleBTDeviceScanDialog.show();
                                //BleBTDeviceScanActivity.startAction(MainActivity.this);
                                break;
                            case 1://设备信息
                                DeviceInformationDialog deviceInformationDialog = new DeviceInformationDialog(MainActivity.this);
                                deviceInformationDialog.show();
                                break;
                            case 2://退出登录
                                LoginRegUtils.logout(MainActivity.this);
                                break;
                        }
                        mInstance.dismiss();
                        MyToast.showLong(MainActivity.this, menuList.get(position).getTitle());
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance = getPopupWindow();
                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getWindow().setAttributes(lp);
                    }
                });
            }
        };

        vp_content.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TabFragment.values()[position].fragment();
            }

            @Override
            public int getCount() {
                return TabFragment.values().length;
            }
        });
        vp_content.setCurrentItem(1, false);
    }

    @Override
    protected void initListener() {

        BleManager.getInstance().setOnConnectDismissListener(onConnectDismiss = new BleManager.OnConnectDismiss() {
            @Override
            public void dismiss(BleDevice device) {
                mHandler.sendEmptyMessageDelayed(0 , Config.BLE_RETRY_INTERVAL);
            }
        });

        rgBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioId) {
                switch (radioId) {
                    case R.id.rb_information:

                        //--统计
                        Bundle bundle = new Bundle();
                        bundle.putString("click", "heart");
                        FirebaseAnalytics.getInstance(MainActivity.this).logEvent("lsm01_test", bundle);

                        vp_content.setCurrentItem(0, false);
                        break;
                    case R.id.rb_today:
                        vp_content.setCurrentItem(1, false);
                        break;
                    case R.id.rb_run:
                        vp_content.setCurrentItem(2, false);
                        break;
                    case R.id.rb_more:
                        vp_content.setCurrentItem(3, false);
                        break;
                }
            }
        });

    }

    @Override
    protected void afterInit() {
        super.afterInit();
        uploadHealthInfoIntent = new Intent(this, UploadHealthInfoService.class);
        startService(uploadHealthInfoIntent);

        registerMessageReceiver();
    }


    private void retryConnect() {
        BleDevice bleDevice = application.getCurrentBleDevice();
        if (bleDevice == null){
            return;
        }
        String mac = bleDevice.getMac();
        String connectDeviceMac = BleBTUtils.getConnectDevice(MainActivity.this);
        if (!TextUtils.isEmpty(mac) && mac.equals(connectDeviceMac)) {//已经配对过的设备
            BleManager.getInstance().connectWrapper(bleDevice, new BleGattCallback() {
                @Override
                public void onStartConnect() {
                }

                @Override
                public void onConnectFail(BleException exception) {
                    if (Config.bleRetryTime > 0){
                        mHandler.sendEmptyMessageDelayed(0 ,  Config.BLE_RETRY_INTERVAL);
                    }
                    Config.bleRetryTime-- ;
                    //MyLog.e(TAG, "onConnectFail===" + exception.getDescription());
                    MyToast.showShort(MainActivity.this, "自動連接藍牙裝置失敗" + exception.getDescription());
                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    application.setCurrentBleDevice(bleDevice);
                    EventBus.getDefault().post(new BleConnectMessage(1, bleDevice));
                    MyActUtils.finishAct(BleBtDeviceDisconnectActivity.class);
                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    MyLog.d(TAG, "onDisConnected===");
                }
            });
        }
    }

    public void switchMenu(View view) {
        menuPop.showAsDropDown(ib_menu, 0, DimensChange.dp2px(this, 5));
    }

    private enum TabFragment {

        information(R.id.rb_information, InformationFragment.class),
        today(R.id.rb_today, TodayFragment.class),
        run(R.id.rb_run, RunFragment.class),
        setting(R.id.rb_more, ExerciseChoiceFragment.class);

        private Fragment fragment;
        private final int tabId;
        private final Class<? extends Fragment> clazz;

        TabFragment(@IdRes int tabId, Class<? extends Fragment> clazz) {
            this.tabId = tabId;
            this.clazz = clazz;
        }

        @NonNull
        public Fragment fragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static TabFragment from(int itemId) {
            for (TabFragment fragment : values()) {
                if (fragment.tabId == itemId) {
                    return fragment;
                }
            }
            return today;
        }

        public static void onDestroy() {
            for (TabFragment fragment : values()) {
                fragment.fragment = null;
            }
        }
    }

    @OnClick({R.id.iv_run_his})
    public void toRunRecordAct() {
        RunRecordActivity.startAction(this);
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(uploadHealthInfoIntent);
        //断开蓝牙设备
        // BleManager.getInstance().disconnect(application.getCurrentBleDevice());
        //移除监听
        BleManager.getInstance().removeConnectDismissListener(onConnectDismiss);
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    //按返回键退回主界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
