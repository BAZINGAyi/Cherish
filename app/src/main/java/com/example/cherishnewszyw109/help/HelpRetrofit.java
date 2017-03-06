package com.example.cherishnewszyw109.help;

import com.example.cherishnewszyw109.api.ApiService;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yuwei on 2016/12/17.
 */
public class HelpRetrofit {
    Retrofit retrofit;
    ApiService infoApi;
    public HelpRetrofit(String url){
         retrofit = new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
                 .baseUrl(url).build();
        //创建api访问请求
        infoApi = retrofit.create(ApiService.class);
    }
    public Call<String> connectHttp(String name, String pass){
        Call<String> call = infoApi.ckecklogin(name,pass);
        return call;
    }
    public Call<String> connectHttpLogin(String name,String random){
        Call<String> call = infoApi.login(name,random);
        return call;
    }
    public Call<String> connectHttpUsername(String name){
        Call<String> call = infoApi.checkUsername(name);
        return call;
    }
    public String formatReturnData(Response<String> response){
        if(response.isSuccessful()){
            String result = response.body();
            result = result.replace("\n","");
            result =result.replace("\r","");
            System.out.println("---请求成功"+result);;
            if(result.equals("success")){
                return "success";
            }else{
                return "error";
            }
        }
        return "error";
    }
}
