package com.hughzhao.drygirl.util;


import android.util.Log;

import com.hughzhao.drygirl.bean.Sister;


import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Utility {
    private static final String TAG = "Utility";
    private static final String BASE_URL="http://gank.io/api/data/福利/";
    private static List<Sister> sisters;
    private static boolean flag = false;
    /**
     * 解析url，根据返回的json数据再进一步获取妹子数据
     */
    public static void parseURL(int count,int page){
        sisters = new ArrayList<>();
        String url = BASE_URL+count+"/"+page;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseJsonData(response);
            }
        });
    }

    /**
     * 解析json数据
     * @param response
     */
    public static void parseJsonData(Response response){
        try {
            String jsonData = response.body().string();
            JSONObject object = new JSONObject(jsonData);
            JSONArray results = object.getJSONArray("results");
            for (int i=0;i<results.length();i++){
                JSONObject sisterObj = (JSONObject) results.get(i);
                Sister sister = new Sister();
                sister.set_id(sisterObj.getString("_id"));
                sister.setCreatedAt(sisterObj.getString("createdAt"));
                sister.setDesc(sisterObj.getString("desc"));
                sister.setPublishedAt(sisterObj.getString("publishedAt"));
                sister.setType(sisterObj.getString("type"));
                sister.setSource(sisterObj.getString("source"));
                sister.setUrl(sisterObj.getString("url"));
                sister.setUsed(sisterObj.getString("used"));
                sister.setWho(sisterObj.getString("who"));
                sisters.add(sister);
            }
            flag =true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取图像集合
     * @return
     */
    public static List<String> getImgUrl(int count,int page){
        parseURL(count,page);
        while (!flag) ;
        flag=false;
        List<String> urls = new ArrayList<>();
        for(int i=0;i<sisters.size();i++){
            urls.add(sisters.get(i).getUrl());
        }
        return urls;
    }
}
