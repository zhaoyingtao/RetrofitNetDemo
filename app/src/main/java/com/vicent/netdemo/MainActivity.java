package com.vicent.netdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zyt.utility.net.RetrofitClientUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApi baseApi = RetrofitClientUtil.create(BaseApi.class);
    }
}
