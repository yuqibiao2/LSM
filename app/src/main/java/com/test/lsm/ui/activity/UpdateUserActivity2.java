package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.OnUserInfoChg;
import com.test.lsm.bean.form.UserUpdateVo;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.bean.json.UserRegReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.ui.pop.PicChoicePop;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MediaUtils;
import com.yyyu.baselibrary.utils.MyFileOprateUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 功能：修改用户信息界面2
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/11
 */
public class UpdateUserActivity2 extends LsmBaseActivity {

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

    private UserUpdateVo userUpdateVo;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;
    private MyApplication application;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_user2;
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
        mGson = new Gson();
        userUpdateVo = mGson.fromJson(userVoJsonStr, UserUpdateVo.class);
        apiMethodManager = APIMethodManager.getInstance();

        application = (MyApplication) getApplication();
        user = application.getUser();

    }

    @Override
    protected void initView() {
        tvUserIcon.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        String username = userUpdateVo.getUSERNAME();
        String urgent_user = userUpdateVo.getURGENT_USER();
        String urgent_phone = userUpdateVo.getURGENT_PHONE();
        //userUpdateVo.setUSER_IMAGE("");
        if (!TextUtils.isEmpty(username)){
            etUsername.setText(""+username);
        }
        if (!TextUtils.isEmpty(urgent_user)){
            tvCstName.setText(""+urgent_user);
        }
        if (!TextUtils.isEmpty(urgent_phone)){
            tvCstTel.setText(""+urgent_phone);
        }

        etUsername.setText(""+user.getUSERNAME());
        tvCstName.setText(""+user.getURGENT_USER());
        tvCstTel.setText(""+user.getURGENT_PHONE());
        String userImage = user.getUSER_IMAGE();
        if (!TextUtils.isEmpty(userImage)){
            GlidUtils.load(this  , ivUserIcon , userImage);
            //userUpdateVo.setUSER_IMAGE(userImage);
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
        String cstName = tvCstName.getText().toString();
        String cstTel = tvCstTel.getText().toString();
        String username = etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            MyToast.showLong(this, "请填写用户名");
            return;
        } else if (TextUtils.isEmpty(cstName)) {
            MyToast.showLong(this, "请选择紧急联系人");
            return;
        }
        userUpdateVo.setUSERNAME(username);
        userUpdateVo.setURGENT_USER(cstName);

        userUpdateVo.setURGENT_PHONE(cstTel);
        showLoadDialog("修改中....");
        apiMethodManager.updateUser(userUpdateVo, new IRequestCallback<UserRegReturn>() {

            @Override
            public void onSuccess(UserRegReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    EventBus.getDefault().post("register_finished");
                    MyToast.showLong(UpdateUserActivity2.this, "修改成功！");
                    String pdStr = mGson.toJson(result.getPd());
                    //保存用户信息
                    MySPUtils.put(UpdateUserActivity2.this , LoginRegUtils.USER_INFO , pdStr);
                    application.setUser(result.getPd());
                    EventBus.getDefault().post(new OnUserInfoChg("修改了用户信息"));
                    initView();
                    finish();
                }
                MyLog.e(TAG, "code：" + code);
                hiddenLoadDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(UpdateUserActivity2.this, "网络错误：" + throwable.getMessage());
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
                String cropPicPath = MediaUtils.filePath + MediaUtils.cropName;
                Bitmap bitmap = BitmapFactory.decodeFile(cropPicPath);
                ivUserIcon.setImageBitmap(bitmap);
                userUpdateVo.setUSER_IMAGE(MyFileOprateUtils.imgToBase64(cropPicPath , this));
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
                userUpdateVo.setURGENT_USER(name);
                userUpdateVo.setURGENT_PHONE(number);
                tvCstName.setText(""+name);
                if (!TextUtils.isEmpty(number)){
                    number = number.trim().replace(" ","");
                }
                tvCstTel.setText(""+number);
                break;
            }
        }
    }

    public static void startAction(Activity activity, String userVoJsonStr) {
        Intent intent = new Intent(activity, UpdateUserActivity2.class);
        intent.putExtra("userVoJsonStr", userVoJsonStr);
        activity.startActivity(intent);
    }


}
