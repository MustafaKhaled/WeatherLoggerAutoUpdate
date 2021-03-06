package com.kot.weatherloggerautoupdate.di


import com.kot.weatherloggerautoupdate.BuildConfig
import com.kot.weatherloggerautoupdate.data.remote.ApiServices

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {

    single { createOkHttpClientInstancee() }
    single { createRetrofitClientInstance(get(), BuildConfig.BASE_URL) }
    single { createApiServiceInstance(get())}

}

fun createOkHttpClientInstancee(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}

fun createRetrofitClientInstance(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

    fun createApiServiceInstance(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)

    }


