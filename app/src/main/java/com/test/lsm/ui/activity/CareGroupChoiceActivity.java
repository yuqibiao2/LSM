package com.test.lsm.ui.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

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
    private BaseQuickAdapter<GetMonitorGroupReturn.DataBean, BaseViewHolder> adapter;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;
    private List<GetMonitorGroupReturn.DataBean> mData;
    private List<GetMonitorGroupReturn.DataBean> data;

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
                helper.setText(R.id.tv_care_group_name , item.getGroupName());
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
                if (code==200){
                    data = result.getData();
                    mData.addAll(data);
                    adapter.notifyDataSetChanged();
                }else {
                    MyToast.showLong(CareGroupChoiceActivity.this , "异常："+result.getMsg());
                }
                dismissLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(CareGroupChoiceActivity.this , ""+throwable.getMessage());
                dismissLoadDialog();
            }
        });

    }

    @Override
    protected void initListener() {

        //---搜索
        ibSearchEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CareGroupDetailActivity.startAction(CareGroupChoiceActivity.this , data.get(position).getGroupId());
            }
        });

    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CareGroupChoiceActivity.class);
        context.startActivity(intent);
    }


}
