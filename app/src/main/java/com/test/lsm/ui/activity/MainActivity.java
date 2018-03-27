package com.test.lsm.ui.activity;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.test.lsm.R;
import com.test.lsm.adapter.MenuAdapter;
import com.test.lsm.bean.MenuItem;
import com.test.lsm.ui.dialog.DeviceInformationDialog;
import com.test.lsm.ui.fragment.InformationFragment;
import com.test.lsm.ui.fragment.RunFragment;
import com.test.lsm.ui.fragment.TodayFragment;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.StatusBarCompat;
import com.yyyu.baselibrary.view.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：首页
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */
public class MainActivity extends LsmBaseActivity {

    @BindView(R.id.ll_top_bar)
    RelativeLayout ll_top_bar;
    @BindView(R.id.ib_menu)
    ImageButton ib_menu;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    private CommonPopupWindow menuPop;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        StatusBarCompat.compat(this, 0xfff0f0f0);

        menuPop = new CommonPopupWindow(this, R.layout.menu_pop_list, DimensChange.dp2px(this , 120), ViewGroup.LayoutParams.WRAP_CONTENT) {
            @Override
            protected void initView() {
                menuList = new ArrayList<>();
                menuList.add(new MenuItem(0 , "设备连接"));
                menuList.add(new MenuItem(1 , "设备信息"));
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
                        switch (position){
                            case 0://设备连接
                                break;
                            case 1://设备信息
                                DeviceInformationDialog deviceInformationDialog = new DeviceInformationDialog(MainActivity.this);
                                deviceInformationDialog.show();
                                break;
                        }
                        MyToast.showLong(MainActivity.this , menuList.get(position).getTitle());
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

    }



    public void switchMenu(View view){
        menuPop.showAsDropDown(ib_menu, 0, DimensChange.dp2px(this , 5));
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
                break;
            case R.id.iv_today:
                vp_content.setCurrentItem(1);
                setTabIcon(ll_bottom, R.id.iv_today);
                break;
            case R.id.iv_run:
                vp_content.setCurrentItem(2);
                setTabIcon(ll_bottom, R.id.iv_run);
                break;
        }
    }

    private void setTabIcon(LinearLayout viewGroup, int clickId) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            int id = child.getId();
            if (clickId == id) {//当前点击的按钮
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                linearParams.width = DimensChange.dp2px(this, 80);
                linearParams.height = DimensChange.dp2px(this, 80);
                child.setLayoutParams(linearParams);
            } else {
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                linearParams.width = DimensChange.dp2px(this, 40);
                linearParams.height = DimensChange.dp2px(this, 40);
                child.setLayoutParams(linearParams);
            }
        }
    }


}
