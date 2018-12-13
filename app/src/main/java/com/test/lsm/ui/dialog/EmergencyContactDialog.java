package com.test.lsm.ui.dialog;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.EmptyDataReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.vo.MonitorExpMsgVo;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.utils.logic.MonitorExpMsgFactory;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyIntentUtils;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 功能：紧急联系人弹出窗
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/26
 */
public class EmergencyContactDialog extends LsmBaseDialog {

    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    public static boolean isShow = false;

    private Context mContext;

    public EmergencyContactDialog(Context context) {
        super(context);
        mContext = context;
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        lp.width = DimensChange.dp2px(getContext(), 300);
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_emergency_constact;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] perms = {Manifest.permission.CALL_PHONE};
                if (EasyPermissions.hasPermissions(mContext, perms)) {
                    MyApplication application = (MyApplication) mContext.getApplicationContext();
                    UserLoginReturn.PdBean user = application.getUser();
                    if (user != null && !TextUtils.isEmpty(user.getURGENT_PHONE())) {
                        dismiss();
                        MonitorExpMsgVo expMsg5 = MonitorExpMsgFactory.getInstance().createExpMsg5(user.getUSER_ID());
                        APIMethodManager.getInstance().uploadMonitorExpMsg(null, expMsg5, new IRequestCallback<EmptyDataReturn>() {
                            @Override
                            public void onSuccess(EmptyDataReturn result) {
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                            }
                        });
                        MyIntentUtils.toCall(mContext, user.getURGENT_PHONE());
                    }
                } else {
                    MyToast.showLong(mContext, "沒有撥打電話權限");
                }
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        if (isShow) {
            super.dismiss();
        }
        isShow = false;
    }

    @Override
    public void show() {
        if (!isShow) {
            super.show();
        }
        isShow = true;
    }
}
