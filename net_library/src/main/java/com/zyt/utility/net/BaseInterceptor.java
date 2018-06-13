package com.zyt.utility.net;

import android.content.Context;


import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * BaseInterceptor
 */
public class BaseInterceptor implements Interceptor {
    private Map<String, String> headers;
    private Context context;

    public BaseInterceptor(Context context, Map<String, String> headers) {
        this.headers = headers;
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        if (UserInfoHelper.getInstance().isLogin()) {
//
//            request = request.newBuilder()
////                    .addHeader("session_key", DbUtils.getInstance().getLoginInfo().getSession_token())
//                    .build();
//        }

        //添加公共参数
//        Request request = chain.request();
//        if (UserInfoHelper.getInstance().isLogin()) {//登录后需要添加公用的参数
//            if ("GET".equals(request.method())) {
//                HttpUrl httpUrl = request.url()
//                        .newBuilder()
//                        .addQueryParameter("session_key", DbUtils.getInstance().getLoginInfo().getSession_key())
//                        .build();
//                request = request.newBuilder().url(httpUrl).build();
//            } else if ("POST".equals(request.method())) {
//                if (request.body() instanceof FormBody) {
//                    FormBody.Builder bodyBuilder = new FormBody.Builder();
//                    FormBody formBody = (FormBody) request.body();
//
//                    //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
//                    for (int i = 0; i < formBody.size(); i++) {
//                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
//                    }
//
//                    formBody = bodyBuilder
//                            .addEncoded("session_key", DbUtils.getInstance().getLoginInfo().getSession_key())
//                            .build();
//
//                    request = request.newBuilder().post(formBody).build();
//                }
//            }
//        }
        return chain.proceed(request);
    }
}