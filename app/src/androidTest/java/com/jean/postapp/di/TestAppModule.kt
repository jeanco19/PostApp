package com.jean.postapp.di

import android.content.Context
import androidx.room.Room
import com.jean.postapp.data.database.PostDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context): PostDatabase {
        return Room.inMemoryDatabaseBuilder(context, PostDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}