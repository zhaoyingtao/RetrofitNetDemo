package com.zyt.utility.net;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyt on 2017/11/30.
 * 封装请求参数------只针对post、get请求，上传文件不能使用这个
 */

public class RequestParams {
    private static Map<String, String> params;

    public RequestParams() {
        if (params != null) {
            params.clear();
        } else {
            params = new HashMap<>();
        }
    }

    /**
     * 添加请求参数参数
     *
     * @param key
     * @param o
     */
    public void addFormPart(String key, Object o) {
        if (params == null) {
            return;
        }
        if (o instanceof String) {
            if (!TextUtils.isEmpty((String) o)) {
                params.put(key, (String) o);
            }
        } else if (o instanceof Integer) {
            params.put(key, String.valueOf(o));
        }
    }

    public Map<String, String> create() {
        return params;
    }

    /**
     * 暂时废弃，没办法获得对应参数值
     *
     * @para * @return
     */
    public Object getFormPart(String key) {
        if (params == null) {
            return null;
        }
        return null;
    }

    /**
     * 清除所有的请求参数
     */
    public void removeAllParams() {
        if (params == null) {
            params.clear();
        }
    }
}
