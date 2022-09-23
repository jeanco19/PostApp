package com.jean.postapp.data.mapper

import com.jean.postapp.data.network.retrofit.response.author.AuthorApiModel
import com.jean.postapp.domain.model.user.Author

fun AuthorApiModel.toAuthor(): Author {
    return Author(
        id = id,
        name = name,
        email = email,
        phone = phone,
        website = website
    )
}

fun Author.toAuthorApiModel(): AuthorApiModel {
    return AuthorApiModel(
        id = id,
        name = name,
        username = "",
        email = email,
        phone = phone,
        website = website
    )
}