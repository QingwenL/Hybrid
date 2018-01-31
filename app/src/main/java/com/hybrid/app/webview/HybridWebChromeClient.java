package com.hybrid.app.webview;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by 2017/5/11
 *
 * @author: Liqingwen
 */
public class HybridWebChromeClient extends WebChromeClient {
    public final static String TAG = HybridWebChromeClient.class.getName();

    private HybridWebView mHybridWebView;

    public HybridWebChromeClient(HybridWebView mHybridWebView) {
        this.mHybridWebView = mHybridWebView;

    }

    /**
     * 获取加载进度
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (mHybridWebView.getHybridWebViewListener() != null) {
            mHybridWebView.getHybridWebViewListener().onProgress(newProgress);
            mHybridWebView.setProgressbar(newProgress);
            if (newProgress == 100) {
                mHybridWebView.getHybridWebViewListener().onProgressFinished();
            }
        }
        super.onProgressChanged(view, newProgress);
    }


    /**
     * 获取HTML标题
     *
     * @param view
     * @param title
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (mHybridWebView.getHybridWebViewListener() != null) {
            mHybridWebView.getHybridWebViewListener().onPageTitle(title);
        }
    }


    /**
     * 打印控制台log
     *
     * @param consoleMessage
     * @return
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.e(TAG, consoleMessage.message() + " -- From line "
                + consoleMessage.lineNumber() + " of "
                + consoleMessage.sourceId());
        return true;
    }

}
