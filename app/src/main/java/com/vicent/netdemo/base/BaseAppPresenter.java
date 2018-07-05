package com.vicent.netdemo.base;

import com.vicent.netdemo.AppApplication;
import com.vicent.netdemo.net.BaseApiService;
import com.zyt.utility.net.RetrofitClientUtil;
import com.zyt.utility.netbase.BasePresenter;
import com.zyt.utility.netbase.BaseView;

/**
 * Created by zyt on 2018/7/2.
 * 继承BasePresenter主要完成BaseApiService的初始化，以及需要在此app中的公共处理
 */

public class BaseAppPresenter<V extends BaseView> extends BasePresenter<V> {
    protected BaseApiService apiService;

    protected BaseAppPresenter() {
        apiService = RetrofitClientUtil.initClient(AppApplication.getInstance()).create(BaseApiService.class);
    }
}
