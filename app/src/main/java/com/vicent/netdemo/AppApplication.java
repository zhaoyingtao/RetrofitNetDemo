package com.vicent.netdemo;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.vicent.netdemo.net.ApiEnvironmentMgr;
import com.vicent.netdemo.net.BaseApiService;
import com.zyt.utility.net.AddCookiesInterceptor;
import com.zyt.utility.net.RetrofitClientUtil;


/**
 * Created by zyt on 2018/6/12.
 */

public class AppApplication extends MultiDexApplication {
    private static AppApplication applicationContext;
    private static BaseApiService apiService;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        init();
    }

    public static Context getInstance() {
        return applicationContext;
    }

    private void init() {
        //一下两种任选其一
        //第一种：完全自定义网络请求
//        RetrofitClientUtil.initClient(this)
//                .useCookiesInterceptor(new AddCookiesInterceptor(this))
//                .setConnectionPoolNums(8)
//                .setConnectionPoolKeepTime(15)
//                .setRequestOutTime(20)
//                .setRequestBaseUrl(ApiEnvironmentMgr.getBaseApiUrl())
//                .build();
        //第二种：直接使用封装自带的网络请求
        RetrofitClientUtil.initClient(this)
                .setRequestBaseUrl(ApiEnvironmentMgr.getBaseApiUrl())
                .build();
        //初始化BaseApiService实体类
        apiService = RetrofitClientUtil.initClient(AppApplication.getInstance()).create(BaseApiService.class);


    }

    //这个函数是模拟一个过程环境，在真机中永远也不会被调用。
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
