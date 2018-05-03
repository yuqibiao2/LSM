package com.test.lsm.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.MenuAdapter;
import com.test.lsm.bean.BleConnectMessage;
import com.test.lsm.bean.MenuItem;
import com.test.lsm.service.CheckBleIsConnectService;
import com.test.lsm.service.UploadHealthInfoService;
import com.test.lsm.ui.dialog.BleBTDeviceScanDialog;
import com.test.lsm.ui.dialog.DeviceInformationDialog;
import com.test.lsm.ui.fragment.InformationFragment;
import com.test.lsm.ui.fragment.RunFragment;
import com.test.lsm.ui.fragment.TodayFragment;
import com.test.lsm.utils.LoginRegUtils;
import com.test.lsm.utils.bt.ble.BleBTUtils;
import com.yyyu.baselibrary.ui.widget.CommonPopupWindow;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

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
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.ll_today)
    LinearLayout llToday;
    @BindView(R.id.tv_tb_time)
    TextView tvTime;
    @BindView(R.id.tv_tb_info)
    ImageView tvTbInfo;
    @BindView(R.id.iv_today)
    ImageView ivToday;
    @BindView(R.id.iv_run)
    ImageView ivRun;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.rl_run)
    RelativeLayout rlRun;
    private CommonPopupWindow menuPop;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuList;
    private Intent uploadHealthInfoIntent;
    private MyApplication application;
    private BleBTDeviceScanDialog bleBTDeviceScanDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getApplication();
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
        vp_content.setCurrentItem(1);
    }

    @Override
    protected void initListener() {

        vp_content.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int tabId = TabFragment.values()[position].tabId;
                setTabIcon(ll_bottom, tabId);
            }
        });

        BleManager.getInstance().setOnConnectDismissListener(new BleManager.OnConnectDismiss() {
            @Override
            public void dismiss(BleDevice bleDevice) {

                retryConnect(bleDevice);


                /*new AlertDialog.Builder(MainActivity.this)
                        .setTitle("蓝牙断开")
                        .setMessage("是否需要重新连接蓝牙设备？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BleBTDeviceScanDialog bleBTDeviceScanDialog = new BleBTDeviceScanDialog(MainActivity.this);
                                bleBTDeviceScanDialog.show();
                            }
                        })
                        *//*.setNeutralButton("不再检测", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })*//*
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();*/
            }
        });

    }

    private void retryConnect(BleDevice bleDevice) {
        String mac = bleDevice.getMac();
        String connectDeviceMac = BleBTUtils.getConnectDevice(MainActivity.this);
        if (!TextUtils.isEmpty(mac) && mac.equals(connectDeviceMac)) {//已经配对过的设备
            BleManager.getInstance().connectWapper(bleDevice, new BleGattCallback() {
                @Override
                public void onStartConnect() {
                    MyLog.d(TAG, "onStartConnect===");
                }

                @Override
                public void onConnectFail(BleException exception) {
                    BleDevice currentBleDevice = application.getCurrentBleDevice();
                    if (currentBleDevice!=null){
                        retryConnect(currentBleDevice);
                    }
                    MyLog.e(TAG, "onConnectFail===" + exception.getDescription());
                    MyToast.showShort(MainActivity.this, "连接失败" + exception.getDescription());
                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    application.setCurrentBleDevice(bleDevice);
                    EventBus.getDefault().post(new BleConnectMessage(1, bleDevice));
                    MyLog.d(TAG, "onConnectSuccess===");
                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    MyLog.d(TAG, "onDisConnected===");
                }
            });
        }
    }

    @Override
    protected void afterInit() {
        super.afterInit();
        uploadHealthInfoIntent = new Intent(this, UploadHealthInfoService.class);
        startService(uploadHealthInfoIntent);
    }

    public void switchMenu(View view) {
        menuPop.showAsDropDown(ib_menu, 0, DimensChange.dp2px(this, 5));
    }

    private enum TabFragment {

        information(R.id.iv_information, InformationFragment.class),
        today(R.id.iv_today, TodayFragment.class),
        run(R.id.iv_run, RunFragment.class);

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

    public void switchTab(View view) {
        switch (view.getId()) {
            case R.id.iv_information:
                vp_content.setCurrentItem(0);
                setTabIcon(ll_bottom, R.id.iv_information);
                llToday.setVisibility(View.GONE);
                tvTbInfo.setVisibility(View.VISIBLE);
                rlRun.setVisibility(View.GONE);
                break;
            case R.id.iv_today:
                vp_content.setCurrentItem(1);
                setTabIcon(ll_bottom, R.id.iv_today);
                llToday.setVisibility(View.VISIBLE);
                tvTbInfo.setVisibility(View.GONE);
                rlRun.setVisibility(View.GONE);
                break;
            case R.id.iv_run:
                vp_content.setCurrentItem(2);
                setTabIcon(ll_bottom, R.id.iv_run);
                llToday.setVisibility(View.GONE);
                tvTbInfo.setVisibility(View.GONE);
                rlRun.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setTabIcon(LinearLayout viewGroup, int clickId) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int id = child.getId();
            if (clickId == id) {//当前点击的按钮
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                linearParams.width = DimensChange.dp2px(this, 60);
                linearParams.height = DimensChange.dp2px(this, 60);
                child.setLayoutParams(linearParams);
            } else {
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                linearParams.width = DimensChange.dp2px(this, 40);
                linearParams.height = DimensChange.dp2px(this, 40);
                child.setLayoutParams(linearParams);
            }
        }
    }

    @OnClick({R.id.iv_run_his})
    public void toRunRecordAct() {
        RunRecordActivity.startAction(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(uploadHealthInfoIntent);
        //断开蓝牙设备
        BleManager.getInstance().disconnect(application.getCurrentBleDevice());
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
