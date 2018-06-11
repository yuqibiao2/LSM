package com.test.lsm.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.lsm.R;
import com.test.lsm.ui.fragment.run_bottom.RunBottomFragment1;
import com.test.lsm.ui.fragment.run_bottom.RunBottomFragment2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends LsmBaseActivity {

    private List<Fragment> frgList;

    @BindView(R.id.vp_run_btm)
    ViewPager vpRunBtm;

    @Override
    public int getLayoutId() {

        return R.layout.activity_test;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        frgList = new ArrayList<>();
        frgList.add(new RunBottomFragment1());
        frgList.add(new RunBottomFragment2());
    }

    @Override
    protected void initView() {

        vpRunBtm.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return frgList.get(position);
            }

            @Override
            public int getCount() {
                return frgList.size();
            }
        });
    }

    @Override
    protected void initListener() {

    }
}
