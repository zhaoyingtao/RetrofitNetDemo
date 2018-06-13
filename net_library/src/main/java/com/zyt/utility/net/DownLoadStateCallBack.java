package com.zyt.utility.net;

/**
 * Created by zyt on 2017/12/13.
 */

public interface DownLoadStateCallBack {


    void downStart();

    void onProgress(String progress);

    void downFinish();

    void downError(String progress);
}
