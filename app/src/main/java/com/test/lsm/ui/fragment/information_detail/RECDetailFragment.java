package com.test.lsm.ui.fragment.information_detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.ui.activity.RECDetailActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：REC詳情頁面
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/24
 */
public class RECDetailFragment extends LsmBaseFragment {

    @BindView(R.id.sp_time_interval)
    Spinner spTimeInterval;
    @BindView(R.id.iv_rec)
    ImageView ivRec;
    @BindView(R.id.tv_rec_tip)
    TextView tvRecTip;
    @BindView(R.id.iv_sny)
    ImageView ivSny;
    @BindView(R.id.iv_sny_tip)
    TextView ivSnyTip;
    @BindView(R.id.all_rec)
    AdapterLinearLayout allRec;

    private List<String> timeIntervalList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rec_detail;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        timeIntervalList = new ArrayList<>();
        timeIntervalList.add("30分鐘");
        timeIntervalList.add("45分鐘");
        timeIntervalList.add("60分鐘");
        timeIntervalList.add("85分鐘");
    }

    @Override
    protected void initView() {

        allRec.setAdapter(new AdapterLinearLayout.LinearAdapter() {
            @Override
            public int getItemCount() {
                return 5;
            }

            @Override
            public View getView(ViewGroup parent, int position) {
                return LayoutInflater.from(getContext()).inflate(R.layout.all_rec_item , parent , false);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeIntervalList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTimeInterval.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

        allRec.setOnItemClickListener(new AdapterLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RECDetailActivity.startAction(getContext());
            }
        });

    }


}
