package com.wlp.myapplication.kt4

/**
 * 网络返回数据基类
 */
data class BaseResult<T>(val code: String, val msg: String, val data: T)
