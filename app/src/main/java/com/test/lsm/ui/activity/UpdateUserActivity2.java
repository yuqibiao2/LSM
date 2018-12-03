package com.test.lsm.ui.activity;

import android.Manifest;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.event.OnUserInfoChg;
import com.test.lsm.bean.vo.UserUpdateVo;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import pub.devrel.easypermissions.EasyPermissions;

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
        //picChoicePop.show(llCon);
        // picChoicePop.show(llCon);
        String[] perms = {Manifest.permission.CAMERA ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            toChoicePic();
        } else {
            EasyPermissions.requestPermissions(this,
                    "相機訪問權限和文件寫入權限沒開啓，可能導致部分功能無法使用，是否開啓？",
                    10003,
                    perms);
        }
    }

    private void toChoicePic(){

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(true) // 裁剪是否可旋转图片
                .scaleEnabled(true)// 裁剪是否可放大缩小图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

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
            case PictureConfig.CHOOSE_REQUEST:{

                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                if (selectList.size()==0){
                    MyToast.showLong(this , "請選擇圖片");
                }else{
                    String compressPath = selectList.get(0).getCompressPath();
                    GlidUtils.load(this , ivUserIcon , compressPath);
                    userUpdateVo.setUSER_IMAGE(MyFileOprateUtils.imgToBase64(compressPath, this));
                }
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
