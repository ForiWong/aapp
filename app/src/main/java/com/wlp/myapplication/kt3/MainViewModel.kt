package com.wlp.myapplication.kt3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    private val _user = MutableLiveData<Pair<Boolean, String?>>()
    public val user : LiveData<Pair<Boolean, String?>> = _user

    private fun getHttpList(): HttpList {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://mockjs.xiaoyaoji.cn/mock/1k8Ou7Sxyro/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(HttpList::class.java)
    }

    public fun getUserData(){
        viewModelScope.launch {
            postUserData()
        }
    }

    public suspend fun postUserData() = withContext(Dispatchers.IO) {
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