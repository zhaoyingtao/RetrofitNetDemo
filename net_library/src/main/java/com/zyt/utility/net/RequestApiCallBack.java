package com.zyt.utility.net;

import android.content.Context;


import com.zyt.utility.R;
import com.zyt.utility.dialog.LoadingDialog;
import com.zyt.utility.util.ToastUtils;

import rx.Subscriber;

/**
 * Created by zyt on 2017/11/17.
 * 请求数据回调---
 */

public abstract class RequestApiCallBack<T> extends Subscriber<T> {
    private Context mContext;
    private LoadingDialog loadingDialog;
    private boolean isShowLoading;

    public RequestApiCallBack() {
        isShowLoading = false;
    }

    public RequestApiCallBack(Context mContext) {
        this.mContext = mContext;
        this.isShowLoading = true;
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
        if (isShowLoading && loadingDialog != null) {
            loadingDialog.show();
        }
    }

    @Override
    public void onCompleted() {
        //在这里设置取消dialog就可以
        dissmissLoadDialog();
    }

    @Override
    public void onError(Throwable e) {
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
