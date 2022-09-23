package com.jean.postapp.data.database

import androidx.room.*
import com.jean.postapp.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postEntity: PostEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePost(postEntity: PostEntity)

    @Query("SELECT * FROM post_table WHERE id = :postId")
    suspend fun getPost(postId: Int): PostEntity

    @Query("SELECT * FROM post_table")
    suspend fun getAllPost(): List<PostEntity>

    @Query("SELECT * FROM post_table WHERE isFavorite = 1")
    fun getFavoritePosts(): Flow<List<PostEntity>>

    @Delete
    suspend fun removePost(postEntity: PostEntity)

    @Query("DELETE FROM post_table")
    suspend fun removeAllPost()
}