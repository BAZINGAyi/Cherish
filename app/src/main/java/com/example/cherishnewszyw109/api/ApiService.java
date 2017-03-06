package com.example.cherishnewszyw109.api;


import com.example.cherishnewszyw109.bean.LocalNews;
import com.example.cherishnewszyw109.bean.NewsGson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Administrator on 2016/10/23.
 */

public interface ApiService {
    @GET("social/")
    Observable<String> getString(@Query("key") String key, @Query("num") String num, @Query("page") int page);

    @GET("{type}")
    Observable <NewsGson> getNewsData(@Path("type") String type,@Query("key") String key, @Query("num") String num, @Query("page") int page);

    @GET("cherishnews/Home/Index/interfaceNews/")
    Observable <LocalNews> getNewsData(@Query("page") int page);

    @GET("cherishnews/Home/Index/interfaceNewsType/")
    Observable <LocalNews> getNewsData(@Query("tid") int type,
                                       @Query("page") int page);

    @GET("cherishnews/Home/Index/interfaceUserLogin/")
    Call <String> login(@Query("username") String username,
                                       @Query("random") String random);

    @POST("cherishnews/Home/Index/interfaceCheckLogin/")
    Call<String> ckecklogin(@Query("username") String username,
                            @Query("pass") String pass);

    @GET("cherishnews/Home/Index/interfaceCheckConflict/")
    Call <String>checkUsername(@Query("username") String username
                                );

    @GET("cherishnews/Home/Index/interfaceSearch/")
    Observable <LocalNews> getSearchData(@Query("title") String title,
                                       @Query("page") int page);

}
