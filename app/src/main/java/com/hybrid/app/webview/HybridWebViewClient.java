package com.hybrid.app.webview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hybrid.app.service.BaseService;

/**
 * Created by 2017/5/11
 * @author: Liqingwen
 */
public class HybridWebViewClient extends WebViewClient {

    public final static String NOT_FOUND = "file:///android_asset/NotFound/not_found.html";
    public final static String HYBRID_SCHEME = "http";
    public final static String HYBRID_HOST = "nativeapi";
    public final static String HYBRID_SERVICE = "serviceName";
    public final static String HYBRID_CALBACKID = "callbackId";
    public final static String HYBRID_DATA = "data";
    private  HybridWebView mHybridWebView;
    private  String url;


    public HybridWebViewClient(HybridWebView mHybridWebView) {
        this.mHybridWebView = mHybridWebView;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        this.url = url;

        Uri uri = Uri.parse(url);
        String  host = uri.getHost();
        switch (uri.getScheme()){ // 判断Scheme拦截服务
            case HybridWebViewClient.HYBRID_SCHEME:
                if(host.equals(HybridWebViewClient.HYBRID_HOST)){
                    String serviceName = uri.getQueryParameter(HybridWebViewClient.HYBRID_SERVICE);
                    String callbackId  =  uri.getQueryParameter(HybridWebViewClient.HYBRID_CALBACKID);
                    String data = uri.getQueryParameter(HybridWebViewClient.HYBRID_DATA);
                    BaseService mBaseService  = mHybridWebView.getmService().get(serviceName);
                    if(mBaseService != null){ // 服务不为空就开始调用
                        mBaseService.onService(mHybridWebView.getHybridWebViewClient(),data,callbackId);
                    }
                    view.stopLoading();
                }
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        // 加载网页失败时处理
        if(request.getUrl().toString().equals(this.url)){
            view.loadUrl(HybridWebViewClient.NOT_FOUND);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return  super.shouldOverrideUrlLoading(view,url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view,request);
    }

    /**
     * 回调H5方法
     * @param callbackId   回调ID
     * @param resultData   服务返回的数据 json字符串
     * @param resultStatus 服务执行状态
     */
    public void onServiceCallback(String callbackId, String resultStatus, String resultData) {
        String jsCallback = "javascript:window.Callback(" + callbackId + ", \"" + resultStatus + "\", " + resultData + ");";
        mHybridWebView.loadUrl(jsCallback);
    }

}
