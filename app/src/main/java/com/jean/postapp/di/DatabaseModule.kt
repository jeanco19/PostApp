package com.jean.postapp.di

import android.content.Context
import androidx.room.Room
import com.jean.postapp.data.database.PostDatabase
import com.jean.postapp.util.Constant.POST_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providePostDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        PostDatabase::class.java,
        POST_DATABASE
    ).build()

    @Singleton
    @Provides
    fun providePostDao(postDatabase: PostDatabase) = postDatabase.postDao()
}