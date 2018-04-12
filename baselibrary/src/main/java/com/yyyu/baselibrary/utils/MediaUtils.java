package com.yyyu.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.yyyu.baselibrary.global.Constant;

import java.io.File;


/**
 * 功能：调用系统意图实现一些多媒体功能
 *
 * @author yyyu
 * @date 2016/5/31
 */
public class MediaUtils {

    private static final String TAG = "MediaUtils";

    public static final int PHOTO_REQUEST_CAMERA = 11; // 相机标志
    public static final int PHOTO_REQUEST_GALLERY = 12;// intent回调相册标记
    public static final int PHOTO_REQUEST_CUT = 13; // 剪切意图标志
    public static final int VIDEO_RESULT = 14; // 录像意图
    public static final int RECORD_RESULT = 15;//录音意图
    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";//临时图片

    public static String filePath = Constant.filePath;


    /**
     * 获取视频的缩略图
     *
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 播放视频的意图
     *
     * @param activity
     * @param videoPath
     */
    public static void toPlayVedio(Activity activity, String videoPath) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.parse(videoPath), "video/*");
        activity.startActivity(it);
    }

    /**
     * 播放声音意图
     *
     * @param activity
     * @param audioPath
     */
    public static void toPlayAudio(Activity activity, String audioPath) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.parse(audioPath), "audio/*");
        activity.startActivity(it);
    }

    /**
     * 录音
     *
     * @param activity
     */
    public static void toRecord(Activity activity) {
        if (!SDcardUtils.hasSdcard()) {
            Toast.makeText(activity, "没有SD卡！！", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(Media.RECORD_SOUND_ACTION);
        activity.startActivityForResult(intent, RECORD_RESULT);
    }

    /**
     * 录像
     *
     * @param activity
     */
    public static void toVedio(Activity activity) {
        if (!SDcardUtils.hasSdcard()) {
            Toast.makeText(activity, "没有SD卡！！", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivityForResult(intent, VIDEO_RESULT);
    }

    /**
     * 拍照
     *
     * @param activity
     */
    public static void toCamera(Activity activity) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (SDcardUtils.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Constant.filePath, PHOTO_FILE_NAME)));
        }else{
            MyToast.showShort(activity , "内存卡不可用");
        }
        activity.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }
    public static void toCamera(Fragment fragment) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (SDcardUtils.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Constant.filePath, PHOTO_FILE_NAME)));
        }else{
            MyToast.showShort(fragment.getActivity() , "内存卡不可用");
        }
        fragment.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    public static void toGallery(Activity activity) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    public static void toGallery(Fragment fragment) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    private static  int minSize_dp = 300;
    public static String cropName = "temp_corp.jpg";
    /**
     *图片裁剪
     *
     * @param object Fragment或者Activity
     * @param uri 图片的uri
     * @param outputX 宽的尺寸
     * @param outputY 高的尺寸
     */
    public static void crop(Object object, Uri uri , int outputX, int outputY , String cropName) {

        Context mContext;

        if(object instanceof Activity){
            mContext = (Activity)object;
        }else if(object instanceof Fragment){
            mContext = ((Fragment) object).getActivity();
        }else{
            throw new UnsupportedOperationException("传入Activity或者Fragment");
        }
        String filePath = MyFileOprateUtils.getFilePathByUri(mContext , uri);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        BitmapFactory.decodeFile(filePath, newOpts);// bitmap中不保留图片的内容信息
        int width = newOpts.outWidth; // 读取到的图片的宽高
        int height = newOpts.outHeight;
        int scale = outputX/outputY;
        if(outputX > width){
            outputX = width;
        }
        if(outputY > height){
            outputY = height;
        }
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //---设置剪切后图片的保存路径（data.getParcelableExtra("data"); 得到的是缩略图）
        intent.putExtra("output", Uri.fromFile(new File(Constant.filePath, cropName)));
        intent.putExtra("crop", "true");
        // 裁剪框的比例
        intent.putExtra("aspectX", scale);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        if(object instanceof Activity){
            ((Activity)object).startActivityForResult(intent, PHOTO_REQUEST_CUT);
        }else if(object instanceof Fragment){
            ((Fragment)object).startActivityForResult(intent, PHOTO_REQUEST_CUT);
        }else{
            throw new UnsupportedOperationException("传入Activity或者Fragment");
        }
    }

    public static void crop(Activity activity, Uri uri) {
          crop(activity, uri,minSize_dp, minSize_dp, cropName);
    }

    public static void crop(Fragment fragment, Uri uri) {
        crop(fragment, uri,minSize_dp, minSize_dp, cropName);
    }

    /**
     *
     * @param fragment
     * @param uri
     * @param scale 宽高比
     */
    public static void crop(Fragment fragment , Uri uri , int scale){
        crop(fragment, uri,minSize_dp*scale, minSize_dp, cropName);
    }


}
