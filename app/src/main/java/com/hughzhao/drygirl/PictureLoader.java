package com.hughzhao.drygirl;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


import com.hughzhao.drygirl.util.CacheHelper;
import com.hughzhao.drygirl.util.HttpUtil;
import com.hughzhao.drygirl.util.LocalCacheUtils;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PictureLoader {
    private ImageView imgView;
    private String imgUrl;
    private byte[] picBytes;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    picBytes = (byte[])msg.obj;
                    if(picBytes!=null){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(picBytes,0,picBytes.length);
                        CacheHelper.addCache(imgUrl,bitmap);//添加到缓存中
                        LocalCacheUtils.setBitmap2Local(imgUrl,bitmap);//写到内存中
                        imgView.setImageBitmap(bitmap);//设置ImageView
                    }
                    break;
            }
        }
    };

    /**
     * 加载url，获取照片byte数组
     * @param imgView
     * @param imgUrl
     */
    public void load(ImageView imgView, final String imgUrl){
        this.imgView = imgView;
        this.imgUrl = imgUrl;
        Bitmap b1 = CacheHelper.getCacheImage(imgUrl);
        if(b1!=null){
            imgView.setImageBitmap(b1);
            return;
        }//从缓存里取
        Bitmap b2 = LocalCacheUtils.getBitmapFromLocal(imgUrl);
        if(b2!=null){
            imgView.setImageBitmap(b2);
            return;
        }//从内存里取
        Drawable drawable = imgView.getDrawable();
        if(drawable!=null && drawable instanceof BitmapDrawable){
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if(bitmap!=null && bitmap.isRecycled()){
                bitmap.recycle();//减小图片对资源的消耗,这里目前理解不行，不到位
            }
        }
        //OkHttp请求
        HttpUtil.sendOkHttpRequest(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what=1;
                msg.obj=response.body().bytes();
                handler.sendMessage(msg);
            }
        });
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
