package com.zyt.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;


/**
 * Created by zyt on 2018/7/2.
 * netlibrary的常量类
 */

public class NetConstant {
    public static Context applicationContext;

    /**
     * app的版本名
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * app的版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取手机的唯一标示
     */
    public static String getDeviceUniqueIndicationCode(Context context) {
        String imei = getIMEI(context);
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        } else {
            String serial;
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
                Log.e("serial", "" + serial);
            } catch (Exception e) {
                e.printStackTrace();
                serial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            if (TextUtils.isEmpty(serial)) {
                serial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return serial;
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
