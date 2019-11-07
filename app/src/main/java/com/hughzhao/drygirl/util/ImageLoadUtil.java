package com.hughzhao.drygirl.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hughzhao.drygirl.util.CacheUtil;
import com.hughzhao.drygirl.util.HttpUtil;
import com.hughzhao.drygirl.util.LocalCacheUtils;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageLoadUtil {
    private Bitmap bitmap;
    private String imgUrl;
    private byte[] picBytes;
    private boolean flag = false;

    /**
     * 加载url，获取照片bitmap
     * @param imgUrl
     */
    public Bitmap load(final String imgUrl){
        this.imgUrl = imgUrl;
        bitmap = CacheUtil.getCacheImage(imgUrl);
        if(bitmap!=null){
            return bitmap;
        }//从缓存里取
        bitmap = LocalCacheUtils.getBitmapFromLocal(imgUrl);
        if(bitmap!=null){
            return bitmap;
        }//从内存里取
//        if(bitmap!=null && bitmap.isRecycled()){
//            bitmap.recycle();//减小图片对资源的消耗,这里目前理解不行，不到位
//        }
        //OkHttp请求
        HttpUtil.sendOkHttpRequest(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                picBytes = response.body().bytes();
                if(picBytes!=null){
                    bitmap = BitmapFactory.decodeByteArray(picBytes,0,picBytes.length);
                    CacheUtil.addCache(imgUrl,bitmap);//添加到缓存中
                    LocalCacheUtils.setBitmap2Local(imgUrl,bitmap);//写到内存中
                    flag = true;
                }
            }
        });
        while (!flag);
        flag =false;
        return bitmap;
//        new Thread(runnable).start();
    }

    /**
     * OkHttp请求
     */
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            HttpUtil.sendOkHttpRequest(imgUrl, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Message msg = new Message();
//                    msg.what=1;
//                    msg.obj=response.body().bytes();
//                    handler.sendMessage(msg);
//                }
//            });
//        }
//    };
    /**
     * 传统http请求
     */
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            try{
//                URL url = new URL(imgUrl);
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                con.setConnectTimeout(10000);
//                if(con.getResponseCode()==200){
//                    InputStream in = con.getInputStream();
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    byte[] bytes = new byte[1024];
//                    int length = -1;
//                    while((length=in.read(bytes))!=-1){
//                        out.write(bytes,0,length);
//                    }
//                    picBytes = out.toByteArray();
//                    in.close();
//                    out.close();
//                    Message msg = new Message();
//                    msg.what=1;
//                    handler.sendMessage(msg);
//
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    };
}
