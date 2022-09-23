package com.jean.postapp.util

object Constant {

    // NETWORK

    const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // DATABASE

    const val POST_DATABASE = "post_database"
    const val POST_TABLE = "post_table"

    // INTENT

    const val EXTRA_POST_ID = "post_id"

    // GENERAL MESSAGES
    const val EMPTY_STRING = ""
    const val GENERIC_ERROR = "Ha ocurrido un error inesperado."
    const val GET_DETAIL_ERROR = "Ha ocurrido un error al recuperar la información de la publicación."
    const val ADD_FAVORITE_SUCCESSFULLY = "Se agregó publicación a favoritas."
    const val REMOVE_FAVORITE_SUCCESSFULLY = "Se retiró publicación de favoritas."
}