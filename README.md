# RetrofitNetDemo
这是一个基于retrofit的网络请求再度封装，附带了一些基本的utils公用类；

一、项目的Application必须继承依赖库的LibraryApplication，否则有些公用类不能使用可能会造成崩溃；

二、在项目中直接引入

compile 'com.bintray.library:net_library:1.0.0'

三、代码中使用

1、需要在你的app中创建一个接口类存放接口：例如：BaseApi

public interface BaseApi {

    //    服务器的baseurl
    String Base_URL = "https://xxxxxxxxx/";

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

