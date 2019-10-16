package com.hughzhao.drygirl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hughzhao.drygirl.bean.Sister;
import com.hughzhao.drygirl.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="MainActivity";
    private ImageView imageView;
    private Button next_btn,flash_btn;
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
        initData();//初始化数据
        initUI();//初始化ui
    }

    /**
     * 初始化数据
     */
    public void initData(){
        urls = new ArrayList<>();
        Utility.parseURL(10,page);
        List<Sister> sisters = Utility.getSisters();
        Log.d(TAG,sisters.toString());
        for (int i =0;i<sisters.size();i++){
            Log.d(TAG,sisters.get(i).getUrl());
            urls.add(sisters.get(i).getUrl());
        }
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_btn:
                if(currentNo>9)
                    currentNo=0;
                loader.load(imageView,urls.get(currentNo));//获取当前url对应的图片
                currentNo++;
                break;
            case R.id.flash_btn:
                page++;
                Log.d(TAG,String.valueOf(page));
                initData();
                break;
        }
    }
}
