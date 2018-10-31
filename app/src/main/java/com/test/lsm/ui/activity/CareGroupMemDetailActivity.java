package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.lsm.R;
import com.test.lsm.ui.fragment.care_group.CareGroupMemHRRecordFragment;
import com.test.lsm.ui.fragment.care_group.CareGroupMemHRVFragment;
import com.test.lsm.ui.fragment.care_group.CareGroupMemRunTraceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

/**
 * 功能：关心群组成员详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/29
 */
public class CareGroupMemDetailActivity extends LsmBaseActivity {


    @BindView(R.id.vp_cg_mem)
    ViewPager vpCgMem;
    @BindView(R.id.ci_cg_mem)
    CircleIndicator ciCgMem;

    private List<Fragment> pageList;


    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_mem_detail;
    }

    @Override
    protected void initView() {
        vpCgMem.setOffscreenPageLimit(3);
        pageList = new ArrayList<>();
        pageList.add(new CareGroupMemHRVFragment());
        pageList.add(new CareGroupMemHRRecordFragment());
        pageList.add(new CareGroupMemRunTraceFragment());
        vpCgMem.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return pageList.get(position);
            }

            @Override
            public int getCount() {
                return pageList.size();
            }
        });
        ciCgMem.setViewPager(vpCgMem);
    }

    @Override
    protected void initListener() {

    }

    public static void startAction(Context context){
        Intent intent = new Intent(context , CareGroupMemDetailActivity.class);
        context.startActivity(intent);
    }


}
