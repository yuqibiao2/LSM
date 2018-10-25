package com.test.lsm.ui.fragment.information_detail;

import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.lsm.R;
import com.test.lsm.ui.activity.AFibDetailActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;

import butterknife.BindView;

/**
 * 功能：AFib詳情頁面
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/24
 */
public class AFibDetailFragment extends LsmBaseFragment {

    @BindView(R.id.sc_auto_scan)
    SwitchCompat scAutoScan;
    @BindView(R.id.all_afib)
    AdapterLinearLayout allAfib;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_afib_detail;
    }

    @Override
    protected void initView() {
        allAfib.setAdapter(new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                return 5;
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.all_afib_item , parent,false);
                return item;
            }
        });
    }

    @Override
    protected void initListener() {
      allAfib.setOnItemClickListener(new AdapterLinearLayout.OnItemClickListener() {
          @Override
          public void onItemClick(int position) {
              AFibDetailActivity.startAction(getContext());
          }
      });
    }
}
