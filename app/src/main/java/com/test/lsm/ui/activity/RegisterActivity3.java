package com.test.lsm.ui.activity;

import android.Manifest;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.test.lsm.R;
import com.test.lsm.bean.form.UserRegVo;
import com.test.lsm.bean.json.UserRegReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.GlidUtils;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.ui.pop.PicChoicePop;
import com.yyyu.baselibrary.ui.widget.RoundImageView;
import com.yyyu.baselibrary.utils.MediaUtils;
import com.yyyu.baselibrary.utils.MyFileOprateUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import pub.devrel.easypermissions.EasyPermissions;

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
    private static  final int REQUEST_CODE_CHOOSE=10010;

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
        if (!TextUtils.isEmpty(username)) {
            etUsername.setText("" + username);
        }
        if (!TextUtils.isEmpty(urgent_user)) {
            tvCstName.setText("" + urgent_user);
        }
        if (!TextUtils.isEmpty(urgent_phone)) {
            tvCstTel.setText("" + urgent_phone);
        }
    }


    @Override
    protected void initListener() {

    }

    @OnClick({R.id.iv_user_icon, R.id.tv_user_icon})
    public void selectIcon() {
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

                    Bundle bundle = new Bundle();
                    bundle.putString("result", "success");
                    FirebaseAnalytics.getInstance(RegisterActivity3.this).logEvent("lsm01_register", bundle);

                    EventBus.getDefault().post("register_finished");
                    MyToast.showLong(RegisterActivity3.this, getStr(R.string.register_success));
                    finish();
                } else if ("06".equals(code)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("result", "fail");
                    FirebaseAnalytics.getInstance(RegisterActivity3.this).logEvent("lsm01_register", bundle);

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
                String cropPicPath = MediaUtils.filePath + MediaUtils.cropName;
                userRegVo.setUSER_IMAGE(MyFileOprateUtils.imgToBase64(cropPicPath, this));
                break;
            }
            case PICK_CONTACT_REQUEST: {
                if (data == null) break;
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
                tvCstName.setText("" + name);
                tvCstTel.setText("" + number);
                break;
            }

            case PictureConfig.CHOOSE_REQUEST:{

                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                if (selectList.size()==0){
                    MyToast.showLong(this , "請選擇圖片");
                }else{
                    String compressPath = selectList.get(0).getCompressPath();
                    GlidUtils.load(this , ivUserIcon , compressPath);
                    userRegVo.setUSER_IMAGE(MyFileOprateUtils.imgToBase64(compressPath, this));
                }
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
