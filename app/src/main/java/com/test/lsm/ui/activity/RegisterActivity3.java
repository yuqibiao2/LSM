package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.lsm.R;
import com.yyyu.baselibrary.utils.MediaUtils;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.ui.pop.PicChoicePop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 功能：注册界面3
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/11
 */
public class RegisterActivity3 extends LsmBaseActivity {

    @BindView(R.id.iv_user_icon)
    RoundImageView ivUserIcon;
    @BindView(R.id.tv_to_finished)
    TextView tvToFinished;
    @BindView(R.id.ll_con)
    LinearLayout llCon;
    @BindView(R.id.tv_linkman)
    TextView tvLinkman;
    private PicChoicePop picChoicePop;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    hiddenLoadDialog();
                    EventBus.getDefault().post("register_finished");
                    finish();
                    break;
            }
            return false;
        }
    });



    @Override
    public int getLayoutId() {
        return R.layout.activity_register3;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        View popView = View.inflate(this, R.layout.pop_pic_select, null);
        popView.measure(0, 0);
        picChoicePop = new PicChoicePop(this,
                WindowManager.LayoutParams.MATCH_PARENT,
                popView.getMeasuredHeight(), popView) {
            @Override
            public void dismiss() {
                super.dismiss();
            }

            @Override
            public void show(View popLocView) {
                super.show(popLocView);
            }
        };
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.iv_user_icon, R.id.tv_user_icon})
    public void selectIcon() {
        picChoicePop.show(llCon);
    }

    public static final int PICK_CONTACT_REQUEST = 21;

    @OnClick({R.id.iv_select_linkman1, R.id.tv_linkman, R.id.tv_select_linkman2})
    public void selectLinkMan() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @OnClick(R.id.tv_to_finished)
    public void toFinished() {
        showLoadDialog("注册中....");
        mHandler.sendEmptyMessageDelayed(0 , 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MediaUtils.PHOTO_REQUEST_CAMERA: {//相机
                if (RESULT_OK != resultCode) break;
                File iconFile = new File(MediaUtils.filePath, MediaUtils.PHOTO_FILE_NAME);
                MediaUtils.crop(this, Uri.fromFile(iconFile));
                break;
            }
            case MediaUtils.PHOTO_REQUEST_GALLERY: {//相册
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    MediaUtils.crop(this, uri);
                }
                break;
            }
            case MediaUtils.PHOTO_REQUEST_CUT: {//剪切
                if (RESULT_OK != resultCode) break;
                Bitmap bitmap = BitmapFactory.decodeFile(MediaUtils.filePath + MediaUtils.cropName);
                ivUserIcon.setImageBitmap(bitmap);
                break;
            }
            case PICK_CONTACT_REQUEST: {
                Uri contactUri = data.getData();
                //如果需要别的信息,就在这里添加参数
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                //将游标移动到第一行
                cursor.moveToFirst();
                //返回列名对应的列的索引值
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                //返回当前行指定列的值,这里就是电话
                String number = cursor.getString(column);
                tvLinkman.setText(number);
            }
        }
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity3.class);
        activity.startActivity(intent);
    }


}
