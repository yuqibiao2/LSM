package com.test.lsm.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.test.lsm.R;
import com.test.lsm.bean.json.EmptyDataReturn;
import com.test.lsm.bean.json.GetUserMonitorsReturn;
import com.test.lsm.bean.vo.GroupAttach;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.yyyu.baselibrary.ui.widget.SwitchCompatWrapper;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/30
 */
public class UserMonitorsAdapter extends BaseQuickAdapter<GetUserMonitorsReturn.DataBean.MonitorInfoListBean, BaseViewHolder> {

    LifecycleProvider<ActivityEvent> mProvider;

    public UserMonitorsAdapter(int layoutResId, @Nullable List<GetUserMonitorsReturn.DataBean.MonitorInfoListBean> data  , LifecycleProvider<ActivityEvent> provider) {
        super(layoutResId, data);
        this.mProvider = provider;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GetUserMonitorsReturn.DataBean.MonitorInfoListBean item) {
        helper.getView(R.id.btn_connect).setEnabled(false);
        helper.getView(R.id.et_tel).setEnabled(false);
        helper.setText(R.id.btn_connect , "已連接");
        helper.setText(R.id.et_tel , item.getPhone());
        helper.setText(R.id.tv_user_name , item.getUsername());
        SwitchCompat scWatch = helper.getView(R.id.sc_watch);
        SwitchCompatWrapper switchCompatWrapper = new SwitchCompatWrapper(scWatch);
        final String isWatching = item.getIsWatching();
        if ("1".equals(isWatching)){
            switchCompatWrapper.setChecked(true);
        }else{
            switchCompatWrapper.setChecked(false);
        }
        switchCompatWrapper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //修改监听状态
                List<GroupAttach> groupAttaches = new ArrayList<>();
                GroupAttach groupAttach = new GroupAttach();
                groupAttach.setAttachId(item.getAttachId());
                groupAttaches.add(groupAttach);
                if (isChecked){
                    groupAttach.setIsWatching("1");
                }else{
                    groupAttach.setIsWatching("0");
                }
                modifyStatus(groupAttaches);
            }
        });
    }

    private void modifyStatus(List<GroupAttach> groupAttaches) {
        APIMethodManager.getInstance().modifyGroupAttachStatus(mProvider, groupAttaches, new IRequestCallback<EmptyDataReturn>() {
            @Override
            public void onSuccess(EmptyDataReturn result) {
                int code = result.getCode();
                if (code == 200){
                    MyToast.showLong(mContext , "修改成功");
                }else {
                    MyToast.showLong(mContext , ""+result.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(mContext , "异常："+throwable.getMessage());
            }
        });
    }

}
