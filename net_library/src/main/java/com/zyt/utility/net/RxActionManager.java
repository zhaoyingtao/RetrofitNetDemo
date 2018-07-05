package com.zyt.utility.net;

import rx.Subscription;

/**
 * Created by zyt on 2018/7/4.
 */

public interface RxActionManager<T> {
    void add(T tag, Subscription subscription);

    void remove(T tag);

    void cancel(T tag);

    void cancelAll();
}
