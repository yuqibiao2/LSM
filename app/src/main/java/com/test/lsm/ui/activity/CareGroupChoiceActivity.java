package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyKeyboardUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心群组选择
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class CareGroupChoiceActivity extends LsmBaseActivity {

    @BindView(R.id.ib_search_enter)
    ImageButton ibSearchEnter;
    @BindView(R.id.rv_care_group)
    RecyclerView rvCareGroup;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ib_nav_back)
    ImageButton ibNavBack;
    @BindView(R.id.iv_search_fold)
    ImageView ivSearchFold;
    @BindView(R.id.rl_search_expand)
    RelativeLayout rlSearchExpand;
    private BaseQuickAdapter<GetMonitorGroupReturn.DataBean, BaseViewHolder> adapter;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;
    private List<GetMonitorGroupReturn.DataBean> mData;

    @Override
    public int getLayoutId() {
        return R.layout.activity_care_group_choice;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        mData = new ArrayList<>();
        apiMethodManager = APIMethodManager.getInstance();
        MyApplication application = (MyApplication) getApplication();
        user = application.getUser();
    }

    @Override
    protected void initView() {
        rvCareGroup.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<GetMonitorGroupReturn.DataBean, BaseViewHolder>(R.layout.rv_care_group_show_item, mData) {
            @Override
            protected void convert(BaseViewHolder helper, GetMonitorGroupReturn.DataBean item) {
                helper.setText(R.id.tv_care_group_name, Html.fromHtml(item.getGroupName()));
            }
        };
        rvCareGroup.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        super.initData();
        showLoadDialog();
        apiMethodManager.getMonitorGroups(provider, user.getUSER_ID(), 1, new IRequestCallback<GetMonitorGroupReturn>() {
            @Override
            public void onSuccess(GetMonitorGroupReturn result) {
                int code = result.getCode();
                if (code == 200) {
                    List<GetMonitorGroupReturn.DataBean> data = result.getData();
                    mData.addAll(data);
                    adapter.notifyDataSetChanged();
                } else {
                    MyToast.showLong(CareGroupChoiceActivity.this, "异常：" + result.getMsg());
                }
                dismissLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(CareGroupChoiceActivity.this, "" + throwable.getMessage());
                dismissLoadDialog();
            }
        });

    }

    @Override
    protected void initListener() {

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    MyKeyboardUtils.hidden(CareGroupChoiceActivity.this);
                    String keyWord = v.getText().toString();
                    locationAndReplaceKeyWord(keyWord);
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

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String groupName = mData
                        .get(position).getGroupName()
                        .replace("<font color = '#F13564'>", "")
                        .replace("</font>" , "");;

                CareGroupDetailActivity.startAction(CareGroupChoiceActivity.this,
                        mData.get(position).getGroupId()
                        ,groupName);
            }
        });

    }

    /**
     * 定位、替换搜索关键词
     *
     * @param keyWord
     */
    private void locationAndReplaceKeyWord(String keyWord) {
        int firstMatchIndex = -1;
        //恢复所有颜色
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupReturn.DataBean dataBean = mData.get(i);
            String groupName = dataBean.getGroupName();
            if (groupName.contains("</font>")){
                String replace = groupName.replace("<font color = '#F13564'>", "")
                        .replace("</font>" , "");
                mData.get(i).setGroupName(replace);
            }
        }
        //替换关键词
        for (int i = 0; i < mData.size(); i++) {
            GetMonitorGroupReturn.DataBean dataBean = mData.get(i);
            String groupName = dataBean.getGroupName();
            if (groupName.contains(keyWord)){
                if (firstMatchIndex ==-1){
                    firstMatchIndex = i;
                }
                String keyWordReplace ="<font color = '#F13564'>"+keyWord+"</font>";
                String replacedGroupName = groupName.replace(keyWord, keyWordReplace);
                mData.get(i).setGroupName(replacedGroupName);
            }
        }
        if (firstMatchIndex != -1){
            rvCareGroup.smoothScrollToPosition(firstMatchIndex);
            adapter.notifyDataSetChanged();
        }
    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CareGroupChoiceActivity.class);
        context.startActivity(intent);
    }


}
