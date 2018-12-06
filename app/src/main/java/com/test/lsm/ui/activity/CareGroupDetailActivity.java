package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.test.lsm.adapter.ExpMemAdapter;
import com.test.lsm.adapter.ExpMemInfoAdapter;
import com.test.lsm.adapter.NorMemAdapter;
import com.test.lsm.bean.event.UpdateMemData;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.bean.json.GetMonitorGroupReturn;
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
import de.greenrobot.event.EventBus;

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
    @BindView(R.id.tv_title)
    TextView tvTitle;


    private BaseQuickAdapter<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean, BaseViewHolder> norMemAdapter;
    private LinearLayoutManager norLayoutManager;
    private View header1;
    private APIMethodManager apiMethodManager;
    private long groupId;
    private List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> norMemInfoList;
    private MemLocationShowFragment memLocationShowFragment;
    private List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> memInfoList;
    private List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> expMemInfoList = new ArrayList<>();
    private Gson mGson;
    private String groupName;
    private RecyclerView rvExpMem;
    private ExpMemAdapter expMemAdapter;
    private LinearLayoutManager expLayoutManager;
    private int currentMemIndex = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getData();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        mGson = new Gson();
        Intent intent = getIntent();
        groupId =intent.getLongExtra("groupId", -1);
        groupName = intent.getStringExtra("groupName");
        apiMethodManager = APIMethodManager.getInstance();
        norMemInfoList = new ArrayList<>();
    }

    @Override
    protected void initView() {

        MyKeyboardUtils.hidden(this);

        tvTitle.setText(groupName);

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
        //allExpMem = header1.findViewById(R.id.all_cg_detail_exp);
        rvExpMem = header1.findViewById(R.id.rv_exp_mem);
        expLayoutManager = new LinearLayoutManager(this);
        expLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvExpMem.setLayoutManager(expLayoutManager);
        expMemAdapter = new ExpMemAdapter(R.layout.rv_cg_detail_exp_item, expMemInfoList);
        rvExpMem.setAdapter(expMemAdapter);

        int[] size = WindowUtils.getSize(this);
        llTop.measure(0, 0);
        int maxHeight = size[1] - llTop.getMeasuredHeight() - DimensChange.dp2px(this, 28);
        rlBottomSheet.getLayoutParams().height = maxHeight;
    }

    @Override
    protected void initListener() {

        //---搜索
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    MyKeyboardUtils.hidden(CareGroupDetailActivity.this);
                    String keyWord = v.getText().toString();
                    locationAndReplaceKeyWord(keyWord  , norMemInfoList);
                    locationAndReplaceKeyWordExp(keyWord  , expMemInfoList);
                    //expLayoutManager.scrollToPosition(0);
                    return true;
                }
                return false;
            }
        });

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

        //异常mem点击
        expMemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean expMemInfo = expMemInfoList.get(position);
                expMemInfo.setUserName(expMemInfo.getUserName()
                        .replace("<font color = '#F13564'>", "")
                        .replace("</font>" , ""));
                CareGroupMemDetailActivity.startAction(CareGroupDetailActivity.this, mGson.toJson(expMemInfo));
            }
        });

        //--正常Meme
        norMemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo = memInfoList.get(position);
                memInfo.setUserName(memInfo.getUserName()
                        .replace("<font color = '#F13564'>", "")
                        .replace("</font>" , ""));
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
        apiMethodManager.getMonitorGroupDetail(provider, groupId,
                new IRequestCallback<GetMonitorGroupDetailReturn>() {
            @Override
            public void onSuccess(GetMonitorGroupDetailReturn result) {
                int code = result.getCode();
                if (code == 200) {
                    GetMonitorGroupDetailReturn.DataBean data = result.getData();
                    memInfoList = data.getMemInfoList();
                    norMemInfoList.addAll(memInfoList);
                    norMemAdapter.notifyDataSetChanged();
                    memLocationShowFragment.inflateMemInfo(memInfoList);
                    List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> expData = data.getExpMemInfoList();
                    expMemInfoList.addAll(expData);
                    expMemAdapter.notifyDataSetChanged();

                    mHandler.sendEmptyMessageDelayed(0 , 10*1000);

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

    private void getData(){
        mHandler.sendEmptyMessageDelayed(0 , 10*1000);
        //发送消息让CareGroupMemDetailActivity更新数据
        EventBus.getDefault().post(new UpdateMemData());
        apiMethodManager.getMonitorGroupDetail(provider, groupId,
                new IRequestCallback<GetMonitorGroupDetailReturn>() {
                    @Override
                    public void onSuccess(GetMonitorGroupDetailReturn result) {
                        int code = result.getCode();
                        if (code == 200) {
                            GetMonitorGroupDetailReturn.DataBean data = result.getData();
                            memInfoList = data.getMemInfoList();
                            norMemInfoList.clear();
                            norMemInfoList.addAll(memInfoList);
                            norMemAdapter.notifyDataSetChanged();
                            //地图
                            memLocationShowFragment.inflateMemInfo(memInfoList);
                            List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> expData = data.getExpMemInfoList();
                            expMemInfoList.clear();
                            expMemInfoList.addAll(expData);
                            expMemAdapter.notifyDataSetChanged();
                        } else {
                            MyToast.showLong(CareGroupDetailActivity.this, "" + result.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        MyToast.showLong(CareGroupDetailActivity.this, "" + throwable.getMessage());
                    }
                });
    }


    /**
     * 定位、替换关键词
     *
     * @param keyWord
     * @param mData
     */
    private void locationAndReplaceKeyWord(String keyWord
            , List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> mData) {
        int firstMatchIndex = -1;
        //恢复所有颜色
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupDetailReturn.DataBean.MemInfoListBean dataBean = mData.get(i);
            String groupName = dataBean.getUserName();
            if (groupName.contains("</font>")){
                String replace = groupName.replace("<font color = '#F13564'>", "")
                        .replace("</font>" , "");
                mData.get(i).setUserName(replace);
            }
        }
        //替换关键词
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupDetailReturn.DataBean.MemInfoListBean dataBean = mData.get(i);
            String groupName = dataBean.getUserName();
            if (groupName.contains(keyWord)){
                if (firstMatchIndex ==-1){
                    firstMatchIndex = i;
                }
                String keyWordReplace ="<font color = '#F13564'>"+keyWord+"</font>";
                String replacedGroupName = groupName.replace(keyWord, keyWordReplace);
                mData.get(i).setUserName(replacedGroupName);
            }
        }
        if (firstMatchIndex != -1){
            rvCgDetailNor.smoothScrollToPosition(firstMatchIndex);
            norMemAdapter.notifyDataSetChanged();
        }
    }

    private void locationAndReplaceKeyWordExp(String keyWord
            , List<GetMonitorGroupDetailReturn.DataBean.ExpMemInfoListBean> mData) {
        int firstMatchIndex = -1;
        //恢复所有颜色
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupDetailReturn.DataBean.MemInfoListBean dataBean = mData.get(i);
            String groupName = dataBean.getUserName();
            if (groupName.contains("</font>")){
                String replace = groupName.replace("<font color = '#F13564'>", "")
                        .replace("</font>" , "");
                mData.get(i).setUserName(replace);
            }
        }
        //替换关键词
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupDetailReturn.DataBean.MemInfoListBean dataBean = mData.get(i);
            String groupName = dataBean.getUserName();
            if (groupName.contains(keyWord)){
                if (firstMatchIndex ==-1){
                    firstMatchIndex = i;
                }
                String keyWordReplace ="<font color = '#F13564'>"+keyWord+"</font>";
                String replacedGroupName = groupName.replace(keyWord, keyWordReplace);
                mData.get(i).setUserName(replacedGroupName);
            }
        }
        if (firstMatchIndex != -1){
            expLayoutManager.scrollToPosition(firstMatchIndex);
            expMemAdapter.notifyDataSetChanged();
        }
    }


    public void back(View view) {
        finish();
    }

    public static void startAction(Context context, Long groupId, String groupName) {
        Intent intent = new Intent(context, CareGroupDetailActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        context.startActivity(intent);
    }


}
