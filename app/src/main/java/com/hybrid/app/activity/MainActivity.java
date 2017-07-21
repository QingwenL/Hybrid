package com.hybrid.app.activity;

import android.app.Activity;
import android.os.Bundle;
import com.hybrid.app.webview.HybridWebView;
import com.hybrid.app.R;
import com.hybrid.app.service.UserService;

/**
 * Created by 2017/5/12
 *
 * @author: Liqingwen
 */
public class MainActivity extends Activity{

    HybridWebView hyw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hyw = (HybridWebView) findViewById(R.id.hyw);
        hyw.setWebViewCache(true); // 支持缓存
        hyw.registerService(new UserService());
        hyw.loadUrl("file:///android_asset/Hybrid/index.html");//https://www.baidu.com
    }

}
