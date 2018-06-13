package com.zyt.utility.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * Created by zyt on 2017/4/7.
 * 打印日志
 */

public class LogUtil {
    private static String TAG = "zyt";

    /**
     * 打印错误日志
     *
     * @param mess
     */
    public static void e(String mess) {
        //只有debug模式下才打印日志
        if (isApkDebugable(LibraryApplication.getInstance())) {
            Log.e(TAG, "----------------------" + mess);
        }
    }

    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
     *
     * @param context
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static void d(String earGrindMVFragment, String s) {
    }
}
