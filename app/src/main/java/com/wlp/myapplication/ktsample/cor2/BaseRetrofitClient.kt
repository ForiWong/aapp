package com.wlp.myapplication.ktsample.cor2

import com.wlp.myapplication.BuildConfig
import com.wlp.myapplication.util.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {
    companion object {
        private const val TIME_OUT = 6
    }
    
    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BASIC
            }

            builder.addInterceptor(logging)
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            handleBuilder(builder)
            return builder.build()
        }
    
    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)
    
    fun <S> getService(clazz: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build().create(clazz)
    }

    object MyRetrofitClient : BaseRetrofitClient() {
        val service by lazy {
//            getService(MyApiService::class.java, "BASE_URL")
        }

        override fun handleBuilder(builder: OkHttpClient.Builder){

        }
    }
    
}