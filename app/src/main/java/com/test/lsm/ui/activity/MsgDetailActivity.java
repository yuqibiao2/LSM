package com.test.lsm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMsgDetail;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MyLog;

import butterknife.BindView;
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

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_msg_icon)
    ImageView ivMsgIcon;
    @BindView(R.id.tv_msg_title)
    TextView tvMsgTitle;
    @BindView(R.id.tv_msg_datetime)
    TextView tvMsgDatetime;

    private String title;
    private String content;
    private APIMethodManager apiMethodManager;

    @Override
    public void beforeInit() {
        super.beforeInit();
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        content = bundle.getString(JPushInterface.EXTRA_ALERT);
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void initView() {
        tvContent.setText("title：" + title + "    content：" + content);
    }

    @Override
    protected void initListener() {
        apiMethodManager.getMsgDetail(Integer.parseInt(content), new IRequestCallback<GetMsgDetail>() {
            @Override
            public void onSuccess(GetMsgDetail result) {
                GetMsgDetail.PdBean.RecordBean record = result.getPd().getRecord();
                String imageUrl =record.getPUSH_IMAGE_URL();
                String desc = record.getPUSH_DESC();
                String title = record.getPUSH_TITLE();
                Glide.with(MsgDetailActivity.this).load(imageUrl).crossFade().into(ivMsgIcon);
                tvMsgTitle.setText(""+title);
                tvContent.setText(""+desc);
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG, "getMsgDetail：" + throwable.getMessage());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    public static void startAction(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MsgDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
