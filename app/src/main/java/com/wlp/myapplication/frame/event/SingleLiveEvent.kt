package com.wlp.myapplication.frame.event

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wlp.myapplication.util.LogUtils
import java.util.concurrent.atomic.AtomicBoolean

/**
 * TODO 再看下 UnPeek-LiveData https://github.com/KunMinX/UnPeek-LiveData
 * Description:单次响应的LiveData
 * LiveData实时数据，对于UI组件是被观察者，用于数据变化时更新UI显示。
 * LiveData可以感知观察者的生命周期。
 * SingleLiveEvent的作用是只相应一次onChanged操作。
 */
open class SingleLiveEvent<T> : MutableLiveData<T?>() {
    companion object {
        private const val TAG = "SingleLiveEvent"
    }

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        if (hasActiveObservers()) {
            LogUtils.w(
                TAG,
                "Multiple observers registered but only one will be notified of changes."
            )
        }
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}