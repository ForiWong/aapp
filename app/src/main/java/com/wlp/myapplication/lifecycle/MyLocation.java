package com.wlp.myapplication.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import static androidx.lifecycle.Lifecycle.State.STARTED;

/**
 * 自定义定位服务，具有注册和解注册
 */
public class MyLocation implements LifecycleObserver {
    Lifecycle mLifecycle;

    public MyLocation(Lifecycle lifecycle){
        mLifecycle = lifecycle;
        // 将自己加入到目标组件生命周期的监听列表中
        mLifecycle.addObserver(this);//这种应用不对，交叉引用
    }

    // 在onCreate时调用
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void register(){
        Log.d("wch", "注册定位服务");
        enable();
    }

    //isAtLeast()
    public void enable() {
        if (mLifecycle.getCurrentState().isAtLeast(STARTED)) {
            // 其他操作
        }
    }

    // 在onDetsory时调用
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void unregister(){
        Log.d("wch", "解注册定位服务");
    }
}

