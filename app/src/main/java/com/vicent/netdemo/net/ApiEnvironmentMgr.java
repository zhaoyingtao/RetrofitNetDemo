package com.vicent.netdemo.net;

import com.vicent.netdemo.AppApplication;
import com.zyt.utility.net.RetrofitClientUtil;

/**
 * Created by zyt on 2018/7/5.
 * 管理网络请求baseurl
 *
 * 注意⚠️上线必须修改这里的请求url
 */

public class ApiEnvironmentMgr {
    private final static int MAEnvironmentDev = 1;//开发
    private final static int MAEnvironmentTest = 2;//测试
    private final static int MAEnvironmentStage = 3;//预发布
    private final static int MAEnvironmentProd = 4;//正式发布
    private static int environmentFlag;

    public static String getBaseApiUrl() {
        String baseUrl;
        switch (environmentFlag) {
            case MAEnvironmentDev://开发
                baseUrl = "";
                break;
            case MAEnvironmentTest://测试
                baseUrl = "";
                break;
            case MAEnvironmentStage://预发布
                baseUrl = "";
                break;
            case MAEnvironmentProd://正式发布
                baseUrl = "";
                break;
            default:
                baseUrl = "https://www.baidu.com/";
                break;
        }
        return baseUrl;
    }

    /**
     * 更改网络请求
     *
     * @param environment
     */
    public static void changeApiEnvironment(int environment) {
        environmentFlag = environment;
        RetrofitClientUtil.initClient(AppApplication.getInstance()).changeApiBaseUrl(getBaseApiUrl());
    }
}
