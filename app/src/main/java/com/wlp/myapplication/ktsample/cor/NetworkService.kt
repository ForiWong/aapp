package com.wlp.myapplication.ktsample.cor

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by wlp on 2022/6/13
 * Description:
 */
interface NetworkService {

    @GET("cxyzy1/AndroidAsyncTaskDemo/raw/master/data.json")
    suspend fun query(): Task

    @POST("app/v1/version/checkVersion")
    suspend fun checkVersion(@Query("currentVersion")currentVersion: String): AppVersionInfo
}