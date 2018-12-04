package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.bean.json.GetMonitorGroupMemDetailReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.care_group.CareGroupMemHRRecordFragment;
import com.test.lsm.ui.fragment.care_group.CareGroupMemHRVFragment;
import com.test.lsm.ui.fragment.care_group.CareGroupMemRunTraceFragment;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyToast;

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
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_hr)
    TextView tvHr;
    @BindView(R.id.tv_calorie)
    TextView tvCalorie;
    @BindView(R.id.tv_step)
    TextView tvStep;
    @BindView(R.id.riv_user_icon)
    RoundImageView rivUserIcon;
    @BindView(R.id.pb_mem)
    ProgressBar pbMem;
    @BindView(R.id.iv_tag)
    ImageView ivTag;

    private List<Fragment> pageList;
    private GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo;
    private APIMethodManager apiMethodManager;
    private CareGroupMemHRVFragment careGroupMemHRVFragment;
    private CareGroupMemHRRecordFragment careGroupMemHRRecordFragment;
    private CareGroupMemRunTraceFragment careGroupMemRunTraceFragment;


    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_mem_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        apiMethodManager = APIMethodManager.getInstance();
        Intent intent = getIntent();
        String memInfoJsonStr = intent.getStringExtra("memInfoJsonStr");
        memInfo = new Gson().fromJson(memInfoJsonStr, GetMonitorGroupDetailReturn.DataBean.MemInfoListBean.class);
    }

    @Override
    protected void initView() {
        vpCgMem.setOffscreenPageLimit(3);
        pageList = new ArrayList<>();
        careGroupMemHRVFragment = new CareGroupMemHRVFragment();
        careGroupMemHRRecordFragment = new CareGroupMemHRRecordFragment();
        careGroupMemRunTraceFragment = new CareGroupMemRunTraceFragment();
        pageList.add(careGroupMemHRVFragment);
        pageList.add(careGroupMemHRRecordFragment);
        pageList.add(careGroupMemRunTraceFragment);
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

        GlidUtils.load(this, rivUserIcon, memInfo.getUserImage());
        tvUserName.setText("" + memInfo.getUserName());
        tvTel.setText("" + memInfo.getPhone());
        tvHr.setText("" + memInfo.getHeartNum() + " bpm");
        tvCalorie.setText("" + memInfo.getCalorieValue() + " 千卡");
        tvStep.setText("" + memInfo.getStepNum() + " 步");
        String watchingTag = memInfo.getWatchingTag();
        int icTag = R.mipmap.ic_mon_mark_blue;
        switch (watchingTag){
            case "1"://blue
                icTag = R.mipmap.ic_mon_mark_blue;
                break;
            case "2"://green
                icTag = R.mipmap.ic_mon_mark_green;
                break;
            case "3"://yellow
                icTag = R.mipmap.ic_mon_mark_yellow;
                break;
            case "4"://purple
                icTag = R.mipmap.ic_mon_mark_purple;
                break;
            case "5"://red
                icTag =R.mipmap.ic_mon_mark_red;
                break;
        }
        ivTag.setImageResource(icTag);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();

        apiMethodManager.getMonitorGroupMemDetail(provider, memInfo.getUserId(), new IRequestCallback<GetMonitorGroupMemDetailReturn>() {
            @Override
            public void onSuccess(GetMonitorGroupMemDetailReturn result) {
                int code = result.getCode();
                if (code == 200) {
                    GetMonitorGroupMemDetailReturn.DataBean data = result.getData();
                    GetMonitorGroupMemDetailReturn.DataBean.HrvInfoBean hrvInfo = data.getHrvInfo();
                    careGroupMemHRVFragment.inflateHrv(hrvInfo);
                    List<GetMonitorGroupMemDetailReturn.DataBean.HeartInfoListBean> heartInfoList = data.getHeartInfoList();
                    careGroupMemHRRecordFragment.inflateLineChart(heartInfoList);
                    GetMonitorGroupMemDetailReturn.DataBean.TraceInfoBean traceInfo = data.getTraceInfo();
                    careGroupMemRunTraceFragment.inflateTraceInfo(traceInfo);
                } else {
                    MyToast.showLong(CareGroupMemDetailActivity.this, "" + result.getMsg());
                }
                pbMem.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(CareGroupMemDetailActivity.this, "異常：" + throwable.getMessage());
                pbMem.setVisibility(View.GONE);
            }
        });
    }

    public void close(View view) {
        finish();
    }

    public static void startAction(Context context, String memInfoJsonStr) {
        Intent intent = new Intent(context, CareGroupMemDetailActivity.class);
        intent.putExtra("memInfoJsonStr", memInfoJsonStr);
        context.startActivity(intent);
    }


}
