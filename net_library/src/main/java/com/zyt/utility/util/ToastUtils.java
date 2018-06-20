package com.zyt.utility.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.utility.LibraryApplication;
import com.zyt.utility.R;

/**
 * Created by zyt on 2017/3/27.
 */

public class ToastUtils {
    private static ToastUtils mToastUtils;
    private Toast mToast;

    public static ToastUtils init() {
        if (mToastUtils == null) {
            synchronized (ToastUtils.class) {
                if (mToastUtils == null) {
                    mToastUtils = new ToastUtils();
                }
            }
        }
        return mToastUtils;
    }

    /**
     * 显示Toast
     *
     * @param mess
     */
    public void showToast(String mess) {
        if (mToast == null) {
            mToast = Toast.makeText(LibraryApplication.getInstance(), mess, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(mess);
        }
        mToast.show();
    }

    /**
     * 显示string文件中的文本
     * @param messResID R.string.xxxx
     */
    public void showToast(int messResID) {
        if (mToast == null) {
            mToast = Toast.makeText(LibraryApplication.getInstance(), LibraryApplication.getInstance().getResources().getString(messResID), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(LibraryApplication.getInstance().getResources().getString(messResID));
        }
        mToast.show();
    }

    /**
     * 显示Toast---在屏幕中间
     *
     * @param mess
     */
    public void showToastMiddle(String mess) {
        if (mToast == null) {
            mToast = Toast.makeText(LibraryApplication.getInstance(), mess, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setText(mess);
        }
        mToast.show();
    }

    /**
     * 显示Toast
     *
     * @param context 可以为APPApplication的环境变量
     * @param mess
     * @param image 图片资源 R.mipmap.xxx 选用默认图片直接传-1
     */
    public void showToastUpImageViewDownText(Context context, String mess, int image) {
        Toast mToast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        ImageView imageView = view.findViewById(R.id.iv_toast);
        TextView textView = view.findViewById(R.id.tv_toast);
        if (image != -1) {
            imageView.setImageDrawable(context.getResources().getDrawable(image));
        }
        textView.setText(mess);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(view);
        mToast.show();
    }

}
