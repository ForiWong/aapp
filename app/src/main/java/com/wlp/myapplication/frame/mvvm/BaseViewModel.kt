package com.wlp.myapplication.frame.mvvm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.wlp.myapplication.frame.event.SingleLiveEvent
import com.wlp.myapplication.util.LogUtils
import com.wlp.myapplication.util.Utils

/**
 * Description:ViewModel的基类
 */
open class BaseViewModel<M : BaseModel?> : AndroidViewModel, IBaseViewModel {
    private var model: M?

    private var uiChangeLiveData: UIChangeLiveData? = null

    constructor(application: Application) : super(application) {
        LogUtils.d("ViewModel onCreated(c): $this")
        model = null
        init()
    }

    constructor(application: Application, model: M) : super(application) {
        LogUtils.d("ViewModel onCreated(c,m): $this")
        this.model = model
        init()
    }

    /**
     * 做一些初始化操作
     */
    fun init() {}

    fun getUiChangeData(): UIChangeLiveData? {
        if (uiChangeLiveData == null) {
            uiChangeLiveData = UIChangeLiveData()
        }
        return uiChangeLiveData
    }

    fun showDialog(resId: Int) {
        uiChangeLiveData?.showDialogEvent?.postValue(Utils.getStringRes(resId))
    }

    @JvmOverloads
    fun showDialog(title: String? = "请稍后...") {
        uiChangeLiveData?.showDialogEvent?.postValue(title)
    }

    fun dismissDialog() {
        uiChangeLiveData?.dismissDialogEvent?.call()
    }

    fun showRemindDialog(remind: String?) {
        uiChangeLiveData?.remindDialogEvent?.postValue(remind)
    }

    fun dismissRemindDialog() {
        uiChangeLiveData?.dismissRemindDialogEvent?.call()
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    @JvmOverloads
    fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
        val params: MutableMap<String, Any> = HashMap()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uiChangeLiveData?.startActivityEvent?.postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        uiChangeLiveData?.finishEvent?.call()
    }

    /**
     * 返回上一层
     */
    fun onBackPressed() {
        uiChangeLiveData?.onBackPressedEvent?.call()
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {}
    override fun onCreate() {}
    override fun onDestroy() {}
    override fun onStart() {}
    override fun onStop() {}
    override fun onResume() {}
    override fun onPause() {}

    /**
     * 显示、启动、关闭、返回事件，具有防重复点击作用
     * TODO 作用？ 有必要继承吗？
     *
     * also函数返回值为传入的对象本身
     * createLiveData(field).also {field = it}
     * //意思是将私有属性showDialogEvent(field)传递给createLiveData -> it，返回值it 再赋值给field,最后函数返回it
     */
    inner class UIChangeLiveData : SingleLiveEvent<Any?>() {
        var showDialogEvent //请求中/加载中...等提示弹框
                : SingleLiveEvent<String?>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var dismissDialogEvent: SingleLiveEvent<Void>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var startActivityEvent: SingleLiveEvent<Map<String, Any>>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var finishEvent: SingleLiveEvent<Void>? = null
            get() = createLiveData(field).also { field = it }
            private set
        var onBackPressedEvent: SingleLiveEvent<Void>? = null
            get() = createLiveData(field).also { field = it }
            private set

        //需点击确定的提示弹框
        var remindDialogEvent: SingleLiveEvent<String>? = null
            get() = createLiveData(field).also { field = it }
            private set

        //隐藏需点击确定的提示弹框
        var dismissRemindDialogEvent: SingleLiveEvent<Void>? = null
            get() = createLiveData(field).also { field = it }
            private set

        private fun <T> createLiveData(mLiveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
            var liveData = mLiveData
            if (liveData == null) {
                liveData = SingleLiveEvent<T>()
            }
            return liveData
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in Any?>) {
            super.observe(owner, observer)
        }

    }

    object ParameterField {
        var CLASS = "CLASS"
        var BUNDLE = "BUNDLE"
    }
}