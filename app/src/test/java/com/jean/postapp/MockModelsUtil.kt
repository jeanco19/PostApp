package com.jean.postapp

import com.jean.postapp.domain.model.post.Comment
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.domain.model.user.Author

const val GENERIC_ERROR = "Ha ocurrido un error inesperado."
const val GET_DETAIL_ERROR = "Ha ocurrido un error al recuperar la información de la publicación."
const val ADD_FAVORITE_SUCCESSFULLY = "Se agregó publicación a favoritas."
const val REMOVE_FAVORITE_SUCCESSFULLY = "Se retiró publicación de favoritas."

fun providePost(isSeen: Boolean, isFavorite: Boolean): Post {
    return Post(
        id = 1,
        userId = 1,
        title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        body = "quia et suscipit\n" +
                "suscipit recusandae consequuntur expedita et cum\n" +
                "reprehenderit molestiae ut ut quas totam\n" +
                "nostrum rerum est autem sunt rem eveniet architecto",
        isSeen = isSeen,
        isFavorite = isFavorite
    )
}

fun provideComment(): Comment {
    return Comment(
        id = 1,
        name = "id labore ex et quam laborum",
        body = "\"laudantium enim quasi est quidem magnam voluptate ipsam " +
                "eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis " +
                "et nam sapiente accusantium"
    )
}

fun provideAuthor(): Author {
    return Author(
        id = 1,
        name = "Leanne Graham",
        email = "Sincere@april.biz",
        phone = "1-770-736-8031 x56442",
        website = "hildegard.org"
    )
}