package com.vicent.netdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


/**
 * Created by zyt on 2018/7/3.
 */

public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
    }

    /**
     * 这个方法提供初始化的布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化成员属性，不需要手动调用，只需要重写内容即可
     */
    protected abstract void initData();
}
