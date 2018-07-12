package com.yyyu.baselibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.yyyu.baselibrary.global.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 功能：文件操作相关的工具类
 *
 * @author yyyu
 * @date 2016/8/6
 */
public class MyFileOprateUtils {

    private static final String TAG = "MyFileOprateUtils";


    /**
     * 将一张图片转换成base64字符串
     *
     * @param imgPath 图片的路径
     * @return base64字符串
     */
    public static String imgToBase64(final String imgPath, final Context context) {

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 把图片加载到输出流中
        int option = 100;
        while (baos.toByteArray().length / 1024 > 1024) {// >1M就压缩
            baos.reset();// 清空流
            if(option <=5){
                break;
            }
            option -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);// 眼option比例压缩
        }
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    /**
     * 压缩图片的质量
     *
     * @param imgPath
     * @param savePath
     */
    public static void compressPicByQuality(File imgPath, File savePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 把图片加载到输出流中
        int option = 100;
        while (baos.toByteArray().length / 1024 > 500) {// >500k就压缩
            baos.reset();// 清空流
            option -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);// 眼option比例压缩
        }
        FileOutputStream fos = null;
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        try {
            fos = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = isBm.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    /**
     * * 按图片size压缩图片后保存在指定路径
     *
     * @param imgPath     :读取图片的路径
     * @param saveImgPath 压缩后图片的保存路径
     */
    private static void compressImageBySize(String imgPath, String saveImgPath, Context context) {

        FileOutputStream fileOutputStream = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);// bitmap中不保留图片的内容信息
        int width = newOpts.outWidth; // 读取到的图片的宽高
        int height = newOpts.outHeight;
        newOpts.inJustDecodeBounds = false; // 再把设置改为允许读取内容
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int afterWidth = dm.widthPixels;
        int afterHeight = dm.heightPixels;
        int sampleSize = 1; // 图片的采样率
        if (width > height && width > afterWidth) {
            sampleSize = width / afterWidth;
        } else if (width < height && height > afterHeight) {
            sampleSize = height / afterHeight;
        }
        if (sampleSize <= 0)
            sampleSize = 1;
        newOpts.inSampleSize = sampleSize; // 设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        try {
            fileOutputStream = new FileOutputStream(saveImgPath);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = isBm.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            Log.e("异常--------", "" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if (isBm != null) {
                    isBm.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    /**
     * 保存图片的方法
     */
    public static String saveBitmap(Activity activity, Bitmap bm, String picName) {

        if (Build.VERSION.SDK_INT >= 22) {
            int REQUEST_EXTERNAL_STORAGE = 1;
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }
        File f = new File(Constant.filePath, picName);
        if (!f.exists()){
            f.getParentFile().mkdir();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 把图片加载到输出流中
        int option = 100;
        while (baos.toByteArray().length / 1024 > 2 * 1024) {// >2M就压缩
            baos.reset();// 清空流
            option -= 10;
            MyLog.e("图片超过1M 压缩了-------------------");
            bm.compress(Bitmap.CompressFormat.JPEG, option, baos);// 眼option比例压缩
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bais.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            MyLog.e("not found =============" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            MyLog.e("io==============" + e.getMessage());
            e.printStackTrace();
        }
        return f.getPath();
    }

    /**
     * 根据文件名得到文件
     */
    public static File getFIleByName(String fileName) {
        File file = new File(Constant.filePath + fileName);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 判断文件是否已经存在
     */
    public static boolean isFileExists(String fileName) {
        File temp = new File(Constant.filePath + fileName);
        return temp.exists();
    }


    public static String getFilePathByUri(final Context context, final Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * 得到缓存数据的方法
     * 返回false表示不从缓存获取
     */
    public static String getDataFromCache(Context context, String url) {
        String data = null;
        File saveFile = new File(context.getCacheDir(), url + ".ofyu");
        if (!saveFile.exists()) return null;
        ByteArrayOutputStream bos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(saveFile);
            bos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            data = bos.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return data;
        }
    }

    /**
     * 缓存数据的方法
     */
    public static void toCacheData(Context context, String url, String dataJson) {
        File saveFile = new File(context.getCacheDir(), url + ".ofyu");
        ByteArrayInputStream bis = null;
        FileOutputStream fos = null;
        try {
            bis = new ByteArrayInputStream(dataJson.getBytes());
            fos = new FileOutputStream(saveFile);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            MyLog.e("缓存成功=============================");
        } catch (IOException e) {
            e.printStackTrace();
            MyLog.e("IOException===========" + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
