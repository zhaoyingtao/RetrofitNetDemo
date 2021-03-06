package com.zyt.utility.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.zyt.utility.R;


/**
 * Created by zyt on 2017/11/30.
 * 正在加载的弹窗
 */

public class LoadingDialog extends Dialog {

    private Context mContext;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.customerDialog);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }

    public void showLoading() {
        if (mContext == null) {
            return;
        }
        if (mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing())
                return;
        }
        if (!isShowing()) {
            show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
    //
//    @Override
//    public void onBackPressed() {
//
//    }
}
