package com.hughzhao.drygirl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hughzhao.drygirl.bean.Sister;
import com.hughzhao.drygirl.util.CacheHelper;
import com.hughzhao.drygirl.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="MainActivity";
    private ImageView imageView;
    private Button pre_btn,next_btn,flash_btn;
    private PictureLoader loader;//根据图片url获取图片的类
    private int currentNo=0;//当前图片的序号，每页共十个
    private List<String> urls;//图片的url集合
    private int page = 1;//当前页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,Thread.currentThread().toString());
        loader = new PictureLoader();
        CacheHelper.initLRUCache();//初始化缓存
        initData();//初始化数据
        initUI();//初始化ui
        loader.load(imageView,urls.get(currentNo));//默认加载第一张
    }

    /**
     * 初始化数据
     */
    public void initData(){
        urls = Utility.getImgUrl(10,page);
        Log.d("urls",String.valueOf(urls.size()));
    }

    /**
     * 初始化UI，设置按钮监听
     */
    public void initUI(){
        imageView = findViewById(R.id.girl);
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);
        flash_btn =findViewById(R.id.flash_btn);
        flash_btn.setOnClickListener(this);
        pre_btn = findViewById(R.id.pre_btn);
        pre_btn.setOnClickListener(this);
        pre_btn.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre_btn:
                currentNo--;
                if(currentNo<0)
                    currentNo=9;
                loader.load(imageView,urls.get(currentNo));//获取当前url对应的图片
                break;
            case R.id.next_btn:
                pre_btn.setEnabled(true);
                currentNo++;
                if(currentNo>9)
                    currentNo=0;
                loader.load(imageView,urls.get(currentNo));//获取当前url对应的图片
                break;
            case R.id.flash_btn:
                page++;
                Log.d(TAG,String.valueOf(page));
                initData();
                break;
        }
    }
}
