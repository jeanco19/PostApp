package com.jean.postapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jean.postapp.data.database.entity.PostEntity

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
}