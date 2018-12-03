package com.zyt.utility.netbase;

import android.content.Context;
import android.widget.Toast;

import com.zyt.utility.NetConstant;
import com.zyt.utility.util.NetworkUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zyt on 2018/7/2.
 */

public abstract class BasePresenter<V extends BaseView> {
    protected V view;
    private CompositeSubscription mCompositeSubscription;
    protected Context mContext;

    public BasePresenter() {

    }

    protected void attachView(Context mContext, V view) {
        this.mContext = mContext;
        this.view = view;
    }
    protected void attachView(Context mContext) {
        this.mContext = mContext;
    }
    protected void detachView() {
        this.view = null;
        onUnSubsribe();
    }

    //取消rxjava的注册，避免内存泄漏
    private void onUnSubsribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscription(Observable observable, Subscriber subscriber) {
        if (!NetworkUtil.isNetworkAvailable(NetConstant.applicationContext)) {
            Toast.makeText(mContext, "网络无连接，请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }
}
