package com.zyt.utility.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by zyt on 2017/6/23.
 */

public class HtmlWebView extends WebView {
    public HtmlWebView(Context context) {
        super(context);
        initSetWeb();
    }

    public HtmlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSetWeb();
    }

    public HtmlWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSetWeb();
    }

    private void initSetWeb() {
        WebSettings settings = getSettings();
        /* 设置支持Js,必须设置的,不然网页基本上不能看 */
        settings.setJavaScriptEnabled(true);
        /* 设置缓存模式,我这里使用的默认,不做多讲解 */
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        /* 设置为true表示支持使用js打开新的窗口 */
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
         /* 大部分网页需要自己保存一些数据,这个时候就的设置下面这个属性 */
        settings.setDomStorageEnabled(true);
         /* 设置为使用webview推荐的窗口 */
        settings.setUseWideViewPort(true);
        /* 设置网页自适应屏幕大小 ---这个属性应该是跟上面一个属性一起用 */
        settings.setLoadWithOverviewMode(true);
        /* HTML5的地理位置服务,设置为true,启用地理定位 */
        settings.setGeolocationEnabled(true);
        /* 设置是否允许webview使用缩放的功能,我这里设为false,不允许 */
        settings.setBuiltInZoomControls(false);
        /* 提高网页渲染的优先级 */
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        /* 设置显示水平滚动条,就是网页右边的滚动条.我这里设置的不显示 */
        this.setHorizontalScrollBarEnabled(false);
        /* 指定垂直滚动条是否有叠加样式 */
        setVerticalScrollbarOverlay(true);
        /* 设置滚动条的样式 */
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        /* 这个不用说了,重写WebChromeClient监听网页加载的进度,从而实现进度条 */
        setWebChromeClient(new WebChromeClient());
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
        setWebViewClient(new WebViewClient());
    }
}
