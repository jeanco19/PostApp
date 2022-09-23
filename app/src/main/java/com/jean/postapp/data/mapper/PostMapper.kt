package com.jean.postapp.data.mapper

import com.jean.postapp.data.database.entity.PostEntity
import com.jean.postapp.data.network.retrofit.response.post.CommentApiModel
import com.jean.postapp.data.network.retrofit.response.post.PostApiModel
import com.jean.postapp.domain.model.post.Comment
import com.jean.postapp.domain.model.post.Post

fun PostApiModel.toPost(): Post {
    return Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}

fun Post.toPostApiModel(): PostApiModel {
    return PostApiModel(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}

fun PostEntity.toPost(): Post {
    return Post(
        id = id,
        userId = userId,
        title = title,
        body = body,
        isSeen = isSeen,
        isFavorite = isFavorite
    )
}

fun Post.toPostEntity(): PostEntity {
    return PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body,
        isSeen = isSeen,
        isFavorite = isFavorite
    )
}

fun CommentApiModel.toComment(): Comment {
    return Comment(
        id = id,
        name = name,
        body = body
    )
}

fun Comment.toCommentApiModel(): CommentApiModel {
    return CommentApiModel(
        id = id,
        postId = 1,
        name = name,
        email = "",
        body = body
    )
}