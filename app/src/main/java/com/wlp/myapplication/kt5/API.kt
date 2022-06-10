package com.wlp.myapplication.kt5

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {

    val httpBuilder: OkHttpClient.Builder
        get() {
            // create http client
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val original = chain.request()
                    //header
                    val request = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method, original.body)
                        .build()
                    return@Interceptor chain.proceed(request)
                })
                .readTimeout(30, TimeUnit.SECONDS)

            // log interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(loggingInterceptor)

            return httpClient
        }

    // core for controller
    val service: IApiServices = create(IApiServices::class.java)

    lateinit var retrofit: Retrofit

    fun <S> create(serviceClass: Class<S>): S {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()

        // create retrofit
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("www") // Put your base URL
            .client(httpBuilder.build())
            .build()

        return retrofit.create(serviceClass)
    }
}
