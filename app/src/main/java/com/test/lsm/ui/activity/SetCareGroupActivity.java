package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.adapter.UserMonitorsAdapter;
import com.test.lsm.bean.json.DoFooBean;
import com.test.lsm.bean.json.EmptyDataReturn;
import com.test.lsm.bean.json.GetUserMonitorsReturn;
import com.test.lsm.bean.json.QueryUserRunInfoReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.vo.GroupAttach;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.dialog.AddGroupAttachDialog;
import com.yyyu.baselibrary.ui.widget.SwitchCompatWrapper;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心人员
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/25
 */
public class SetCareGroupActivity extends LsmBaseActivity {

    @BindView(R.id.ib_nav_back)
    ImageButton ibNavBack;
    @BindView(R.id.rv_care_group)
    RecyclerView rvCareGroup;
    @BindView(R.id.iv_add_care_group)
    ImageView ivAddCareGroup;
    @BindView(R.id.sc_watch_all)
    SwitchCompat scWatchAll;
    private List<GetUserMonitorsReturn.DataBean.MonitorInfoListBean> monitorsList = new ArrayList<>();
    private UserMonitorsAdapter userMonitorsAdapter;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;
    private AddGroupAttachDialog addGroupAttachDialog;
    private SwitchCompatWrapper switchCompatWrapper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_care_group;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        MyApplication application = (MyApplication) getApplication();
        user = application.getUser();
        apiMethodManager = APIMethodManager.getInstance();
        switchCompatWrapper = new SwitchCompatWrapper(scWatchAll);
    }

    @Override
    protected void initView() {
        rvCareGroup.setLayoutManager(new LinearLayoutManager(this));
        userMonitorsAdapter = new UserMonitorsAdapter(R.layout.rv_care_group_item, monitorsList , provider);
        rvCareGroup.setAdapter(userMonitorsAdapter);
    }

    @Override
    protected void initListener() {

        //---删除监听人
        userMonitorsAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new AlertDialog.Builder(SetCareGroupActivity.this)
                        .setTitle(getStr(R.string.operate))
                        .setMessage("確認要刪除該監聽人嗎？")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                long attachId = monitorsList.get(position).getAttachId();
                                deleteListener(attachId);
                            }
                        })
                        .setNegativeButton(getStr(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        });

        //---添加监听人
        ivAddCareGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroupAttachDialog = new AddGroupAttachDialog(SetCareGroupActivity.this, user.getUSER_ID());
                addGroupAttachDialog.show();
            }
        });

        //全选
        switchCompatWrapper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){//全选
                    switchAll(true , monitorsList);
                }else{//全不选
                    switchAll(false , monitorsList);
                }
            }
        });
    }

    private void switchAll(boolean check , List<GetUserMonitorsReturn.DataBean.MonitorInfoListBean> monitorsList){
        List<GroupAttach> groupAttachList = new ArrayList<>();
        for (GetUserMonitorsReturn.DataBean.MonitorInfoListBean  dataBean : monitorsList){
            GroupAttach groupAttach = new GroupAttach();
            groupAttach.setAttachId(dataBean.getAttachId());
            if (check){
                groupAttach.setIsWatching("1");
            }else{
                groupAttach.setIsWatching("0");
            }
            groupAttachList.add(groupAttach);
        }
        apiMethodManager.modifyGroupAttachStatus(provider, groupAttachList, new IRequestCallback<EmptyDataReturn>() {
            @Override
            public void onSuccess(EmptyDataReturn result) {
                int code = result.getCode();
                if (code == 200){
                    inflateData();
                }else{
                    MyToast.showLong(SetCareGroupActivity.this , ""+result.getMsg());
                    switchCompatWrapper.setCheckedNotCallbackChgEvent(false);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(SetCareGroupActivity.this , "异常："+throwable.getMessage());
                switchCompatWrapper.setCheckedNotCallbackChgEvent(false);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        inflateData();
    }

    public void deleteListener(Long attachId){
        apiMethodManager.deleteGroupAttach(provider, attachId, new IRequestCallback<EmptyDataReturn>() {
            @Override
            public void onSuccess(EmptyDataReturn result) {
                int code = result.getCode();
                if (code == 200){
                    inflateData();
                }else{
                    MyToast.showLong(SetCareGroupActivity.this , "删除失败"+result.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(SetCareGroupActivity.this , "删除失败"+throwable.getMessage());
            }
        });

    }

    public void inflateData(){
        showLoadDialog();
        apiMethodManager.getMonitorsByUserId(provider, user.getUSER_ID(), new IRequestCallback<GetUserMonitorsReturn>() {
            @Override
            public void onSuccess(GetUserMonitorsReturn result) {

                int code = result.getCode();
                if (code == 200){
                    boolean isWatchAll = result.getData().isWatchAll();
                    switchCompatWrapper.setCheckedNotCallbackChgEvent(isWatchAll);
                    List<GetUserMonitorsReturn.DataBean.MonitorInfoListBean> monitorInfoList = result.getData().getMonitorInfoList();
                    monitorsList.clear();
                    monitorsList.addAll(monitorInfoList);
                    userMonitorsAdapter.notifyDataSetChanged();
                }else {
                    MyToast.showLong(SetCareGroupActivity.this , ""+result.getMsg());
                }

                dismissLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(SetCareGroupActivity.this , "异常："+throwable.getMessage());
                dismissLoadDialog();
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SetCareGroupActivity.class);
        context.startActivity(intent);
    }

}
