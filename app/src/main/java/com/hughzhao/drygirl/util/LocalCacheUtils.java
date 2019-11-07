package com.hughzhao.drygirl.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LocalCacheUtils {
    /**
     * 文件保存的路径
     */
    public static final String FILE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";

    /**
     * 从本地SD卡获取图片的bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromLocal(String url) {
        try {
            File file = new File(FILE_PATH, url);
            Log.d("hughzhao",FILE_PATH);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 向本地SD卡写网络图片的bitmap
     *
     * @param url
     * @param bitmap
     */
    public static void setBitmap2Local(String url, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径。以图片的url为文件名
            File file = new File(FILE_PATH, url);
            // file事实上是图片，它的父级File是目录，推断一下目录是否存在，假设不存在，创建目录
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 目录不存在
                fileParent.mkdirs();// 创建目录
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 一种转换url的方式
//     * @param key
//     * @return
//     */
//    public static String hashKeyForDisk(String key) {
//        String cacheKey;
//        try {
//            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
//            mDigest.update(key.getBytes());
//            cacheKey = bytesToHexString(mDigest.digest());
//        } catch (NoSuchAlgorithmException e) {
//            cacheKey = String.valueOf(key.hashCode());
//        }
//        return cacheKey;
//    }
//
//    private static String bytesToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            String hex = Integer.toHexString(0xFF & bytes[i]);
//            if (hex.length() == 1) {
//                sb.append('0');
//            }
//            sb.append(hex);
//        }
//        return sb.toString();
//    }

}