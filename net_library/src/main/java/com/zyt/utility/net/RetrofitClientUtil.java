package com.zyt.utility.net;

import android.content.Context;

import com.zyt.utility.NetConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by zyt on 2017/11/17.
 */

public class RetrofitClientUtil {
    private static final int DEFAULT_TIMEOUT = 20;//请求超时时间
    private static OkHttpClient sOkHttpClient;
    public static Context mContext;
    private static Retrofit sRetrofit;
    private static RetrofitClientUtil retrofitClientUtil;


    private RetrofitClientUtil(Context context) {
        sOkHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(
//                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .addNetworkInterceptor(new ProgressInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor(context))
                .addInterceptor(new AddCookiesInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .build();
    }

    private static RetrofitClientUtil getSimpleRetrofitClient(Context context) {
        if (retrofitClientUtil == null) {
            synchronized (RetrofitClientUtil.class) {
                if (retrofitClientUtil == null) {
                    retrofitClientUtil = new RetrofitClientUtil(context);
                }
            }
        }
        return retrofitClientUtil;
    }

    /**
     * 必须传入application的context
     * @param context
     * @return
     */
    public static RetrofitClientUtil initClient(Context context) {
        if (context != null) {
            mContext = context;
            NetConstant.applicationContext = context;
        }
        return getSimpleRetrofitClient(context);
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        if (sRetrofit == null) {
            throw new RuntimeException("sRetrofit is null! you should init Retrofit");
        }
        return sRetrofit.create(service);
    }

    /**
     * 改变baseurl
     *
     * @param apiUrl apiurl不规范不是正确格式会直接崩溃
     */
    public void changeApiBaseUrl(String apiUrl) {
        sRetrofit = new Retrofit.Builder()
                .client(sOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(CustomConverterFactory.create())
                .baseUrl(apiUrl)
                .build();
    }
}
