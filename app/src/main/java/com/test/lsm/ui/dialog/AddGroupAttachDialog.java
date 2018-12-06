package com.test.lsm.ui.dialog;

import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.lsm.R;
import com.test.lsm.bean.json.ConnectMonitorReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.SetCareGroupActivity;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.utils.WindowUtils;

import butterknife.BindView;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/11/30
 */
public class AddGroupAttachDialog extends LsmBaseDialog {

    @BindView(R.id.sc_watch)
    SwitchCompat scWatch;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.pb_add)
    ProgressBar pbAdd;
    private APIMethodManager apiMethodManager;
    final  SetCareGroupActivity mSetCareGroupActivity;
    private Integer mUserId;

    private boolean isDestroy = false;

    public AddGroupAttachDialog(SetCareGroupActivity setCareGroupActivity, Integer userId) {
        super(setCareGroupActivity);
        this.mSetCareGroupActivity = setCareGroupActivity;
        this.mUserId = userId;
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        int width = WindowUtils.getSize(mContext)[0];
        lp.width = width * 4 / 5;
        return lp;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        isDestroy = false;
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    protected int getLayoutId() {

        return R.layout.dialog_add_group_attach;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

        btnConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tel = etTel.getText().toString();
                if (TextUtils.isEmpty(tel)) {
                    MyToast.showLong(mContext, "請輸入手機號");
                    return;
                }
                pbAdd.setVisibility(View.VISIBLE);
                btnConnect.setEnabled(false);
                apiMethodManager.connectMonitor(mUserId, tel, new IRequestCallback<ConnectMonitorReturn>() {
                    @Override
                    public void onSuccess(ConnectMonitorReturn result) {
                        int code = result.getCode();
                        if (code == 200) {
                            mSetCareGroupActivity.inflateData();
                            dismiss();
                        } else {
                            MyToast.showLong(mContext, result.getMsg());
                        }
                        if (!isDestroy){
                            pbAdd.setVisibility(View.GONE);
                            btnConnect.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        MyToast.showLong(mContext, "异常：" + throwable.getMessage());
                        if (isDestroy){
                            pbAdd.setVisibility(View.GONE);
                            btnConnect.setEnabled(true);
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onDetachedFromWindow() {
        isDestroy = true;
        super.onDetachedFromWindow();
    }
}
