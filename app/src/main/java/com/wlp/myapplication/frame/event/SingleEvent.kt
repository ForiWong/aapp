package com.wlp.myapplication.frame.event

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * 包装一个数据T data，代表一个事件event,通过LiveData暴露出来
 */
open class SingleEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     * 返回内容，阻止其再次使用
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
