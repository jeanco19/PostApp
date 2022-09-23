package com.jean.postapp.di

import com.jean.postapp.data.datasource.post.PostLocalDataSource
import com.jean.postapp.data.datasource.post.PostRemoteDataSource
import com.jean.postapp.data.datasource.user.UserRemoteDataSource
import com.jean.postapp.data.repository.post.PostRepository
import com.jean.postapp.data.repository.post.PostRepositoryImpl
import com.jean.postapp.data.repository.user.UserRepository
import com.jean.postapp.data.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // POST MODULE

    @Singleton
    @Provides
    fun providePostRepository(
        postRemoteDataSource: PostRemoteDataSource,
        localDataSource: PostLocalDataSource,
        ioDispatcher: CoroutineDispatcher
    ): PostRepository {
        return PostRepositoryImpl(postRemoteDataSource, localDataSource, ioDispatcher)
    }

    // USER MODULE

    @Singleton
    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        ioDispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource, ioDispatcher)
    }
}