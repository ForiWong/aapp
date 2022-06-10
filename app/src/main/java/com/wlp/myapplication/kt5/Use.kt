package com.wlp.myapplication.kt5

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by wlp on 2022/6/10
 * Description:
 */

private const val TAG = "Use"

class Use {
    //不使用用协程
    fun getAll() {
        API.service.getAllHealthData("").enqueue(object : Callback<AllHealthBean?> {
            override fun onResponse(
                call: Call<AllHealthBean?>,
                response: Response<AllHealthBean?>
            ) {
                Log.e(TAG, "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<AllHealthBean?>, t: Throwable) {}
        })
    }

    //使用协程
    fun getAll2() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val allHealthBean = API.service.getAllHealthData("")
                Log.e(TAG, "launch initRetrofit: ${allHealthBean.toString()}")
            } catch (e: Exception) {
                Log.e(TAG, "initRetrofit: ${e.toString()}")
            }
        }
    }
}