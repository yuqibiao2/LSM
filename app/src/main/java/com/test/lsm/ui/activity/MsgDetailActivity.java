package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMsgDetail;
import com.test.lsm.bean.json.PushExtra;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.StatusBarCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2018/5/14
 */
public class MsgDetailActivity extends LsmBaseActivity {

    private static final String TAG = "MsgDetailActivity";

    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.iv_msg_icon)
    ImageView ivMsgIcon;
    @BindView(R.id.tv_msg_title)
    TextView tvMsgTitle;
    @BindView(R.id.tv_msg_datetime)
    TextView tvMsgDatetime;
    @BindView(R.id.iv_msg_icon_small)
    ImageView ivMsgIconSmall;

    private String title;
    private String content;
    private APIMethodManager apiMethodManager;
    private PushExtra pushExtra;

    @Override
    public void beforeInit() {
        super.beforeInit();
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        pushExtra = new Gson().fromJson(extra, PushExtra.class);
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    protected boolean setDefaultStatusBarCompat() {
        StatusBarCompat.setTrans(this);
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        showLoadDialog();
        apiMethodManager.getMsgDetail(Integer.parseInt(pushExtra.getMsgId()), new IRequestCallback<GetMsgDetail>() {
            @Override
            public void onSuccess(GetMsgDetail result) {
                GetMsgDetail.PdBean.RecordBean record = result.getPd().getRecord();
                String imageUrl = record.getPUSH_IMAGE_URL();
                String imageUrlSmall = record.getFILE_ICON_URL();
                String desc = record.getPUSH_DESC();
                String title = record.getPUSH_TITLE();
                Glide.with(MsgDetailActivity.this).load(imageUrl).crossFade().into(ivMsgIcon);
                Glide.with(MsgDetailActivity.this).load(imageUrlSmall).crossFade().into(ivMsgIconSmall);
                tvMsgTitle.setText("" + title);
                String str = "<html><head>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0'/>" +
                        "<style type='text/css'>  img{width:100%;height:auto;}</style></head><body>" +
                        desc + "</body></html>";
                wvContent.loadData(str, "text/html; charset=UTF-8", null);
                dismissLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoadDialog();
                MyLog.e(TAG, "getMsgDetail：" + throwable.getMessage());
            }
        });
    }

    public static void startAction(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MsgDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    
}
