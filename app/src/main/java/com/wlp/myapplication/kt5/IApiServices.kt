package com.wlp.myapplication.kt5

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiServices {
    /**
     * 不使用协程
     * 需要返回一个Call对象使用
     */
    @GET("getHealthCare")
    fun getAllHealthData(@Query("userId") userId: String): Call<AllHealthBean>

    /**
     * 使用协程
     * 直接返回数据类对象
     */
    @GET("getHealthCare")
    suspend fun getAllHealthData2(@Query("userId") userId: String): AllHealthBean

}
