package com.vicent.netdemo.ui;


import android.widget.Toast;

import com.vicent.netdemo.AppApplication;
import com.vicent.netdemo.R;
import com.vicent.netdemo.base.BaseActivity;
import com.vicent.netdemo.iview.MainIView;
import com.vicent.netdemo.presenter.MainPresent;
import com.zyt.utility.net.RetrofitClientUtil;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements MainIView {
    private MainPresent mainPresent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mainPresent = new MainPresent(this,this);
        mainPresent.getData();
    }

    @Override
    public void getDataSuccess() {
        Toast.makeText(this, "请求成功", Toast.LENGTH_SHORT).show();
    }
    private void changeBaseUrl(){
        RetrofitClientUtil.initClient(AppApplication.getInstance()).changeApiBaseUrl("要更改的URL");
    }
}
