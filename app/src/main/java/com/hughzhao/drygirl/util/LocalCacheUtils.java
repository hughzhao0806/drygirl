package com.hughzhao.drygirl.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LocalCacheUtils {
    /**
     * 文件保存的路径
     */
    public static final String FILE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";

    /**
     * 从本地SD卡获取网络图片，key是url的MD5值
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromLocal(String url) {
        try {
            String fileName = hashKeyForDisk(url);
            Log.d("hugzhao",fileName);
            File file = new File(FILE_PATH, fileName);
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
     * 向本地SD卡写网络图片
     *
     * @param url
     * @param bitmap
     */
    public static void setBitmap2Local(String url, Bitmap bitmap) {
        try {
            // 文件的名字
            String fileName = hashKeyForDisk(url);
            // 创建文件流，指向该路径。文件名称叫做fileName
            File file = new File(FILE_PATH, fileName);
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




    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}