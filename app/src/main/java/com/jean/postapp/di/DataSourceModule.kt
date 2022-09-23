package com.jean.postapp.di

import com.jean.postapp.data.database.PostDao
import com.jean.postapp.data.datasource.post.PostLocalDataSource
import com.jean.postapp.data.datasource.post.PostLocalDataSourceImpl
import com.jean.postapp.data.datasource.post.PostRemoteDataSource
import com.jean.postapp.data.datasource.post.PostRemoteDataSourceImpl
import com.jean.postapp.data.datasource.user.UserRemoteDataSource
import com.jean.postapp.data.datasource.user.UserRemoteDataSourceImpl
import com.jean.postapp.data.network.retrofit.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    // POST MODULE

    @Singleton
    @Provides
    fun providePostRemoteDataSource(
        retrofitService: RetrofitService,
        ioDispatcher: CoroutineDispatcher
    ): PostRemoteDataSource {
        return PostRemoteDataSourceImpl(retrofitService, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providePostLocalDataSource(
        postDao: PostDao,
        ioDispatcher: CoroutineDispatcher
    ): PostLocalDataSource {
        return PostLocalDataSourceImpl(postDao, ioDispatcher)
    }

    // USER MODULE

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        retrofitService: RetrofitService,
        ioDispatcher: CoroutineDispatcher
    ): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(retrofitService, ioDispatcher)
    }
}