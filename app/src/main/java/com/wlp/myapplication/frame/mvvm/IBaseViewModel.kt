package com.wlp.myapplication.frame.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.LifecycleOwner

/**
 * Description:ViewModel接口
 *
 * Lifecycle可以感知Activity和Fragment的生命周期。
 * （1）实现LifecycleObserver接口，该接口不提供任何方法，而是依靠OnLifecycleEvent注解实现生命周期感知。
 * （2）将LifecycleObserver的实现类添加到Activity中：getLifecycle().addObserver(lifecycleObserver);
 * （3）当Activity回调生命周期函数时，与生命周期对应的被OnLifecycleEvent注解修饰的方法会执行。
 *
 */
interface IBaseViewModel : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()
}