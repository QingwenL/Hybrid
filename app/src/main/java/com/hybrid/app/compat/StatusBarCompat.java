package com.hybrid.app.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Liqingwen
 * @version V1.0
 * @ClassName: {@link StatusBarCompat}
 * @Description: TODO (Translucent Bar)
 * @Date 2017/11/24 17:03
 */
public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#06CEAE");

    /**
     * @param activity
     * @param titleBar   标题栏 View
     * @param visibility {@link #View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN},        设置布局全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见
     *                   {@link #SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION},   设置隐藏虚拟按钮与 window.setNavigationBarColor(设置虚拟按钮背景颜色); 配套使用
     *                   {@link #SYSTEM_UI_FLAG_LAYOUT_STABLE},            防止系统栏隐藏时内容区域大小发生变化
     *                   {@link #SYSTEM_UI_FLAG_LIGHT_STATUS_BAR}          设置STATUS_BAR 字体颜色为黑色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(AppCompatActivity activity, ViewGroup titleBar, int visibility) {
        ColorDrawable dra = (ColorDrawable) titleBar.getBackground();
        int statusBarColor = dra.getColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(visibility);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor != INVALID_VAL ? statusBarColor : COLOR_DEFAULT);
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            ((ViewGroup) titleBar.getParent()).addView(statusBarView, 0, lp);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(statusBarColor != INVALID_VAL ? statusBarColor : COLOR_DEFAULT);
            contentView.addView(statusBarView, lp);
        }
    }

    /**
     * 拿到系统STATUS_BAR 高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}