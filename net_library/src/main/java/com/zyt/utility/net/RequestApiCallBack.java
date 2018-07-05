package com.zyt.utility.net;

import android.content.Context;

import com.zyt.utility.dialog.LoadingDialog;

import rx.Subscriber;

/**
 * Created by zyt on 2017/11/17.
 * 请求数据回调---
 */

public abstract class RequestApiCallBack<T> extends Subscriber<T> {
    private Context mContext;
    private LoadingDialog loadingDialog;
    private boolean isShowLoading;
    private String apiFlag;//请求当前api的flag 后期可以做取消请求处理

    /**
     *
     * @param apiFlag 请求当前api的flag 后期可以做取消请求处理
     */
    public RequestApiCallBack(String apiFlag) {
        isShowLoading = false;
        this.apiFlag = apiFlag;
    }

    /**
     *
     * @param mContext context不为空会在请求时会出现加载弹窗
     * @param apiFlag 请求当前api的flag 后期可以做取消请求处理
     */
    public RequestApiCallBack(Context mContext, String apiFlag) {
        this.mContext = mContext;
        this.isShowLoading = true;
        this.apiFlag = apiFlag;
        initLoadingDialog(isShowLoading);
    }

    /**
     * 初始化LoadingDialog
     *
     * @param isShowLoading
     */
    private void initLoadingDialog(boolean isShowLoading) {
        if (isShowLoading) {
            loadingDialog = new LoadingDialog(mContext);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        RxApiManager.get().add(apiFlag, this);
        if (isShowLoading && loadingDialog != null) {
            loadingDialog.show();
        }
    }

    @Override
    public void onCompleted() {
        RxApiManager.get().remove(apiFlag);
        //在这里设置取消dialog就可以
        dissmissLoadDialog();
    }

    @Override
    public void onError(Throwable e) {
        RxApiManager.get().remove(apiFlag);
        dissmissLoadDialog();
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

    /**
     * 关闭加载dialog
     */
    private void dissmissLoadDialog() {
        if (isShowLoading && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
