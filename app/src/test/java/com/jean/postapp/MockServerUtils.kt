package com.jean.postapp

import com.jean.postapp.data.network.retrofit.RetrofitService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofitService(baseUrl: String): RetrofitService {

    val okHttpClient = OkHttpClient
        .Builder()
        .build()

    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build().create(RetrofitService::class.java)
}