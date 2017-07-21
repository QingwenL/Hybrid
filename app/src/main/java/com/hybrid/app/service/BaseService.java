package com.hybrid.app.service;


import com.hybrid.app.webview.HybridWebViewClient;

/**
 * Created by 2017/5/11
 * @author: Liqingwen
 */
public interface BaseService {

    String STATE_FAIL = "fail";//处理失败返回标志
    String STATE_SUCC = "success";//处理成功返回标志
    String STATE_PROGRESS = "progress";//正在处理中


    /**
     * 执行服务
     * @param mHybridWebViewClient {@link HybridWebViewClient}
     * @param data               由H5传入的N数据
     * @param callbackId         CT回调码
     */
    void onService(HybridWebViewClient mHybridWebViewClient,String data,String callbackId);



}
