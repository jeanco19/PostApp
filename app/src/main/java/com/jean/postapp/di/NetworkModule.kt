package com.jean.postapp.di

import com.jean.postapp.data.network.retrofit.RetrofitService
import com.jean.postapp.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // RETROFIT INSTANCE

    @Singleton
    @Provides
    fun provideBaseUrl(): String = BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(url: String, okHttpClient: OkHttpClient): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RetrofitService::class.java)
    }

    // COROUTINES INSTANCE

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}