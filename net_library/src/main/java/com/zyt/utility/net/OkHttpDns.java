package com.zyt.utility.net;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zyt on 2018/11/7.
 */

public class OkHttpDns implements Dns {
    private static final Dns SYSTEM = Dns.SYSTEM;

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        Log.e("HttpDns", "lookup:" + hostname);
        String ip = getIPByHost(hostname);
        if (ip != null && !ip.equals("")) {
            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            Log.e("HttpDns", "inetAddresses:" + inetAddresses);
            return inetAddresses;
        }
        return SYSTEM.lookup(hostname);
    }

    /**
     * 根据url获得ip,此方法只是最简单的模拟,实际情况很复杂,需要做缓存处理
     *
     * @param host
     * @return
     */
    private String getIPByHost(String host) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("203.107.1.1")
                .addPathSegment("d")
                .addQueryParameter("host", host)
                .build();
        //与我们正式请求独立，所以这里新建一个OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        try {
            String result = null;
            /**
             * 子线程中同步去获取
             */
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                JSONObject jsonObject = new JSONObject(body);
                JSONArray ips = jsonObject.optJSONArray("ips");
                if (ips != null) {
                    result = ips.optString(0);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
