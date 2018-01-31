package com.hybrid.app.activity;


import android.os.Bundle;
import android.view.View;

import com.hybrid.app.compat.StatusBarCompat;
import com.hybrid.app.webview.HybridWebView;
import com.hybrid.app.R;
import com.hybrid.app.service.UserService;

/**
 * Created by 2017/5/12
 *
 * @author: Liqingwen
 */
public class MainActivity extends BaseActivity implements HybridWebView.HybridWebViewListener {

    HybridWebView hyw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, mHeadLayout, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        hyw = (HybridWebView) findViewById(R.id.hyw);
        hyw.setWebViewCache(true); // 支持缓存
        hyw.setHybridWebViewListener(this);
        hyw.registerService(new UserService());
        hyw.loadUrl("https://www.baidu.com");//https://www.baidu.com\
    }


    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgressFinished() {
    }

    @Override
    public void onPageTitle(String title) {
        setTitle(title);
    }


    @Override
    public void onBackPressed() {
        if (!hyw.canGoBack()) {
            super.onBackPressed();
        }
        hyw.goBack();

    }
}
