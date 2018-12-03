package com.zyt.utility.net;

import android.content.Context;

import rx.Subscriber;

/**
 * Created by zyt on 2017/11/17.
 * 请求数据回调---
 */

public abstract class RequestApiCallBack<T> extends Subscriber<T> {
    private String apiFlag;//请求当前api的flag 后期可以做取消请求处理
    protected Context mMContext;

    public RequestApiCallBack(String apiFlag) {
        this.apiFlag = apiFlag;
    }

    public RequestApiCallBack(Context mContext, String apiFlag) {
        this.mMContext = mContext;
        this.apiFlag = apiFlag;
    }

    @Override
    public void onStart() {
        super.onStart();
        RxApiManager.get().add(apiFlag, this);

    }

    @Override
    public void onCompleted() {
        RxApiManager.get().remove(apiFlag);

    }

    @Override
    public void onError(Throwable e) {
        RxApiManager.get().remove(apiFlag);
        if (e instanceof ExceptionHandle.ResponeThrowable) {
            onFail((ExceptionHandle.ResponeThrowable) e);
        } else {
            onFail(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFail(ExceptionHandle.ResponeThrowable e);


}
