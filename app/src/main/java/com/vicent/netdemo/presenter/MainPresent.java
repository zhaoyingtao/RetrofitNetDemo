package com.vicent.netdemo.presenter;


import android.content.Context;

import com.google.gson.JsonObject;
import com.vicent.netdemo.base.BaseAppPresenter;
import com.vicent.netdemo.iview.MainIView;
import com.zyt.utility.net.ExceptionHandle;
import com.zyt.utility.net.RequestApiCallBack;


/**
 * Created by zyt on 2018/7/2.
 */

public class MainPresent extends BaseAppPresenter<MainIView> {
    public MainPresent(Context mContext, MainIView loginView) {
        attachView(mContext, loginView);
    }

    /**
     * 没有加载弹窗的请求
     */
    public void getData() {
        //注意此处RequestApiCallBack<JsonObject>("postData")泛型是你要解析的数据对象格式，构造参数去源码看
        addSubscription(apiService.getPostData(",", ""),
                new RequestApiCallBack<JsonObject>("postData") {

            @Override
            public void onSuccess(JsonObject jsonObject) {
                view.getDataSuccess();
            }

            @Override
            public void onFail(ExceptionHandle.ResponeThrowable e) {
            }
        });
    }

    /**
     * 有加载弹窗的请求
     */
    public void getDataTwo() {
        //注意此处RequestApiCallBack<JsonObject>(mContext,"postData")泛型是你要解析的数据对象格式，构造参数构造参数去源码看
        addSubscription(apiService.getPostData(",", ""),
                new RequestApiCallBack<JsonObject>(mContext, "postData") {

            @Override
            public void onSuccess(JsonObject jsonObject) {
                view.getDataSuccess();
            }

            @Override
            public void onFail(ExceptionHandle.ResponeThrowable e) {
            }
        });
    }
}
