package com.jean.postapp.data.util

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T): Result<T>(data)
    class Failure<T>(message: String? = null, data: T? = null): Result<T>(data, message)
}