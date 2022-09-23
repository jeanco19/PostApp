package com.jean.postapp

import com.jean.postapp.data.database.entity.PostEntity
import com.jean.postapp.domain.model.post.Comment
import com.jean.postapp.domain.model.post.Post
import com.jean.postapp.domain.model.user.Author

fun providePostEntity(isSeen: Boolean, isFavorite: Boolean): PostEntity {
    return PostEntity(
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

fun providePost(isSeen: Boolean, isFavorite: Boolean): Post {
    return Post(
        id = 1,
        userId = 1,
        title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        body = "quia et suscipit\n" +
                "suscipit recusandae consequuntur expedita et cum\n" +
                "reprehenderit molestiae ut ut quas totam\n" +
                "nostrum rerum est autem sunt rem eveniet architecto",
        author = Author(
            id = 1,
            name = "Leanne Graham",
            email = "Sincere@april.biz",
            phone = "1-770-736-8031 x56442",
            website = "hildegard.org"
        ),
        comments = listOf(Comment(
            id = 1,
            name = "id labore ex et quam laborum",
            body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\n" +
                    "tempora quo necessitatibus\n" +
                    "dolor quam autem quasi\n" +
                    "reiciendis et nam sapiente accusantium"
        )),
        isSeen = isSeen,
        isFavorite = isFavorite
    )
}