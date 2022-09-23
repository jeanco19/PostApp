package com.jean.postapp.data.util

import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> Response<T>.bodyOrException(): T {
    val body = body()
    if (body != null && isSuccessful) {
        return body
    } else {
        throw HttpException(this)
    }
}