package com.example.cherishnewszyw109.help;

import com.example.cherishnewszyw109.api.ApiService;
import com.example.cherishnewszyw109.bean.NewsUri;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yuwei on 2016/12/16.
 */
public class HelpHttp {
    private Retrofit retrofit;
    private ApiService apiManager;
    public HelpHttp(){
         retrofit = new Retrofit.Builder()
                .baseUrl(NewsUri.BaseUrl)
                //String
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//添加 json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加 RxJava 适配器
                .build();
    }

    public ApiService newsTypeHttp(){
        apiManager = retrofit.create(ApiService.class);//这里采用的是Java的动态代理模式
        return apiManager;
    }

    public ApiService newsHttp(){
        apiManager = retrofit.create(ApiService.class);//这里采用的是Java的动态代理模式
        return  apiManager;
    }
}
