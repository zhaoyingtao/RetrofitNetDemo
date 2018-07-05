package com.vicent.netdemo;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.vicent.netdemo.net.ApiEnvironmentMgr;
import com.zyt.utility.net.RetrofitClientUtil;


/**
 * Created by zyt on 2018/6/12.
 */

public class AppApplication extends MultiDexApplication {
    private static AppApplication applicationContext;

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
        //初始化网络请求
        RetrofitClientUtil.initClient(this).changeApiBaseUrl(ApiEnvironmentMgr.getBaseApiUrl());
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
