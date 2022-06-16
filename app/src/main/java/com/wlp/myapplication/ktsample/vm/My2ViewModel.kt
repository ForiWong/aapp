package com.wlp.myapplication.ktsample.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by wlp on 2022/6/9
 * Description:
 * ViewModel的作用
 * 1、ViewModel 用于管理与界面（Activity、Fragment）相关的数据。
 * 2、ViewModel让数据可在发生屏幕旋转等配置更改后仍能继续存在。
 * 3、ViewModel 让Activity与Fragment共享数据更方便。
 * ViewModel作为JetPack中一个重要部件，本身并不复杂。ViewModel总结起来就一个功能：保存数据，并且在ViewModel
 * 中的数据，不会因为配置变化（横竖屏转换）而丢失，只有在Activity真正被销毁的时候，才会真正销毁数据。
 */
class My2ViewModel : ViewModel() {
    //对外变量 保证数据安全
    val number: LiveData<Int>
        //使number获取的值是_number
        get() = _number;
        //对内变量

    //变量初始化方法1 立即初始化
    private var _number: MutableLiveData<Int> = MutableLiveData(0)

    fun modifyNumber(aNumber: Int) {
        _number.value = _number.value?.plus(aNumber)
    }

//初始化方式2 立刻初始化
//    init {
//        _number = MutableLiveData()
//        _number.value = 0
//    }

//初始化方式三 当需要调用时 进行初始化
// private var _number: MutableLiveData<Int> by lazy { MutableLiveData<Int>().also { it.value = 0 } }

}