package com.vicent.netdemo.net;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zyt on 2018/6/20.
 */

public interface BaseApiService {

    //get请求方法
    @GET("接口名")
    Observable<JsonObject> getData(@Query("token") String token);

    //post的请求需要根据后台接收方式不同使用不同的传参（除了这俩种方法还有其他方法自行百度吧）
    //post请求方法
    @Multipart
    @POST("接口名")
    Observable<JsonObject> getPostData(@PartMap Map<String, RequestBody> params);

    //post的另一种请求方法
    @FormUrlEncoded
    @POST("接口名")
    Observable<JsonObject> getPostData(@Field("Id") String Id, @Field("Comment") String Comment);

}
