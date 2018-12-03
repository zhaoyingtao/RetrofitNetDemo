package com.zyt.utility.net;

import android.content.Context;

import com.zyt.utility.NetConstant;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by zyt on 2017/11/17.
 */

public class RetrofitClientUtil {
    private static int default_timeout = 20;//请求超时时间
    private static int connectionPoolNums = 8;//连接池个数
    private static int connectionPoolKeepTime = 15;//连接池保活时间
    private static OkHttpClient.Builder sOkHttpClient;
    public static Context mContext;
    private static Retrofit sRetrofit;
    private Interceptor cookiesInterceptor;
    private String baseUrl;

    public RetrofitClientUtil() {
        sOkHttpClient = new OkHttpClient.Builder();
    }

    //    private RetrofitClientUtil(Context context) {
//        sOkHttpClient = new OkHttpClient.Builder()
//                .dns(new OkHttpDns())
//                .addInterceptor(new AddCookiesInterceptor(context))
//                .connectTimeout(default_timeout, TimeUnit.SECONDS)
//                .writeTimeout(default_timeout, TimeUnit.SECONDS)
//                .readTimeout(default_timeout, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
//        // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
//        //配置多个证书，有请求证书、抓包证书
////        try {
////            setCertificates(sOkHttpClient, new InputStream[]{mContext.getAssets().open("cer/app_master.cer")
////                    , mContext.getAssets().open("cer/yangying_charles_certificate.pem")
////                    , mContext.getAssets().open("cer/liuqian_charles_certificate.pem")
////                    , mContext.getAssets().open("cer/zyt_charles.pem")});
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }
    public static RetrofitClientUtil initClient(Context context) {
        if (context != null) {
            mContext = context;
            NetConstant.applicationContext = context;
        }
        return new RetrofitClientUtil();
    }

    /**
     * 设置拦截器
     *
     * @param interceptor
     * @return
     */
    public RetrofitClientUtil useCookiesInterceptor(Interceptor interceptor) {
        cookiesInterceptor = interceptor;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param outTime 单位秒
     * @return
     */
    public RetrofitClientUtil setRequestOutTime(int outTime) {
        default_timeout = outTime;
        return this;
    }

    /**
     * 连接池个数
     *
     * @param nums 大于0的正数
     * @return
     */
    public RetrofitClientUtil setConnectionPoolNums(int nums) {
        if (nums > 0) {
            connectionPoolNums = nums;
        }
        return this;
    }

    /**
     * 连接池保活时间
     *
     * @param outTime 单位秒
     * @return
     */
    public RetrofitClientUtil setConnectionPoolKeepTime(int outTime) {
        connectionPoolKeepTime = outTime;
        return this;
    }

    /**
     * baseUrl
     *
     * @param baseUrl 请求域名
     * @return
     */
    public RetrofitClientUtil setRequestBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public void build() {
        if (sOkHttpClient == null) {
            sOkHttpClient = new OkHttpClient.Builder();
        }
        if (cookiesInterceptor == null) {
            cookiesInterceptor = new AddCookiesInterceptor(mContext);
        }
        sOkHttpClient.dns(new OkHttpDns())
                .addInterceptor(cookiesInterceptor)
                .connectTimeout(default_timeout, TimeUnit.SECONDS)
                .writeTimeout(default_timeout, TimeUnit.SECONDS)
                .readTimeout(default_timeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(connectionPoolNums, connectionPoolKeepTime, TimeUnit.SECONDS));
        //最后集成
        changeApiBaseUrl(baseUrl);
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
     * @param apiUrl
     */
    public void changeApiBaseUrl(String apiUrl) {
        sRetrofit = new Retrofit.Builder()
                .client(sOkHttpClient.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(CustomConverterFactory.create())
                .baseUrl(apiUrl)
                .build();
    }

    /**
     * 通过okhttpClient来设置证书
     *
     * @param clientBuilder OKhttpClient.builder
     * @param certificates  读取证书的InputStream
     */
    public void setCertificates(OkHttpClient.Builder clientBuilder, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory
                        .generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
