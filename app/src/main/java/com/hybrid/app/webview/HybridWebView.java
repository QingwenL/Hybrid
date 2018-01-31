package com.hybrid.app.webview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.hybrid.app.R;
import com.hybrid.app.service.BaseService;
import com.hybrid.app.utils.CacheUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2017/5/11
 *
 * @author: Liqingwen
 */
public class HybridWebView extends FrameLayout {

    /**
     * HybridWebView 基础监听器
     */
    public interface HybridWebViewListener {
        void onProgress(int progress);

        void onProgressFinished();

        void onPageTitle(String title);
    }


    private WebView mWebView;
    private HybridWebViewListener hybridWebViewListener;
    private HybridWebViewClient hybridWebViewClient;
    private HybridWebChromeClient hybridWebChromeClient;
    private ProgressBar progressbar;

    private ArrayMap<String, BaseService> mService = new ArrayMap<String, BaseService>();


    public HybridWebView(Context context) {
        super(context);
        init();
        initProgressBar();
    }

    public HybridWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initProgressBar();
    }

    public HybridWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initProgressBar();
    }


    /**
     * 初始化WebView
     */
    private void init() {
        mWebView = new WebView(getContext());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持Js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持js打开新窗口
        webSettings.setAllowFileAccess(true); // 支持访问文件
        webSettings.setUseWideViewPort(true);//支持任意比例缩放
        webSettings.setSupportZoom(false); // 不显示缩放按钮
        webSettings.setBuiltInZoomControls(false); // 不使用特殊的缩放机制
        webSettings.setTextZoom(100); // 默认ebView正常字体
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        // webSettings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        setWebViewCache(false);//默认关闭缓存
        hybridWebViewClient = new HybridWebViewClient(this);
        hybridWebChromeClient = new HybridWebChromeClient(this);
        mWebView.setWebViewClient(hybridWebViewClient);
        mWebView.setWebChromeClient(hybridWebChromeClient);
        addView(mWebView);
    }


    private void initProgressBar() {
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip2px(3), 0, 0));
        ClipDrawable d = new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.title_bg)), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressbar.setProgressDrawable(d);
        addView(progressbar);
    }


    public void setProgressbar(final int newProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(newProgress);
        animator.setDuration(1000);
        progressbar.setAlpha(1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                progressbar.setProgress(value);
                if (value == 100) {
                    ValueAnimator animator = ValueAnimator.ofFloat(1, 0).setDuration(500);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            progressbar.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });
                    animator.start();
                }
            }
        });
        animator.start();

    }


    /**
     * 打开缓存/或关闭缓存
     */
    public void setWebViewCache(boolean isOpen) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(isNetworkConnected()
                ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK); // 缓存策略 ： 网络良好根据cache-control决定是否从网络上取数据/没网络链接只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        if (isOpen) {
            webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
            webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
            webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        } else {
            webSettings.setDomStorageEnabled(false); // 关闭 DOM storage API 功能
            webSettings.setDatabaseEnabled(false);   //关闭 database storage API 功能
            webSettings.setAppCacheEnabled(false); //关闭 Application Caches 功能
        }
    }


    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }


    /**
     * Gets whether this WebView has a back history item.
     *
     * @return true iff this WebView has a back history item
     */
    public boolean canGoBack() {

        return mWebView.canGoBack();
    }

    /**
     * Goes back in the history of this WebView.
     */
    public void goBack() {

        mWebView.goBack();
    }

    /**
     * Gets whether this WebView has a forward history item.
     *
     * @return true iff this Webview has a forward history item
     */
    public boolean canGoForward() {

        return mWebView.canGoForward();
    }

    /**
     * Goes forward in the history of this WebView.
     */
    public void goForward() {
        mWebView.goForward();
    }


    /**
     * 清空缓存
     */
    public void cleanCache() {
        CacheUtils.delFile(CacheUtils.getCachePath(getContext()));
    }

    /**
     * 注册服务
     *
     * @param mBaseService
     */
    public void registerService(BaseService mBaseService) {
        mService.put(mBaseService.getClass().getSimpleName(), mBaseService);
    }

    /**
     * 解除服务
     *
     * @param mBaseService
     */
    public void unRegisterServic(BaseService mBaseService) {
        mService.remove(mBaseService.getClass().getSimpleName());
    }


    /**
     * 判断当前网络是否链接
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public Map<String, BaseService> getmService() {
        return mService;
    }

    public HybridWebViewListener getHybridWebViewListener() {
        return hybridWebViewListener;
    }

    public void setHybridWebViewListener(HybridWebViewListener hybridWebViewListener) {
        this.hybridWebViewListener = hybridWebViewListener;

    }

    public HybridWebChromeClient getHybridWebChromeClient() {
        return hybridWebChromeClient;
    }

    public void setHybridWebChromeClient(HybridWebChromeClient hybridWebChromeClient) {
        this.hybridWebChromeClient = hybridWebChromeClient;
    }

    public HybridWebViewClient getHybridWebViewClient() {
        return hybridWebViewClient;
    }

    public void setHybridWebViewClient(HybridWebViewClient hybridWebViewClient) {
        this.hybridWebViewClient = hybridWebViewClient;
    }

}
