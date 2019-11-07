package com.hughzhao.drygirl.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class CacheUtil {

    private static LruCache<String, Bitmap> mCache;


    public static void initLRUCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        int cacheSize = (int) (maxMemory / 8);
        mCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 缓存取bitmap
     *
     * @param key key
     * @return 缓存bm
     */
    public static Bitmap getCacheImage(String key) {
        Bitmap mb = mCache.get(key);
        return mb;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public static void removeCache(String key) {
        mCache.remove(key);
    }

    /**
     * 添加一个缓存
     *
     * @param key
     * @param bitmap
     */
    public static void addCache(String key, Bitmap bitmap) {
        if (getCacheImage(key) == null) {
            mCache.put(key, bitmap);
        }
    }
}
