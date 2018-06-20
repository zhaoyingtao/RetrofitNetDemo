package com.zyt.utility;

import android.app.Application;

/**
 * Created by zyt on 2018/6/20.
 * 项目modle中Application需要集成这个LibraryApplication
 */

public class LibraryApplication extends Application {
    private static LibraryApplication appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static LibraryApplication getInstance() {
        return appContext;
    }
}
