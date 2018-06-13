package com.zyt.utility.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zyt on 2017/11/23.
 * 验证工具
 */

public class VerificationUtils {
    private static VerificationUtils verificationUtils;

    public static VerificationUtils getInstance() {
        if (verificationUtils == null) {
            synchronized (VerificationUtils.class) {
                if (verificationUtils == null) {
                    verificationUtils = new VerificationUtils();
                }
            }
        }
        return verificationUtils;
    }

    /**
     * 验证手机号----根据需求验证
     *
     * @param phoneNum
     * @return
     */
    public boolean checkPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum) || !phoneNum.startsWith("1") || phoneNum.length() != 11) {
            return false;
        }
        return true;
    }


    /**
     * 验证手机号
     *
     * @param phoneNum
     * @return
     */
    public boolean isPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9]|17[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher m = pattern.matcher(phoneNum);
        return m.matches();
    }
}
