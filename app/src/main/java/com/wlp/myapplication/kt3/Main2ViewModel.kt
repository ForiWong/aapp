package com.wlp.myapplication.kt3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Main2ViewModel : ViewModel() {
    /**
     * 创建协程统一上下文
     */
    private val mJob = SupervisorJob()

    /**
     * 创建io协程
     */
    private val mIoScope = CoroutineScope(Dispatchers.IO + mJob)

    private val _user = MutableLiveData<Pair<Boolean, String?>>()
    public val user: LiveData<Pair<Boolean, String?>> = _user

    override fun onCleared() {
        super.onCleared()
        mJob.cancel()//取消正在请求网络接口的协程
    }

    private fun getHttpList(): HttpList {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://mockjs.xiaoyaoji.cn/mock/1k8Ou7Sxyro/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(HttpList::class.java)
    }

    public fun getUserData() {
        mIoScope.launch {
            yield()
            val response = getHttpList().getNameData()
            if (response.isSuccessful) {
                //请求成功
                _user.postValue(Pair(true, response.body()?.name))
            } else {
                //请求失败
                _user.postValue(Pair(false, "网络异常,${response.errorBody().toString()}"))
            }
        }
    }
}