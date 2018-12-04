package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.adapter.ExpMemInfoAdapter;
import com.test.lsm.adapter.NorMemAdapter;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.care_group.MemLocationShowFragment;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyKeyboardUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心群组详情
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class CareGroupDetailActivity extends LsmBaseActivity {

    private static final String TAG = "CareGroupDetailActivity";

    @BindView(R.id.ib_search_enter)
    ImageButton ibSearchEnter;
    @BindView(R.id.rl_search_expand)
    RelativeLayout rlSearchExpand;
    @BindView(R.id.rv_cg_detail_nor)
    RecyclerView rvCgDetailNor;
    @BindView(R.id.tv_mask)
    TextView tvMask;
    @BindView(R.id.rl_bottom_sheet)
    RelativeLayout rlBottomSheet;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.fl_map)
    FrameLayout flMap;
    @BindView(R.id.iv_search_fold)
    ImageView ivSearchFold;


    private BaseQuickAdapter<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean, BaseViewHolder> norMemAdapter;
    private LinearLayoutManager norLayoutManager;
    private View header1;
    private APIMethodManager apiMethodManager;
    private long groupId;
    private List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> norMemInfoList;
    private MemLocationShowFragment memLocationShowFragment;
    private AdapterLinearLayout allExpMem;
    ExpMemInfoAdapter expMemInfoAdapter;
    private List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> memInfoList;
    private List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> expMemInfoList;
    private Gson mGson;

    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        mGson = new Gson();
        groupId = getIntent().getLongExtra("groupId", -1);
        apiMethodManager = APIMethodManager.getInstance();
        norMemInfoList = new ArrayList<>();
    }

    @Override
    protected void initView() {

        MyKeyboardUtils.hidden(this);

        //初始化地图展示fragment
        memLocationShowFragment = new MemLocationShowFragment();
        replaceFrg(R.id.fl_map, memLocationShowFragment);

        header1 = LayoutInflater.from(this).inflate(R.layout.pt_gc_detail_rv_header1, null);
        View header2 = LayoutInflater.from(this).inflate(R.layout.pt_gc_detail_rv_header2, null);
        norLayoutManager = new LinearLayoutManager(this);
        rvCgDetailNor.setLayoutManager(norLayoutManager);
        norMemAdapter = new NorMemAdapter(R.layout.rv_cg_detail_nor_item, norMemInfoList);
        norMemAdapter.addHeaderView(header1);
        norMemAdapter.addHeaderView(header2);
        rvCgDetailNor.setAdapter(norMemAdapter);
        allExpMem = header1.findViewById(R.id.all_cg_detail_exp);
        LinearLayoutManager expLayoutManager = new LinearLayoutManager(this);
        expLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        int[] size = WindowUtils.getSize(this);
        llTop.measure(0, 0);
        int maxHeight = size[1] - llTop.getMeasuredHeight() - DimensChange.dp2px(this, 28);
        rlBottomSheet.getLayoutParams().height = maxHeight;

    }

    @Override
    protected void initListener() {

        //---搜索展开
        ivSearchFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSearchExpand.setVisibility(View.VISIBLE);
                ivSearchFold.setVisibility(View.GONE);
            }
        });

        //---搜索折叠
        ibSearchEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlSearchExpand.setVisibility(View.GONE);
                ivSearchFold.setVisibility(View.VISIBLE);
            }
        });

        allExpMem.setOnItemClickListener(new AdapterLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean expMemInfo = expMemInfoList.get(position);
                CareGroupMemDetailActivity.startAction(CareGroupDetailActivity.this, mGson.toJson(expMemInfo));
            }
        });

        //--正常Meme
        norMemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo = memInfoList.get(position);
                CareGroupMemDetailActivity.startAction(CareGroupDetailActivity.this, mGson.toJson(memInfo));
            }
        });

        //---recycleView滑动
        rvCgDetailNor.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int scrollY = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY += dy;
                if (scrollY >= header1.getMeasuredHeight()) {
                    tvMask.setVisibility(View.VISIBLE);
                } else {
                    tvMask.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        showLoadDialog();
        apiMethodManager.getMonitorGroupDetail(provider, groupId, new IRequestCallback<GetMonitorGroupDetailReturn>() {
            @Override
            public void onSuccess(GetMonitorGroupDetailReturn result) {
                int code = result.getCode();
                if (code == 200) {
                    GetMonitorGroupDetailReturn.DataBean data = result.getData();
                    memInfoList = data.getMemInfoList();
                    norMemInfoList.addAll(memInfoList);
                    norMemAdapter.notifyDataSetChanged();
                    memLocationShowFragment.inflateMemInfo(memInfoList);
                    expMemInfoList = data.getExpMemInfoList();
                    expMemInfoAdapter = new ExpMemInfoAdapter(CareGroupDetailActivity.this, expMemInfoList);
                    allExpMem.setAdapter(expMemInfoAdapter);
                } else {
                    MyToast.showLong(CareGroupDetailActivity.this, "" + result.getMsg());
                }
                dismissLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(CareGroupDetailActivity.this, "" + throwable.getMessage());
                dismissLoadDialog();
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context, Long groupId) {
        Intent intent = new Intent(context, CareGroupDetailActivity.class);
        intent.putExtra("groupId", groupId);
        context.startActivity(intent);
    }


}
