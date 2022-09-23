package com.jean.postapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jean.postapp.util.Constant.POST_TABLE

@Entity(tableName = POST_TABLE)
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isSeen: Boolean,
    val isFavorite: Boolean
)