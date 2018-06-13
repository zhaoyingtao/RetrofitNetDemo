package com.zyt.utility.net;

import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zyt on 2017/11/30.
 * 封装请求参数
 */

public class RequestParams {
    private Map<String, RequestBody> params;

    public RequestParams() {
        params = new HashMap<>();
    }

    /**
     * 添加请求参数参数 ---以表单的格式提交
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
                params.put(key, RequestBody.create(MediaType.parse("text/plain"), (String) o));
            }
        } else if (o instanceof File) {
            File file = (File) o;
            //第一个参数filename是和后台约定的字段，后面的都是固定的
            params.put("filename\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png/jpg/jpeg/mp4/mov/amr"), file));
        } else if (o instanceof Integer) {
            params.put(key, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(o)));
        }
//        else {
//            params.put(key, RequestBody.create(MediaType.parse("text/plain"), String.valueOf(o)));
//        }
    }

    /**
     * 批量上传图片，添加图片参数 ---以表单的格式提交
     *
     * @param files
     */
    public void addFormPart(List<String> files) {
        if (params == null || files == null) {
            return;
        }
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = new File(files.get(i));
            //两个反斜杠不能取消
            params.put("filename[]\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png/jpg/jpeg"), file));
        }
    }

    public Map<String, RequestBody> create() {
        return params;
    }

    /**
     * 暂时废弃，没办法获得对应参数值
     *
     * @param key
     * @return
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
