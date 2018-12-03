package com.zyt.utility.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zyt.utility.NetConstant;

/**
 * Created by zyt on 2017/11/20.
 * 存储工具
 */

public class APPSharedPreferences {
    private static SharedPreferences sharedPreferences;
    private static APPSharedPreferences appSharedPreferences;


    public static APPSharedPreferences init() {
        if (appSharedPreferences == null) {
            synchronized (APPSharedPreferences.class) {
                if (appSharedPreferences == null) {
                    appSharedPreferences = new APPSharedPreferences();
                    sharedPreferences = NetConstant.applicationContext.getSharedPreferences("ttMaster", Context.MODE_PRIVATE);
                }
            }
        }
        return appSharedPreferences;
    }

    /**
     * 移除这个存储
     *
     * @param key
     */
    public void removeObject(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 存储String
     *
     * @param key
     * @param value
     */
    public void setSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 移除当前key对应的值
     *
     * @param key
     */
    public void removeSharedPreferences(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 存储Int型数据
     *
     * @param key
     * @param value
     */
    public void setSharedPreferences(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 取出int
     *
     * @param key
     * @return
     */
    public int getSharedPreferences(String key, int defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 取出String
     *
     * @param key
     * @return
     */
    public String getSharedPreferences(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    /**
     * 存float
     *
     * @param key
     * @param value
     */
    public void setSharedPreferences(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 取float
     *
     * @param key
     * @return
     */
    public float getSharedPreferences(String key, float defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, defaultValue);
        }
        return 0f;
    }

    /**
     * 存储boolean
     *
     * @param key
     * @param value
     */
    public void setSharedPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 取出boolean
     *
     * @param key
     * @return
     */
    public boolean getSharedPreferences(String key, boolean defaultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defaultValue);
        }
        return false;
    }
}
