package com.hybrid.app.service;


import com.hybrid.app.webview.HybridWebViewClient;

/**
 * Created by 2017/5/12
 * @author: Liqingwen
 */
public class UserService implements  BaseService{
    @Override
    public void onService(HybridWebViewClient mHybridWebViewClient, String data, String callbackId) {
        int x = (int) (Math.random()*1000);
        mHybridWebViewClient.onServiceCallback(callbackId,STATE_SUCC,"{\"随机码\":\""+x+"\"}");
    }


}
