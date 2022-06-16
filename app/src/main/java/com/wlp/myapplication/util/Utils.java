package com.wlp.myapplication.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Description:用于工具类中获取上下文环境ApplicationContext
 */
public final class Utils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("can't be instantiate.");
    }

    /**
     * 初始化工具类
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("should be initialized in application.");
    }

    /**
     * 获取string资源
     */
    public static String getStringRes(int res){
        return getContext().getResources().getString(res);
    }

    /**c
     * 获取color资源
     */
    public static int getColor(int colorResId){
        return ContextCompat.getColor(getContext(), colorResId);
    }

    /**c
     * 获取drawable资源
     */
    public static Drawable getDrawable(int drawableId){
        return ContextCompat.getDrawable(getContext(), drawableId);
    }
}