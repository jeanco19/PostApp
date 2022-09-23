package com.jean.postapp.domain.model.post

import android.os.Parcelable
import com.jean.postapp.domain.model.user.Author
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    var author: Author? = null,
    var comments: List<Comment> = listOf(),
    var isSeen: Boolean = false,
    var isFavorite: Boolean = false
) : Parcelable