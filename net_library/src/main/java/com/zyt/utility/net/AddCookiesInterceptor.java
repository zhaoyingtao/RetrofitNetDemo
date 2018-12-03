package com.zyt.utility.net;

import android.content.Context;
import android.os.Build;

import com.zyt.utility.NetConstant;
import com.zyt.utility.util.APPSharedPreferences;
import com.zyt.utility.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zyt on 2017/11/27.
 */

public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        ddd(chain);
        final Request.Builder builder = chain.request().newBuilder();
        //添加请求头
        builder.addHeader("client-method", chain.request().method());
        builder.addHeader("client-source", "app");
        builder.addHeader("client-version", NetConstant.getAppVersionName(context));
        builder.addHeader("client-platform", "android");
        builder.addHeader("client-network", NetworkUtil.getNetWorkStatus(context));
        builder.addHeader("client-platform_version", Build.VERSION.RELEASE);
        //因为需要手机phone权限，这里就不做强制调用了
//        builder.addHeader("client-deviceid", NetConstant.getDeviceUniqueIndicationCode(context));

//        Observable.just(APPSharedPreferences.init().getSharedPreferences("client_token"))
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String client_token) {
//                        //添加cookie
//                        builder.addHeader("client-token", client_token);
//                    }
//                });

//        LogUtil.e("url---------" + builder.build().url());
//        LogUtil.e("header---------" + builder.build().headers().toString());
        //proceed方法只能调用一次，调用多次会请求多次接口
        Response response = chain.proceed(builder.build());
//        collectionLog(builder.build(), response);
        return response;
    }

    /**
     * 日志收集
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void collectionLog(Request request, Response response) throws IOException {
        /**获取请求接口前的数据 */
        //the request url
        String url = request.url().toString();
        //the request method
        String method = request.method();
        //the request body
        RequestBody requestBody = request.body();
        if (requestBody != null) {//post请求会有请求体
            StringBuilder sb = new StringBuilder("Request Body [");
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset));
                sb.append(" (Content-Type = ").append(String.valueOf(contentType)).append(",")
                        .append(requestBody.contentLength()).append("-byte body)");
            } else {
                sb.append(" (Content-Type = ").append(String.valueOf(contentType))
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
            sb.append("]");
        }
        /**获取请求接口后的数据 ===剔除掉下载的大数据，否则会报oom的*/
        String bodyString = "";
        if (!url.endsWith("mp4") && !url.endsWith("mp3") && !url.endsWith("apk")) {
            ResponseBody body = response.body();
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
                bodyString = buffer.clone().readString(charset);
//                LogUtil.e(bodyString);
            }
        } else {
//            LogUtil.e("请求后的数据=url==" + url);
        }
//        //日志收集
//        AppLogCollectionUtil.init().addBaseParams(context);
//        AppLogCollectionUtil.init().addParams("appRequestMethod", method);
//        AppLogCollectionUtil.init().addParams("appRequestURL", url);
//        AppLogCollectionUtil.init().addParams("appHeaderFields", request.headers().toString());
//        AppLogCollectionUtil.init().addParams("appServerCallBack", bodyString);
//        JSONObject rootJson;
//        try {
//            rootJson = new JSONObject(bodyString);
//            int code = rootJson.optInt("code", -1);
//            if (code != 0) {
//                AppLogCollectionUtil.init().addParams("appUploadLogLevel", "CDLogUploadLevelError");
//            } else {
//                AppLogCollectionUtil.init().addParams("appUploadLogLevel", "CDLogUploadLevelNomal");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        LogUpLoadManager.init().addLogToGroup(AppLogCollectionUtil.init().getLogJsonObject());
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}