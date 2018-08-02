package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.form.UserRegVo;
import com.test.lsm.bean.json.UserRegReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.ui.pop.PicChoicePop;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MediaUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    private static final String TAG = "RegisterActivity3";

    @BindView(R.id.iv_user_icon)
    RoundImageView ivUserIcon;
    @BindView(R.id.ll_con)
    LinearLayout llCon;
    @BindView(R.id.tv_linkman)
    TextView tvLinkman;
    @BindView(R.id.tv_user_icon)
    TextView tvUserIcon;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.tv_cst_name)
    TextView tvCstName;
    @BindView(R.id.tv_cst_tel)
    TextView tvCstTel;

    private PicChoicePop picChoicePop;

    private UserRegVo userRegVo;
    private APIMethodManager apiMethodManager;

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
        String userVoJsonStr = getIntent().getStringExtra("userVoJsonStr");
        MyLog.e(TAG, "userVoJsonStr：" + userVoJsonStr);
        Gson mGson = new Gson();
        userRegVo = mGson.fromJson(userVoJsonStr, UserRegVo.class);
        apiMethodManager = APIMethodManager.getInstance();
    }

    @Override
    protected void initView() {
        tvUserIcon.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        String username = userRegVo.getUSERNAME();
        String urgent_user = userRegVo.getURGENT_USER();
        String urgent_phone = userRegVo.getURGENT_PHONE();
        if (!TextUtils.isEmpty(username)){
            etUsername.setText(""+username);
        }
        if (!TextUtils.isEmpty(urgent_user)){
            tvCstName.setText(""+urgent_user);
        }
        if (!TextUtils.isEmpty(urgent_phone)){
            tvCstTel.setText(""+urgent_phone);
        }
    }


    @Override
    protected void initListener() {

    }

    @OnClick({R.id.iv_user_icon, R.id.tv_user_icon})
    public void selectIcon() {
        picChoicePop.show(llCon);
    }

    public static final int PICK_CONTACT_REQUEST = 21;

    @OnClick({R.id.tv_linkman})
    public void selectLinkMan() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @OnClick(R.id.tv_to_finished)
    public void toFinished() {
        String linkman = tvLinkman.getText().toString();
        String username = etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            MyToast.showLong(this, getStr(R.string.fill_in_username));
            return;
        } else if (TextUtils.isEmpty(linkman)) {
            MyToast.showLong(this, getStr(R.string.choice_link_man));
            return;
        }
        userRegVo.setUSERNAME(username);
        showLoadDialog(getStr(R.string.register_loading));
        apiMethodManager.register(userRegVo, new IRequestCallback<UserRegReturn>() {

            @Override
            public void onSuccess(UserRegReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    EventBus.getDefault().post("register_finished");
                    MyToast.showLong(RegisterActivity3.this, getStr(R.string.register_success));
                    finish();
                } else if ("06".equals(code)) {
                    MyToast.showLong(RegisterActivity3.this, getStr(R.string.user_is_exists));
                }
                //MyLog.e(TAG, "code：" + code);
                hiddenLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(RegisterActivity3.this, "网络错误：" + throwable.getMessage());
                hiddenLoadDialog();
            }
        });
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
                if (data==null) break;
                Uri contactUri = data.getData();
                //如果需要别的信息,就在这里添加参数
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                //将游标移动到第一行
                cursor.moveToFirst();
                //返回列名对应的列的索引值
                int column1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int column2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                //返回当前行指定列的值,这里就是电话
                String number = cursor.getString(column1);
                String name = cursor.getString(column2);
                userRegVo.setURGENT_USER(name);
                userRegVo.setURGENT_PHONE(number);
                tvCstName.setText(""+name);
                tvCstTel.setText(""+number);
                break;
            }
        }
    }

    public static void startAction(Activity activity, String userVoJsonStr) {
        Intent intent = new Intent(activity, RegisterActivity3.class);
        intent.putExtra("userVoJsonStr", userVoJsonStr);
        activity.startActivity(intent);
    }


}
