package com.wlp.myapplication.ktsample.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 创建一个继承ViewModel的类，在这个类中创建一个MutableLiveData的变量，并给他设置获取和设置值的方法
 */
class MyViewModel : ViewModel() {
    var number: MutableLiveData<Int> = MutableLiveData(11)

    fun get(): MutableLiveData<Int> {
        return number
    }

    fun add(value: Int) {
        number.value = number.value?.plus(value)
    }

    fun set(value: Int) {
        number.value = value
    }
}
