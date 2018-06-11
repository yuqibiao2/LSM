package com.test.lsm.ui.dialog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.test.lsm.R;
import com.test.lsm.ui.fragment.run_bottom.RunBottomFragment1;
import com.test.lsm.ui.fragment.run_bottom.RunBottomFragment2;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：跑步页底部
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/29
 */
public class RunBottomDialog extends LsmBaseBottomDialog {

    private List<Fragment> frgList;

    public RunBottomDialog(Context context) {
        super(context);
    }

    public RunBottomDialog(Context context, int peekHeight, int maxHeight) {
        super(context, peekHeight, maxHeight);
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        frgList = new ArrayList<>();
        frgList.add(new RunBottomFragment1());
        frgList.add(new RunBottomFragment2());


    }

    @Override
    public int getLayoutId() {

        return R.layout.pt_run_bottom;
    }

    @Override
    protected void initView() {

        ViewPager vpRunBtm = getView(R.id.vp_run_btm);

        vpRunBtm.setAdapter(new FragmentPagerAdapter(((FragmentActivity) mContext).getSupportFragmentManager()) {
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
}
