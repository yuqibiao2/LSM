package com.test.lsm.ui.dialog;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.json.UserLoginReturn;
import com.yyyu.baselibrary.utils.DimensChange;
import com.yyyu.baselibrary.utils.MyInetntUtils;
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

    public  static  boolean isShow = false;

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
                        MyInetntUtils.toCall(mContext, user.getURGENT_PHONE());
                    }
                } else {
                    MyToast.showLong(mContext, "沒有撥打電話權限");
                }
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()){
                    dismiss();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        isShow = false;
    }

    @Override
    public void show() {
        isShow = true;
        super.show();
    }
}
