package com.vicent.netdemo;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zyt on 2018/6/20.
 */

public interface BaseApi {

    //    ////    APP正式服
    String H5_Base_URL = "https://h5.changguwen.com/";
    //    H5正式服
    String Base_URL = "https://server.changguwen.com/";

    //获得主页数据
    @GET("home-index-index.api")
    Observable<JsonObject> getMainInfoData(@Query("token") String token);

    //学古诗分类
    @GET("study-study-index.api")
    Observable<JsonObject> getStudyAncientPoetryData(@Query("children_token") String children_token);

    //学古诗分类内容列表
    @GET("study-study-select.api")
    Observable<JsonObject> getStudyIslandData(@Query("count") String count, @Query("class_token") String class_token, @Query("children_token") String children_token, @Query("content_token") String content_token);

}
